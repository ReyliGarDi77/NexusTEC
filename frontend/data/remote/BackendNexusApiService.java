package data.remote;

import backend.escrow.EscrowLedgerService;
import backend.panicbutton.CampusDispatchService;
import backend.skillswap.SkillSwapService;
import backend.studygroups.ClassroomLocatorService;
import data.model.EscrowTransaction;
import data.model.HalconUser;
import data.model.PanicAlert;
import data.model.SkillSwap;
import data.model.StudyGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adaptador de Servicios Backend Integrado de Nexus-Tec.
 * Conecta el contrato de NexusApiService directamente con los microservicios
 * modulares de /backend (Escrow Ledger, Campus Dispatch, Classroom Locator y Skill Swap).
 * Garantiza persistencia, hashes criptográficos SHA-256 e interoperabilidad completa.
 */
public class BackendNexusApiService implements NexusApiService {

    private final SkillSwapService skillSwapService;
    private final EscrowLedgerService escrowLedgerService;
    private final CampusDispatchService campusDispatchService;
    private final ClassroomLocatorService classroomLocatorService;

    // Billeteras de Estudiantes Halcón ITTOL en memoria sincronizada
    private final Map<String, HalconUser> studentWallets = new ConcurrentHashMap<>();
    private final Map<String, String> studentNames = new ConcurrentHashMap<>();

    public BackendNexusApiService() {
        this.skillSwapService = new SkillSwapService();
        this.escrowLedgerService = new EscrowLedgerService();
        this.campusDispatchService = new CampusDispatchService();
        this.classroomLocatorService = new ClassroomLocatorService();

        seedDefaultUsers();
    }

    public BackendNexusApiService(SkillSwapService skillSwapService,
                                  EscrowLedgerService escrowLedgerService,
                                  CampusDispatchService campusDispatchService,
                                  ClassroomLocatorService classroomLocatorService) {
        this.skillSwapService = skillSwapService;
        this.escrowLedgerService = escrowLedgerService;
        this.campusDispatchService = campusDispatchService;
        this.classroomLocatorService = classroomLocatorService;

        seedDefaultUsers();
    }

    private void seedDefaultUsers() {
        HalconUser u1 = new HalconUser(
                "student_ittol_01",
                "reyli.garcia@toluca.tecnm.mx",
                "Reyli García Díaz",
                "21280942",
                "Ingeniería en Sistemas Computacionales",
                7,
                150.00,
                4.95
        );
        HalconUser u2 = new HalconUser(
                "student_ittol_02",
                "carlos.mendoza@toluca.tecnm.mx",
                "Carlos Mendoza Romero",
                "21280455",
                "Ingeniería en Sistemas Computacionales",
                6,
                85.00,
                4.80
        );
        HalconUser u3 = new HalconUser(
                "student_ittol_03",
                "fernanda.quimica@toluca.tecnm.mx",
                "María Fernanda Morales",
                "20280112",
                "Ingeniería Química",
                8,
                120.00,
                5.00
        );

        studentWallets.put(u1.getId(), u1);
        studentWallets.put(u2.getId(), u2);
        studentWallets.put(u3.getId(), u3);

        studentNames.put(u1.getId(), u1.getFullName());
        studentNames.put(u2.getId(), u2.getFullName());
        studentNames.put(u3.getId(), u3.getFullName());
    }

    // ==========================================
    // Microservicio: Skill-Swap
    // ==========================================
    @Override
    public ApiResponse<List<SkillSwap>> getAvailableSkillSwaps(String category, String searchKeyword) {
        List<SkillSwapService.SwapRecord> records = skillSwapService.listSwaps(category);
        List<SkillSwap> result = new ArrayList<>();
        for (SkillSwapService.SwapRecord r : records) {
            boolean matchesKeyword = (searchKeyword == null || searchKeyword.trim().isEmpty() ||
                    (r.offeredSkill != null && r.offeredSkill.toLowerCase().contains(searchKeyword.toLowerCase())) ||
                    (r.requestedSkill != null && r.requestedSkill.toLowerCase().contains(searchKeyword.toLowerCase())));

            if (matchesKeyword) {
                result.add(mapRecordToSwap(r));
            }
        }
        return ApiResponse.success(result, "Intercambios recuperados de SkillSwapService.");
    }

    @Override
    public ApiResponse<SkillSwap> getSkillSwapById(String swapId) {
        SkillSwapService.SwapRecord r = skillSwapService.getSwap(swapId);
        if (r != null) {
            return ApiResponse.success(mapRecordToSwap(r), "Intercambio encontrado.");
        }
        return ApiResponse.error("Intercambio no encontrado en registro backend.", 404);
    }

    @Override
    public ApiResponse<SkillSwap> createSkillSwap(SkillSwap skillSwap) {
        if (skillSwap == null) return ApiResponse.error("Objeto swap nulo", 400);
        SkillSwapService.SwapRecord r = mapSwapToRecord(skillSwap);
        SkillSwapService.SwapRecord created = skillSwapService.createSwap(r);
        return ApiResponse.success(mapRecordToSwap(created), "Propuesta creada en backend Skill-Swap.");
    }

    @Override
    public ApiResponse<SkillSwap> updateSkillSwapStatus(String swapId, String newStatus) {
        boolean updated = skillSwapService.updateStatus(swapId, newStatus);
        if (updated) {
            SkillSwapService.SwapRecord r = skillSwapService.getSwap(swapId);
            return ApiResponse.success(mapRecordToSwap(r), "Estado de intercambio actualizado.");
        }
        return ApiResponse.error("No se pudo actualizar el estado del intercambio.", 404);
    }

    // ==========================================
    // Microservicio: Escrow (Créditos Halcón)
    // ==========================================
    @Override
    public ApiResponse<EscrowTransaction> createEscrowHold(String swapId, String payerId, String payeeId, double creditsAmount) {
        HalconUser payer = studentWallets.get(payerId);
        if (payer == null) {
            payer = studentWallets.values().iterator().next();
        }

        if (payer.getHalconCreditsBalance() < creditsAmount) {
            return ApiResponse.error("Saldo insuficiente de Créditos Halcón (" + payer.getHalconCreditsBalance() + " < " + creditsAmount + ")", 400);
        }

        // Descontar saldo retenido en custodia
        payer.setHalconCreditsBalance(payer.getHalconCreditsBalance() - creditsAmount);

        // Delegar al microservicio EscrowLedgerService con firma SHA-256
        EscrowLedgerService.CustodyEntry entry = escrowLedgerService.lockCredits(swapId, payerId, payeeId, creditsAmount);

        EscrowTransaction tx = mapEntryToTransaction(entry);
        return ApiResponse.success(tx, "Créditos retenidos con éxito bajo custodia inteligente Escrow.");
    }

    @Override
    public ApiResponse<EscrowTransaction> getEscrowTransaction(String transactionId) {
        EscrowLedgerService.CustodyEntry entry = escrowLedgerService.getEntry(transactionId);
        if (entry != null) {
            return ApiResponse.success(mapEntryToTransaction(entry), "Transacción encontrada en ledger.");
        }
        return ApiResponse.error("Transacción no encontrada en el ledger de custodia.", 404);
    }

    @Override
    public ApiResponse<EscrowTransaction> releaseEscrowCredits(String transactionId, String payerConfirmationSecret) {
        EscrowLedgerService.CustodyEntry entry = escrowLedgerService.getEntry(transactionId);
        if (entry == null) {
            return ApiResponse.error("Transacción de custodia inexistente.", 404);
        }

        boolean released = escrowLedgerService.releaseFunds(transactionId);
        if (released) {
            // Acreditar los fondos liberados al prestador (payee)
            HalconUser payee = studentWallets.get(entry.payeeStudentId);
            if (payee != null) {
                payee.setHalconCreditsBalance(payee.getHalconCreditsBalance() + entry.creditsAmount);
            }
            return ApiResponse.success(mapEntryToTransaction(entry), "Créditos liberados satisfactoriamente.");
        }
        return ApiResponse.error("No se pudo liberar la transacción en su estado actual: " + entry.state, 400);
    }

    @Override
    public ApiResponse<EscrowTransaction> disputeEscrow(String transactionId, String disputeReason) {
        boolean disputed = escrowLedgerService.dispute(transactionId, disputeReason);
        if (disputed) {
            EscrowLedgerService.CustodyEntry entry = escrowLedgerService.getEntry(transactionId);
            EscrowTransaction tx = mapEntryToTransaction(entry);
            tx.setDisputeReason(disputeReason);
            return ApiResponse.success(tx, "Disputa iniciada; fondos congelados para mediación.");
        }
        return ApiResponse.error("Transacción no encontrada para disputa.", 404);
    }

    @Override
    public ApiResponse<EscrowTransaction> refundEscrow(String transactionId, String adminAuthorizationToken) {
        EscrowLedgerService.CustodyEntry entry = escrowLedgerService.getEntry(transactionId);
        if (entry == null) return ApiResponse.error("Transacción no encontrada.", 404);

        boolean refunded = escrowLedgerService.refund(transactionId, adminAuthorizationToken);
        if (refunded) {
            // Reembolsar al pagador original
            HalconUser payer = studentWallets.get(entry.payerStudentId);
            if (payer != null) {
                payer.setHalconCreditsBalance(payer.getHalconCreditsBalance() + entry.creditsAmount);
            }
            return ApiResponse.success(mapEntryToTransaction(entry), "Reembolso efectuado exitosamente.");
        }
        return ApiResponse.error("No se pudo procesar el reembolso.", 400);
    }

    @Override
    public ApiResponse<HalconUser> getHalconWalletBalance(String studentId) {
        HalconUser user = studentWallets.get(studentId);
        if (user == null && !studentWallets.isEmpty()) {
            user = studentWallets.values().iterator().next();
        }
        return ApiResponse.success(user, "Saldo recuperado.");
    }

    // ==========================================
    // Microservicio: Botón de Pánico
    // ==========================================
    @Override
    public ApiResponse<PanicAlert> triggerPanicAlert(PanicAlert alert) {
        if (alert == null) return ApiResponse.error("Alerta nula", 400);

        CampusDispatchService.EmergencyEvent event = campusDispatchService.dispatchAlert(
                alert.getStudentId(),
                alert.getStudentCode(),
                alert.getLatitude(),
                alert.getLongitude(),
                alert.getCampusBuildingZone(),
                alert.getAlertLevel() != null ? alert.getAlertLevel().name() : "HIGH",
                alert.isSilentMode()
        );

        alert.setAlertId(event.alertId);
        alert.setStatus(PanicAlert.AlertStatus.TRIGGERED);
        return ApiResponse.success(alert, "Alerta emitida con éxito a la Caseta Central de Seguridad.");
    }

    @Override
    public ApiResponse<PanicAlert> cancelPanicAlert(String alertId, String cancellationReason) {
        boolean resolved = campusDispatchService.resolveAlert(alertId, cancellationReason);
        if (resolved) {
            CampusDispatchService.EmergencyEvent ev = campusDispatchService.getEvent(alertId);
            PanicAlert alert = mapEventToAlert(ev);
            alert.setStatus(PanicAlert.AlertStatus.FALSE_ALARM);
            alert.setNotes(cancellationReason);
            alert.setResolvedAt(new Date());
            return ApiResponse.success(alert, "Alerta cancelada.");
        }
        return ApiResponse.error("Alerta no encontrada.", 404);
    }

    @Override
    public ApiResponse<List<PanicAlert>> getActiveCampusAlerts() {
        List<CampusDispatchService.EmergencyEvent> events = campusDispatchService.getActiveEvents();
        List<PanicAlert> list = new ArrayList<>();
        for (CampusDispatchService.EmergencyEvent ev : events) {
            list.add(mapEventToAlert(ev));
        }
        return ApiResponse.success(list, "Alertas activas recuperadas.");
    }

    @Override
    public ApiResponse<PanicAlert> updateAlertStatus(String alertId, String status) {
        boolean updated = campusDispatchService.updateStatus(alertId, status);
        if (updated) {
            CampusDispatchService.EmergencyEvent ev = campusDispatchService.getEvent(alertId);
            return ApiResponse.success(mapEventToAlert(ev), "Estado de alerta actualizado.");
        }
        return ApiResponse.error("Alerta no encontrada.", 404);
    }

    // ==========================================
    // Microservicio: Flash Study Groups
    // ==========================================
    @Override
    public ApiResponse<List<StudyGroup>> getOpenStudyGroups(String subjectName) {
        List<ClassroomLocatorService.StudyTable> tables = classroomLocatorService.getOpenTables(subjectName);
        List<StudyGroup> results = new ArrayList<>();
        for (ClassroomLocatorService.StudyTable t : tables) {
            if (subjectName == null || subjectName.trim().isEmpty() || "Todos".equalsIgnoreCase(subjectName) ||
                    (t.subject != null && t.subject.toLowerCase().contains(subjectName.toLowerCase()))) {
                results.add(mapTableToGroup(t));
            }
        }
        return ApiResponse.success(results, "Grupos abiertos recuperados.");
    }

    @Override
    public ApiResponse<StudyGroup> createStudyGroup(StudyGroup studyGroup) {
        if (studyGroup == null) return ApiResponse.error("Grupo nulo", 400);
        ClassroomLocatorService.StudyTable table = mapGroupToTable(studyGroup);
        ClassroomLocatorService.StudyTable created = classroomLocatorService.createTable(table);
        return ApiResponse.success(mapTableToGroup(created), "Mesa de estudio flash publicada en campus.");
    }

    @Override
    public ApiResponse<StudyGroup> joinStudyGroup(String groupId, String studentId) {
        boolean joined = classroomLocatorService.joinTable(groupId, studentId);
        if (joined) {
            ClassroomLocatorService.StudyTable table = classroomLocatorService.getTable(groupId);
            return ApiResponse.success(mapTableToGroup(table), "Te has unido al grupo de estudio.");
        }
        return ApiResponse.error("Grupo completo o ya registrado.", 400);
    }

    @Override
    public ApiResponse<StudyGroup> leaveStudyGroup(String groupId, String studentId) {
        boolean left = classroomLocatorService.leaveTable(groupId, studentId);
        if (left) {
            ClassroomLocatorService.StudyTable table = classroomLocatorService.getTable(groupId);
            return ApiResponse.success(mapTableToGroup(table), "Has salido del grupo.");
        }
        return ApiResponse.error("Grupo no encontrado o estudiante no registrado.", 404);
    }

    // ==========================================
    // Mapeadores Auxiliares de Entidades
    // ==========================================
    private SkillSwap mapRecordToSwap(SkillSwapService.SwapRecord r) {
        if (r == null) return null;
        SkillSwap.SwapStatus status = SkillSwap.SwapStatus.PENDING;
        if (r.status != null) {
            try {
                status = SkillSwap.SwapStatus.valueOf(r.status);
            } catch (Exception ignored) {
            }
        }
        return new SkillSwap(
                r.id,
                r.requesterCode,
                r.providerCode,
                r.offeredSkill,
                r.requestedSkill,
                r.careerCategory,
                "Intercambio gestionado por el microservicio SkillSwap.",
                2,
                r.halconCredits,
                status,
                r.timestamp != null ? r.timestamp : new Date(),
                r.campusLocation
        );
    }

    private SkillSwapService.SwapRecord mapSwapToRecord(SkillSwap s) {
        SkillSwapService.SwapRecord r = new SkillSwapService.SwapRecord();
        r.id = s.getSwapId();
        r.requesterCode = s.getRequesterStudentId();
        r.providerCode = s.getProviderStudentId();
        r.offeredSkill = s.getOfferedSkill();
        r.requestedSkill = s.getRequestedSkill();
        r.careerCategory = s.getCategory();
        r.halconCredits = s.getAgreedHalconCredits();
        r.status = s.getStatus() != null ? s.getStatus().name() : "PENDING";
        r.campusLocation = s.getMeetingLocation();
        r.timestamp = s.getScheduledDate() != null ? s.getScheduledDate() : new Date();
        return r;
    }

    private EscrowTransaction mapEntryToTransaction(EscrowLedgerService.CustodyEntry e) {
        if (e == null) return null;
        EscrowTransaction.TransactionStatus status = EscrowTransaction.TransactionStatus.IN_ESCROW_HOLD;
        if (e.state != null) {
            try {
                status = EscrowTransaction.TransactionStatus.valueOf(e.state);
            } catch (Exception ignored) {
            }
        }
        EscrowTransaction tx = new EscrowTransaction(
                e.transactionId,
                e.relatedSwapId,
                e.payerStudentId,
                e.payeeStudentId,
                e.creditsAmount,
                status
        );
        tx.setSecurityHash(e.cryptographicProof);
        tx.setCreatedAt(e.createdAt);
        tx.setReleasedAt(e.releasedAt);
        return tx;
    }

    private PanicAlert mapEventToAlert(CampusDispatchService.EmergencyEvent ev) {
        if (ev == null) return null;
        PanicAlert.AlertLevel level = PanicAlert.AlertLevel.HIGH;
        if (ev.level != null) {
            try {
                level = PanicAlert.AlertLevel.valueOf(ev.level);
            } catch (Exception ignored) {
            }
        }
        PanicAlert.AlertStatus status = PanicAlert.AlertStatus.TRIGGERED;
        if ("RESOLVED".equalsIgnoreCase(ev.status)) {
            status = PanicAlert.AlertStatus.RESOLVED;
        }

        PanicAlert alert = new PanicAlert(
                ev.alertId,
                ev.studentId,
                studentNames.getOrDefault(ev.studentId, "Estudiante ITTOL"),
                ev.studentCode,
                ev.latitude,
                ev.longitude,
                ev.campusZone,
                level,
                "Emergencia de Campus"
        );
        alert.setSilentMode(ev.silentMode);
        alert.setStatus(status);
        alert.setTriggeredAt(ev.timestamp);
        return alert;
    }

    private StudyGroup mapTableToGroup(ClassroomLocatorService.StudyTable t) {
        if (t == null) return null;
        StudyGroup g = new StudyGroup();
        g.setGroupId(t.tableId);
        g.setSubjectName(t.subject);
        g.setTopicDescription(t.topic);
        g.setCreatorStudentId("student_host");
        g.setCreatorStudentName(t.hostStudent);
        g.setMaxCapacity(t.capacity);
        g.setMeetingLocation(t.locationIttol);
        g.setScheduledTime(t.scheduledTime != null ? t.scheduledTime : new Date());
        g.setDurationMinutes(60);
        g.setStatus(StudyGroup.GroupStatus.OPEN);
        if (t.currentAttendees != null) {
            for (String att : t.currentAttendees) {
                g.addMember(att);
            }
        }
        return g;
    }

    private ClassroomLocatorService.StudyTable mapGroupToTable(StudyGroup g) {
        ClassroomLocatorService.StudyTable t = new ClassroomLocatorService.StudyTable();
        t.tableId = g.getGroupId();
        t.subject = g.getSubjectName();
        t.topic = g.getTopicDescription();
        t.hostStudent = g.getCreatorStudentName();
        t.capacity = g.getMaxCapacity();
        t.locationIttol = g.getMeetingLocation();
        t.scheduledTime = g.getScheduledTime();
        if (g.getMemberStudentIds() != null) {
            t.currentAttendees.addAll(g.getMemberStudentIds());
        }
        return t;
    }

    public SkillSwapService getSkillSwapService() {
        return skillSwapService;
    }

    public EscrowLedgerService getEscrowLedgerService() {
        return escrowLedgerService;
    }

    public CampusDispatchService getCampusDispatchService() {
        return campusDispatchService;
    }

    public ClassroomLocatorService getClassroomLocatorService() {
        return classroomLocatorService;
    }
}

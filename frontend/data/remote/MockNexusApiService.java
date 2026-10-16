package data.remote;

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
 * Servicio simulador de backend Retrofit de alta fidelidad para pruebas, demostración y entorno offline en ITTOL.
 * Implementa el contrato completo de NexusApiService con persistencia en memoria y datos institucionales.
 */
public class MockNexusApiService implements NexusApiService {
    private final Map<String, SkillSwap> skillSwaps = new ConcurrentHashMap<>();
    private final Map<String, EscrowTransaction> escrowTransactions = new ConcurrentHashMap<>();
    private final Map<String, PanicAlert> panicAlerts = new ConcurrentHashMap<>();
    private final Map<String, StudyGroup> studyGroups = new ConcurrentHashMap<>();
    private final Map<String, HalconUser> users = new ConcurrentHashMap<>();

    public MockNexusApiService() {
        seedInitialIttolData();
    }

    private void seedInitialIttolData() {
        // Usuario Estudiante Halcón ITTOL
        HalconUser student = new HalconUser(
                "student_ittol_01",
                "reyli.garcia@toluca.tecnm.mx",
                "Reyli García Díaz",
                "21280942",
                "Ingeniería en Sistemas Computacionales",
                7,
                150.00,
                4.95
        );
        users.put(student.getId(), student);

        // Semilla: Skill-Swap
        SkillSwap swap1 = new SkillSwap(
                "swap_101",
                student.getId(),
                "student_ittol_02",
                "Estructura de Datos en Java & Algoritmos",
                "Cálculo Integral & Ecuaciones Diferenciales",
                "Ing. Sistemas",
                "Asesoría técnica para proyecto departamental y árboles AVL.",
                3,
                25.0,
                SkillSwap.SwapStatus.PENDING,
                new Date(),
                "Biblioteca Central ITTOL · Cubículo 3"
        );
        skillSwaps.put(swap1.getSwapId(), swap1);

        SkillSwap swap2 = new SkillSwap(
                "swap_102",
                "student_ittol_03",
                student.getId(),
                "Diseño de Circuitos Digitales & FPGA VHDL",
                "Programación Web React & Node.js",
                "Mecatrónica",
                "Configuración de placas FPGA y simulación en Quartus.",
                2,
                30.0,
                SkillSwap.SwapStatus.ACCEPTED,
                new Date(),
                "Edificio D · Laboratorio LC1"
        );
        skillSwaps.put(swap2.getSwapId(), swap2);

        // Semilla: Escrow (Créditos Halcón)
        EscrowTransaction tx1 = new EscrowTransaction(
                "escrow_901",
                swap1.getSwapId(),
                student.getId(),
                "student_ittol_02",
                25.0,
                EscrowTransaction.TransactionStatus.IN_ESCROW_HOLD
        );
        tx1.setSecurityHash("0x8F9A2B4C1D7E3F0A2B9C8D7E6F5A4B3C");
        escrowTransactions.put(tx1.getTransactionId(), tx1);

        // Semilla: Flash Study Groups
        StudyGroup group1 = new StudyGroup(
                "group_301",
                "Arquitectura de Computadoras",
                "Resolución de microinstrucciones y pipeline para el examen departamental.",
                student.getId(),
                student.getFullName(),
                5,
                "Edificio F · Cubículo 2",
                new Date(),
                90
        );
        group1.addMember("student_ittol_02");
        group1.addMember("student_ittol_03");
        studyGroups.put(group1.getGroupId(), group1);

        StudyGroup group2 = new StudyGroup(
                "group_302",
                "Termodinámica & Balances",
                "Taller práctico de resolución de ciclos combinados.",
                "student_ittol_04",
                "María Fernanda Morales (Química)",
                4,
                "Cafetería Halcón · Terraza Norte",
                new Date(),
                60
        );
        studyGroups.put(group2.getGroupId(), group2);
    }

    // ==========================================
    // Microservicio: Skill-Swap
    // ==========================================
    @Override
    public ApiResponse<List<SkillSwap>> getAvailableSkillSwaps(String category, String searchKeyword) {
        List<SkillSwap> results = new ArrayList<>();
        for (SkillSwap swap : skillSwaps.values()) {
            boolean matchesCategory = (category == null || category.isEmpty() || "Todos".equalsIgnoreCase(category) ||
                    swap.getCategory().toLowerCase().contains(category.toLowerCase()));
            boolean matchesKeyword = (searchKeyword == null || searchKeyword.isEmpty() ||
                    swap.getOfferedSkill().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                    swap.getRequestedSkill().toLowerCase().contains(searchKeyword.toLowerCase()));
            if (matchesCategory && matchesKeyword) {
                results.add(swap);
            }
        }
        return ApiResponse.success(results, "Intercambios obtenidos con éxito.");
    }

    @Override
    public ApiResponse<SkillSwap> getSkillSwapById(String swapId) {
        SkillSwap swap = skillSwaps.get(swapId);
        if (swap != null) {
            return ApiResponse.success(swap, "Intercambio encontrado.");
        }
        return ApiResponse.error("Intercambio no encontrado.", 404);
    }

    @Override
    public ApiResponse<SkillSwap> createSkillSwap(SkillSwap skillSwap) {
        if (skillSwap.getSwapId() == null || skillSwap.getSwapId().isEmpty()) {
            skillSwap.setSwapId("swap_" + System.currentTimeMillis());
        }
        skillSwaps.put(skillSwap.getSwapId(), skillSwap);
        return ApiResponse.success(skillSwap, "Propuesta de intercambio registrada en campus.");
    }

    @Override
    public ApiResponse<SkillSwap> updateSkillSwapStatus(String swapId, String newStatus) {
        SkillSwap swap = skillSwaps.get(swapId);
        if (swap != null) {
            try {
                swap.setStatus(SkillSwap.SwapStatus.valueOf(newStatus));
                return ApiResponse.success(swap, "Estado de intercambio actualizado.");
            } catch (Exception e) {
                return ApiResponse.error("Estado inválido.", 400);
            }
        }
        return ApiResponse.error("Intercambio no encontrado.", 404);
    }

    // ==========================================
    // Microservicio: Escrow (Créditos Halcón)
    // ==========================================
    @Override
    public ApiResponse<EscrowTransaction> createEscrowHold(String swapId, String payerId, String payeeId, double creditsAmount) {
        EscrowTransaction tx = new EscrowTransaction(
                "escrow_" + System.currentTimeMillis(),
                swapId,
                payerId,
                payeeId,
                creditsAmount,
                EscrowTransaction.TransactionStatus.IN_ESCROW_HOLD
        );
        tx.setSecurityHash("0x" + Long.toHexString(System.currentTimeMillis()).toUpperCase() + "SECUREHASH");
        escrowTransactions.put(tx.getTransactionId(), tx);
        return ApiResponse.success(tx, "Créditos retenidos con éxito bajo custodia inteligente Escrow.");
    }

    @Override
    public ApiResponse<EscrowTransaction> getEscrowTransaction(String transactionId) {
        EscrowTransaction tx = escrowTransactions.get(transactionId);
        if (tx != null) {
            return ApiResponse.success(tx, "Transacción recuperada.");
        }
        return ApiResponse.error("Transacción no encontrada.", 404);
    }

    @Override
    public ApiResponse<EscrowTransaction> releaseEscrowCredits(String transactionId, String payerConfirmationSecret) {
        EscrowTransaction tx = escrowTransactions.get(transactionId);
        if (tx != null) {
            tx.setStatus(EscrowTransaction.TransactionStatus.RELEASED);
            tx.setReleasedAt(new Date());
            return ApiResponse.success(tx, "Créditos liberados satisfactoriamente.");
        }
        return ApiResponse.error("Transacción no encontrada.", 404);
    }

    @Override
    public ApiResponse<EscrowTransaction> disputeEscrow(String transactionId, String disputeReason) {
        EscrowTransaction tx = escrowTransactions.get(transactionId);
        if (tx != null) {
            tx.setStatus(EscrowTransaction.TransactionStatus.DISPUTED);
            tx.setDisputeReason(disputeReason);
            return ApiResponse.success(tx, "Disputa iniciada; fondos congelados para mediación.");
        }
        return ApiResponse.error("Transacción no encontrada.", 404);
    }

    @Override
    public ApiResponse<EscrowTransaction> refundEscrow(String transactionId, String adminAuthorizationToken) {
        EscrowTransaction tx = escrowTransactions.get(transactionId);
        if (tx != null) {
            tx.setStatus(EscrowTransaction.TransactionStatus.REFUNDED);
            return ApiResponse.success(tx, "Reembolso efectuado.");
        }
        return ApiResponse.error("Transacción no encontrada.", 404);
    }

    @Override
    public ApiResponse<HalconUser> getHalconWalletBalance(String studentId) {
        HalconUser user = users.get(studentId);
        if (user == null && !users.isEmpty()) {
            user = users.values().iterator().next();
        }
        return ApiResponse.success(user, "Saldo recuperado.");
    }

    // ==========================================
    // Microservicio: Botón de Pánico
    // ==========================================
    @Override
    public ApiResponse<PanicAlert> triggerPanicAlert(PanicAlert alert) {
        if (alert.getAlertId() == null || alert.getAlertId().isEmpty()) {
            alert.setAlertId("alert_" + System.currentTimeMillis());
        }
        panicAlerts.put(alert.getAlertId(), alert);
        return ApiResponse.success(alert, "Alerta emitida con éxito a la Caseta Central de Seguridad.");
    }

    @Override
    public ApiResponse<PanicAlert> cancelPanicAlert(String alertId, String cancellationReason) {
        PanicAlert alert = panicAlerts.get(alertId);
        if (alert != null) {
            alert.setStatus(PanicAlert.AlertStatus.FALSE_ALARM);
            alert.setNotes(cancellationReason);
            alert.setResolvedAt(new Date());
            return ApiResponse.success(alert, "Alerta cancelada.");
        }
        return ApiResponse.error("Alerta no encontrada.", 404);
    }

    @Override
    public ApiResponse<List<PanicAlert>> getActiveCampusAlerts() {
        return ApiResponse.success(new ArrayList<>(panicAlerts.values()), "Alertas activas recuperadas.");
    }

    @Override
    public ApiResponse<PanicAlert> updateAlertStatus(String alertId, String status) {
        PanicAlert alert = panicAlerts.get(alertId);
        if (alert != null) {
            try {
                alert.setStatus(PanicAlert.AlertStatus.valueOf(status));
                return ApiResponse.success(alert, "Estado de alerta actualizado.");
            } catch (Exception e) {
                return ApiResponse.error("Estado inválido.", 400);
            }
        }
        return ApiResponse.error("Alerta no encontrada.", 404);
    }

    // ==========================================
    // Microservicio: Flash Study Groups
    // ==========================================
    @Override
    public ApiResponse<List<StudyGroup>> getOpenStudyGroups(String subjectName) {
        List<StudyGroup> results = new ArrayList<>();
        for (StudyGroup g : studyGroups.values()) {
            if (subjectName == null || subjectName.isEmpty() || "Todos".equalsIgnoreCase(subjectName) ||
                    g.getSubjectName().toLowerCase().contains(subjectName.toLowerCase())) {
                results.add(g);
            }
        }
        return ApiResponse.success(results, "Grupos abiertos recuperados.");
    }

    @Override
    public ApiResponse<StudyGroup> createStudyGroup(StudyGroup studyGroup) {
        if (studyGroup.getGroupId() == null || studyGroup.getGroupId().isEmpty()) {
            studyGroup.setGroupId("group_" + System.currentTimeMillis());
        }
        studyGroups.put(studyGroup.getGroupId(), studyGroup);
        return ApiResponse.success(studyGroup, "Mesa de estudio flash publicada en campus.");
    }

    @Override
    public ApiResponse<StudyGroup> joinStudyGroup(String groupId, String studentId) {
        StudyGroup group = studyGroups.get(groupId);
        if (group != null) {
            boolean added = group.addMember(studentId);
            if (added) {
                return ApiResponse.success(group, "Te has unido al grupo de estudio.");
            } else {
                return ApiResponse.error("Grupo completo o ya registrado.", 400);
            }
        }
        return ApiResponse.error("Grupo no encontrado.", 404);
    }

    @Override
    public ApiResponse<StudyGroup> leaveStudyGroup(String groupId, String studentId) {
        StudyGroup group = studyGroups.get(groupId);
        if (group != null) {
            group.getMemberStudentIds().remove(studentId);
            return ApiResponse.success(group, "Has salido del grupo.");
        }
        return ApiResponse.error("Grupo no encontrado.", 404);
    }
}


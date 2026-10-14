package data.remote;

import data.model.EscrowTransaction;
import data.model.HalconUser;
import data.model.PanicAlert;
import data.model.SkillSwap;
import data.model.StudyGroup;

import java.util.List;
import java.util.Map;

/**
 * Contrato de interfaz Retrofit para el consumo de los microservicios Nexus-Tec.
 * Modularizado para Skill-Swap, Escrow (Créditos Halcón), Botón de Pánico y Grupos de Estudio.
 */
public interface NexusApiService {

    // ==========================================
    // Microservicio: Skill-Swap
    // ==========================================
    ApiResponse<List<SkillSwap>> getAvailableSkillSwaps(String category, String searchKeyword);

    ApiResponse<SkillSwap> getSkillSwapById(String swapId);

    ApiResponse<SkillSwap> createSkillSwap(SkillSwap skillSwap);

    ApiResponse<SkillSwap> updateSkillSwapStatus(String swapId, String newStatus);

    // ==========================================
    // Microservicio: Escrow (Créditos Halcón)
    // ==========================================
    ApiResponse<EscrowTransaction> createEscrowHold(String swapId, String payerId, String payeeId, double creditsAmount);

    ApiResponse<EscrowTransaction> getEscrowTransaction(String transactionId);

    ApiResponse<EscrowTransaction> releaseEscrowCredits(String transactionId, String payerConfirmationSecret);

    ApiResponse<EscrowTransaction> disputeEscrow(String transactionId, String disputeReason);

    ApiResponse<EscrowTransaction> refundEscrow(String transactionId, String adminAuthorizationToken);

    ApiResponse<HalconUser> getHalconWalletBalance(String studentId);

    // ==========================================
    // Microservicio: Botón de Pánico (Emergencias)
    // ==========================================
    ApiResponse<PanicAlert> triggerPanicAlert(PanicAlert alert);

    ApiResponse<PanicAlert> cancelPanicAlert(String alertId, String cancellationReason);

    ApiResponse<List<PanicAlert>> getActiveCampusAlerts();

    ApiResponse<PanicAlert> updateAlertStatus(String alertId, String status);

    // ==========================================
    // Microservicio: Flash Study Groups
    // ==========================================
    ApiResponse<List<StudyGroup>> getOpenStudyGroups(String subjectName);

    ApiResponse<StudyGroup> createStudyGroup(StudyGroup studyGroup);

    ApiResponse<StudyGroup> joinStudyGroup(String groupId, String studentId);

    ApiResponse<StudyGroup> leaveStudyGroup(String groupId, String studentId);
}


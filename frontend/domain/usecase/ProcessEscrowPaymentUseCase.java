package domain.usecase;

import data.model.EscrowTransaction;
import data.repository.EscrowRepository;
import domain.state.EscrowContext;

/**
 * Caso de Uso: Orquestar el flujo de pago con Créditos Halcón bajo custodia (Escrow).
 * Aplica el patrón State para transicionar de forma segura el estado de los fondos.
 */
public class ProcessEscrowPaymentUseCase {
    private final EscrowRepository repository;

    public ProcessEscrowPaymentUseCase(EscrowRepository repository) {
        this.repository = repository != null ? repository : EscrowRepository.getInstance();
    }

    /**
     * Inicia el depósito y retención en custodia de los Créditos Halcón acordados.
     */
    public EscrowContext initiateEscrow(String swapId, String payerId, String payeeId, double creditsAmount) {
        double currentBalance = repository.getWalletBalance(payerId);
        if (currentBalance < creditsAmount) {
            throw new IllegalStateException("Saldo insuficiente de Créditos Halcón (" + currentBalance + " < " + creditsAmount + ")");
        }

        EscrowTransaction transaction = new EscrowTransaction();
        transaction.setTransactionId("escrow_" + System.currentTimeMillis());
        transaction.setRelatedSwapId(swapId);
        transaction.setPayerStudentId(payerId);
        transaction.setPayeeStudentId(payeeId);

        EscrowContext context = new EscrowContext(transaction);
        // Transición vía patrón State: PENDING_DEPOSIT -> IN_ESCROW_HOLD
        context.deposit(creditsAmount);
        context.lockInHold();

        repository.createEscrowHold(swapId, payerId, payeeId, creditsAmount);
        return context;
    }

    /**
     * Confirma la recepción del servicio y libera los fondos retenidos.
     */
    public void confirmAndRelease(EscrowContext context, String studentConfirmationSecret) {
        if (context == null) throw new IllegalArgumentException("Contexto de Escrow no válido.");
        context.releaseCredits(studentConfirmationSecret);
        repository.releaseCredits(context.getTransaction().getTransactionId(), studentConfirmationSecret);
    }

    /**
     * Reporta un conflicto y transiciona a estado en disputa.
     */
    public void dispute(EscrowContext context, String reason) {
        if (context == null) throw new IllegalArgumentException("Contexto de Escrow no válido.");
        context.dispute(reason);
        repository.disputeTransaction(context.getTransaction().getTransactionId(), reason);
    }
}


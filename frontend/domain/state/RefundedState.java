package domain.state;

/**
 * Estado Final de Reembolso: Los créditos fueron devueltos a la billetera del estudiante solicitante.
 */
public class RefundedState implements EscrowState {

    @Override
    public void deposit(EscrowContext context, double amountCredits) {
        throw new IllegalStateException("La transacción fue cancelada y reembolsada; no admite operaciones adicionales.");
    }

    @Override
    public void lockInHold(EscrowContext context) {
        throw new IllegalStateException("Transacción reembolsada y cerrada.");
    }

    @Override
    public void releaseCredits(EscrowContext context, String studentConfirmationSecret) {
        throw new IllegalStateException("Fondos ya devueltos al pagador. No pueden ser liberados.");
    }

    @Override
    public void dispute(EscrowContext context, String reason) {
        throw new IllegalStateException("No se puede disputar una transacción ya reembolsada.");
    }

    @Override
    public void refund(EscrowContext context, String authorizationToken) {
        throw new IllegalStateException("Los créditos ya fueron reembolsados con anterioridad.");
    }

    @Override
    public String getStateName() {
        return "REFUNDED";
    }
}

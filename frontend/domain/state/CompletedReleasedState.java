package domain.state;

/**
 * Estado Final Exitoso: Los créditos han sido liberados y acreditados en la cuenta del estudiante prestador.
 */
public class CompletedReleasedState implements EscrowState {

    @Override
    public void deposit(EscrowContext context, double amountCredits) {
        throw new IllegalStateException("La transacción ya finalizó con éxito; no se admiten nuevos depósitos.");
    }

    @Override
    public void lockInHold(EscrowContext context) {
        throw new IllegalStateException("La transacción ya fue completada.");
    }

    @Override
    public void releaseCredits(EscrowContext context, String studentConfirmationSecret) {
        throw new IllegalStateException("Los fondos ya fueron transferidos al prestador previamente.");
    }

    @Override
    public void dispute(EscrowContext context, String reason) {
        throw new IllegalStateException("No es posible disputar una transacción completada y liberada.");
    }

    @Override
    public void refund(EscrowContext context, String authorizationToken) {
        throw new IllegalStateException("Fondos ya entregados al prestador. Se requiere mediación especial.");
    }

    @Override
    public String getStateName() {
        return "COMPLETED_RELEASED";
    }
}

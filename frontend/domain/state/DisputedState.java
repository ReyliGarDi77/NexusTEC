package domain.state;

import data.model.EscrowTransaction;

/**
 * Estado de Mediación: Conflicto reportado entre estudiantes.
 * Congela los créditos hasta la intervención del comité arbitral o acuerdo mutuo.
 */
public class DisputedState implements EscrowState {

    @Override
    public void deposit(EscrowContext context, double amountCredits) {
        throw new IllegalStateException("Transacción en disputa; fondos congelados.");
    }

    @Override
    public void lockInHold(EscrowContext context) {
        throw new IllegalStateException("Los fondos ya están congelados por causa de disputa.");
    }

    @Override
    public void releaseCredits(EscrowContext context, String studentConfirmationSecret) {
        context.getTransaction().setStatus(EscrowTransaction.TransactionStatus.RELEASED);
        context.logTransition("Disputa resuelta a favor del prestador. Créditos liberados.");
        context.setState(new CompletedReleasedState());
    }

    @Override
    public void dispute(EscrowContext context, String reason) {
        context.logTransition("Actualización de argumentos de la disputa: " + reason);
    }

    @Override
    public void refund(EscrowContext context, String authorizationToken) {
        context.getTransaction().setStatus(EscrowTransaction.TransactionStatus.REFUNDED);
        context.logTransition("Disputa resuelta a favor del solicitante. Reembolso efectuado con token: " + authorizationToken);
        context.setState(new RefundedState());
    }

    @Override
    public String getStateName() {
        return "DISPUTED";
    }
}

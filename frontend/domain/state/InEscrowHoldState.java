package domain.state;

import data.model.EscrowTransaction;

import java.util.Date;

/**
 * Estado Activo: Los Créditos Halcón están retenidos de manera segura bajo custodia inteligente (Escrow).
 */
public class InEscrowHoldState implements EscrowState {

    @Override
    public void deposit(EscrowContext context, double amountCredits) {
        throw new IllegalStateException("Los fondos ya se encuentran bajo custodia (Hold).");
    }

    @Override
    public void lockInHold(EscrowContext context) {
        context.logTransition("Créditos asegurados y confirmados bajo custodia.");
    }

    @Override
    public void releaseCredits(EscrowContext context, String studentConfirmationSecret) {
        if (studentConfirmationSecret == null || studentConfirmationSecret.trim().isEmpty()) {
            throw new IllegalArgumentException("Se requiere el código de confirmación del estudiante solicitante.");
        }
        context.getTransaction().setStatus(EscrowTransaction.TransactionStatus.RELEASED);
        context.getTransaction().setReleasedAt(new Date());
        context.logTransition("Servicio completado satisfactoriamente. Créditos liberados al prestador.");
        context.setState(new CompletedReleasedState());
    }

    @Override
    public void dispute(EscrowContext context, String reason) {
        context.getTransaction().setStatus(EscrowTransaction.TransactionStatus.DISPUTED);
        context.getTransaction().setDisputeReason(reason);
        context.logTransition("Disputa iniciada. Motivo: " + reason);
        context.setState(new DisputedState());
    }

    @Override
    public void refund(EscrowContext context, String authorizationToken) {
        context.getTransaction().setStatus(EscrowTransaction.TransactionStatus.REFUNDED);
        context.logTransition("Reembolso directo ejecutado bajo autorización: " + authorizationToken);
        context.setState(new RefundedState());
    }

    @Override
    public String getStateName() {
        return "IN_ESCROW_HOLD";
    }
}

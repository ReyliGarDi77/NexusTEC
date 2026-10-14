package domain.state;

import data.model.EscrowTransaction;

/**
 * Estado Inicial: El pago está pendiente de depósito de Créditos Halcón por parte del solicitante.
 */
public class PendingDepositState implements EscrowState {

    @Override
    public void deposit(EscrowContext context, double amountCredits) {
        if (amountCredits <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor a cero.");
        }
        context.getTransaction().setCreditsAmount(amountCredits);
        context.getTransaction().setStatus(EscrowTransaction.TransactionStatus.IN_ESCROW_HOLD);
        context.logTransition("Depósito exitoso de " + amountCredits + " Créditos Halcón.");
        context.setState(new InEscrowHoldState());
    }

    @Override
    public void lockInHold(EscrowContext context) {
        throw new IllegalStateException("No se pueden bloquear fondos sin haber realizado el depósito previo.");
    }

    @Override
    public void releaseCredits(EscrowContext context, String studentConfirmationSecret) {
        throw new IllegalStateException("No se pueden liberar fondos en estado pendiente de depósito.");
    }

    @Override
    public void dispute(EscrowContext context, String reason) {
        throw new IllegalStateException("No se puede iniciar una disputa antes del depósito.");
    }

    @Override
    public void refund(EscrowContext context, String authorizationToken) {
        context.getTransaction().setStatus(EscrowTransaction.TransactionStatus.REFUNDED);
        context.logTransition("Transacción cancelada antes del depósito de fondos.");
        context.setState(new RefundedState());
    }

    @Override
    public String getStateName() {
        return "PENDING_DEPOSIT";
    }
}


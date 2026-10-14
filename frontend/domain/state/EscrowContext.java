package domain.state;

import data.model.EscrowTransaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Contexto del patrón State que administra la transición y ejecución del ciclo de vida
 * de las transacciones bajo custodia (Escrow) de Créditos Halcón.
 */
public class EscrowContext {
    private EscrowState currentState;
    private final EscrowTransaction transaction;
    private final List<String> auditLog;

    public EscrowContext(EscrowTransaction transaction) {
        this.transaction = transaction;
        this.auditLog = new ArrayList<>();
        // Estado inicial por defecto
        this.currentState = new PendingDepositState();
        logTransition("Inicializado en estado: " + currentState.getStateName());
    }

    public void setState(EscrowState state) {
        String prev = this.currentState != null ? this.currentState.getStateName() : "NINGUNO";
        this.currentState = state;
        logTransition("Transición de estado: [" + prev + "] -> [" + state.getStateName() + "]");
    }

    public EscrowState getCurrentState() {
        return currentState;
    }

    public EscrowTransaction getTransaction() {
        return transaction;
    }

    public void deposit(double amountCredits) {
        currentState.deposit(this, amountCredits);
    }

    public void lockInHold() {
        currentState.lockInHold(this);
    }

    public void releaseCredits(String studentConfirmationSecret) {
        currentState.releaseCredits(this, studentConfirmationSecret);
    }

    public void dispute(String reason) {
        currentState.dispute(this, reason);
    }

    public void refund(String authorizationToken) {
        currentState.refund(this, authorizationToken);
    }

    public void logTransition(String message) {
        String entry = "[" + new Date() + "] " + message;
        auditLog.add(entry);
    }

    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }
}


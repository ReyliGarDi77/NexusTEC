package viewmodel;

import data.model.EscrowTransaction;
import data.repository.EscrowRepository;
import domain.state.EscrowContext;
import domain.usecase.ProcessEscrowPaymentUseCase;

import java.util.List;

/**
 * ViewModel del módulo Escrow (Custodia de Créditos Halcón).
 * Expone el estado reactivo de las transacciones, saldo de créditos y operaciones de liberación/disputa.
 */
public class EscrowViewModel extends BaseViewModel {
    private final EscrowRepository repository;
    private final ProcessEscrowPaymentUseCase escrowUseCase;

    private final ObservableValue<Double> walletBalance = new ObservableValue<>(0.0);
    private final ObservableValue<EscrowContext> activeEscrowContext = new ObservableValue<>(null);
    private final ObservableValue<List<EscrowTransaction>> transactionHistory = new ObservableValue<>(null);

    public EscrowViewModel() {
        this.repository = EscrowRepository.getInstance();
        this.escrowUseCase = new ProcessEscrowPaymentUseCase(repository);
    }

    public EscrowViewModel(EscrowRepository repository, ProcessEscrowPaymentUseCase escrowUseCase) {
        this.repository = repository;
        this.escrowUseCase = escrowUseCase;
    }

    public ObservableValue<Double> getWalletBalance() {
        return walletBalance;
    }

    public ObservableValue<EscrowContext> getActiveEscrowContext() {
        return activeEscrowContext;
    }

    public ObservableValue<List<EscrowTransaction>> getTransactionHistory() {
        return transactionHistory;
    }

    public void loadBalance(String studentId) {
        setLoading(true);
        try {
            double balance = repository.getWalletBalance(studentId);
            walletBalance.setValue(balance);
            transactionHistory.setValue(repository.getAllCachedTransactions());
        } catch (Exception e) {
            setError("Error consultando saldo de Créditos Halcón: " + e.getMessage());
        } finally {
            setLoading(false);
        }
    }

    public void startEscrowHold(String swapId, String payerId, String payeeId, double amountCredits) {
        setLoading(true);
        try {
            EscrowContext context = escrowUseCase.initiateEscrow(swapId, payerId, payeeId, amountCredits);
            activeEscrowContext.setValue(context);
            setSuccess("Créditos retenidos con éxito bajo custodia inteligente Escrow.");
            loadBalance(payerId);
        } catch (Exception e) {
            setError(e.getMessage());
        } finally {
            setLoading(false);
        }
    }

    public void releaseCredits(String confirmationSecret) {
        EscrowContext context = activeEscrowContext.getValue();
        if (context == null) {
            setError("No existe una transacción de Escrow activa para liberar.");
            return;
        }

        setLoading(true);
        try {
            escrowUseCase.confirmAndRelease(context, confirmationSecret);
            activeEscrowContext.setValue(context); // Notifica actualización de estado
            setSuccess("¡Créditos Halcón liberados satisfactoriamente al prestador!");
        } catch (Exception e) {
            setError("Error al liberar créditos: " + e.getMessage());
        } finally {
            setLoading(false);
        }
    }

    public void raiseDispute(String reason) {
        EscrowContext context = activeEscrowContext.getValue();
        if (context == null) {
            setError("No hay transacción activa sujeta a disputa.");
            return;
        }

        setLoading(true);
        try {
            escrowUseCase.dispute(context, reason);
            activeEscrowContext.setValue(context);
            setSuccess("Disputa registrada. La transacción ha sido congelada para mediación.");
        } catch (Exception e) {
            setError("Error al registrar disputa: " + e.getMessage());
        } finally {
            setLoading(false);
        }
    }
}


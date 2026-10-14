package data.repository;

import data.model.EscrowTransaction;
import data.model.HalconUser;
import data.remote.ApiResponse;
import data.remote.NexusApiService;
import data.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Repositorio de datos para el módulo Escrow (Créditos Halcón).
 * Gestiona el ciclo de vida de los pagos custodiados y saldos de billetera estudiantil.
 */
public class EscrowRepository {
    private static volatile EscrowRepository instance;
    private final NexusApiService apiService;
    private final Map<String, EscrowTransaction> transactionCache = new ConcurrentHashMap<>();

    public EscrowRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public static EscrowRepository getInstance() {
        if (instance == null) {
            synchronized (EscrowRepository.class) {
                if (instance == null) {
                    instance = new EscrowRepository();
                }
            }
        }
        return instance;
    }

    public EscrowTransaction createEscrowHold(String swapId, String payerId, String payeeId, double creditsAmount) {
        EscrowTransaction transaction = new EscrowTransaction(
                "escrow_" + System.currentTimeMillis(),
                swapId,
                payerId,
                payeeId,
                creditsAmount,
                EscrowTransaction.TransactionStatus.IN_ESCROW_HOLD
        );
        transactionCache.put(transaction.getTransactionId(), transaction);

        if (apiService != null) {
            ApiResponse<EscrowTransaction> response = apiService.createEscrowHold(swapId, payerId, payeeId, creditsAmount);
            if (response != null && response.isSuccess() && response.getData() != null) {
                transactionCache.put(response.getData().getTransactionId(), response.getData());
                return response.getData();
            }
        }
        return transaction;
    }

    public EscrowTransaction getTransaction(String transactionId) {
        if (transactionCache.containsKey(transactionId)) {
            return transactionCache.get(transactionId);
        }
        if (apiService != null) {
            ApiResponse<EscrowTransaction> response = apiService.getEscrowTransaction(transactionId);
            if (response != null && response.isSuccess() && response.getData() != null) {
                transactionCache.put(transactionId, response.getData());
                return response.getData();
            }
        }
        return null;
    }

    public boolean releaseCredits(String transactionId, String secretConfirmation) {
        EscrowTransaction transaction = transactionCache.get(transactionId);
        if (transaction != null) {
            transaction.setStatus(EscrowTransaction.TransactionStatus.RELEASED);
            if (apiService != null) {
                apiService.releaseEscrowCredits(transactionId, secretConfirmation);
            }
            return true;
        }
        return false;
    }

    public boolean disputeTransaction(String transactionId, String reason) {
        EscrowTransaction transaction = transactionCache.get(transactionId);
        if (transaction != null) {
            transaction.setStatus(EscrowTransaction.TransactionStatus.DISPUTED);
            transaction.setDisputeReason(reason);
            if (apiService != null) {
                apiService.disputeEscrow(transactionId, reason);
            }
            return true;
        }
        return false;
    }

    public double getWalletBalance(String studentId) {
        if (apiService != null) {
            ApiResponse<HalconUser> response = apiService.getHalconWalletBalance(studentId);
            if (response != null && response.isSuccess() && response.getData() != null) {
                return response.getData().getHalconCreditsBalance();
            }
        }
        return 100.0; // Saldo por defecto de cortesía institucional Halcón
    }

    public List<EscrowTransaction> getAllCachedTransactions() {
        return new ArrayList<>(transactionCache.values());
    }
}


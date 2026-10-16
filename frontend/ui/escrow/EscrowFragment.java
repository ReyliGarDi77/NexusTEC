package ui.escrow;

import data.model.EscrowTransaction;
import domain.state.EscrowContext;
import ui.base.BaseFragment;
import viewmodel.EscrowViewModel;

/**
 * Fragment visual para el módulo Escrow (Billetera y Custodia Inteligente de Créditos Halcón).
 * Implementa visualización de saldo de alta gama, transiciones de estado y verificación criptográfica.
 */
public class EscrowFragment extends BaseFragment {
    private final EscrowViewModel viewModel;
    private EscrowAdapter adapter;

    public EscrowFragment(EscrowViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public String getFragmentTitle() {
        return "Billetera & Custodia Escrow";
    }

    @Override
    protected void initViews() {
        adapter = new EscrowAdapter();
        // Cargar saldo de estudiante Halcón ITTOL
        viewModel.loadBalance("student_ittol_01");
    }

    @Override
    protected void setupListeners() {
        adapter.setOnItemClickListener((transaction, position) -> {
            performHapticFeedback(HapticType.LIGHT_IMPACT);
            System.out.println("💳 [EscrowFragment] Transacción seleccionada: " + transaction.getTransactionId() + " (" + transaction.getStatus() + ")");
        });
    }

    @Override
    protected void observeViewModel() {
        viewModel.getWalletBalance().observe(balance -> {
            System.out.println("💰 [EscrowFragment] Saldo disponible renderizado: " + balance + " Créditos Halcón");
        });

        viewModel.getActiveEscrowContext().observe(context -> {
            if (context != null) {
                performHapticFeedback(HapticType.MEDIUM_IMPACT);
                System.out.println("🔄 [EscrowFragment UI] Transición visual de estado ejecutada: " + context.getCurrentState().getStateName());
                System.out.println("   -> Bitácora de Auditoría: " + context.getAuditLog());
            }
        });

        viewModel.getTransactionHistory().observe(transactions -> {
            adapter.updateList(transactions);
            System.out.println("📜 [EscrowFragment] Historial de transacciones actualizado: " + (transactions != null ? transactions.size() : 0) + " registros.");
        });

        viewModel.getSuccessMessage().observe(msg -> {
            if (msg != null) {
                performHapticFeedback(HapticType.SUCCESS_NOTIFICATION);
                System.out.println("✅ [EscrowFragment] " + msg);
            }
        });

        viewModel.getErrorMessage().observe(err -> {
            if (err != null) {
                performHapticFeedback(HapticType.WARNING_ALERT);
                System.err.println("⚠️ [EscrowFragment] " + err);
            }
        });
    }

    public void onDepositClicked(String swapId, String payerId, String payeeId, double amount) {
        performHapticFeedback(HapticType.MEDIUM_IMPACT);
        System.out.println("🔒 [EscrowFragment] Reteniendo " + amount + " CH en custodia para swap: " + swapId);
        viewModel.startEscrowHold(swapId, payerId, payeeId, amount);
    }

    public void onReleaseClicked(String confirmationCode) {
        performHapticFeedback(HapticType.SUCCESS_NOTIFICATION);
        System.out.println("🔓 [EscrowFragment] Confirmando y liberando créditos al prestador...");
        viewModel.releaseCredits(confirmationCode);
    }

    public void onDisputeClicked(String reason) {
        performHapticFeedback(HapticType.WARNING_ALERT);
        System.out.println("⚖️ [EscrowFragment] Iniciando proceso de mediación arbitral en campus ITTOL...");
        viewModel.raiseDispute(reason);
    }

    public EscrowAdapter getAdapter() {
        return adapter;
    }
}

package ui.escrow;

import data.model.EscrowTransaction;
import domain.state.EscrowContext;
import ui.base.BaseFragment;
import viewmodel.EscrowViewModel;

/**
 * Fragment para visualizar saldo de Créditos Halcón, retenciones activas y liberar pagos.
 */
public class EscrowFragment extends BaseFragment {
    private final EscrowViewModel viewModel;
    private EscrowAdapter adapter;

    public EscrowFragment(EscrowViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void initViews() {
        adapter = new EscrowAdapter();
    }

    @Override
    protected void setupListeners() {
        adapter.setOnItemClickListener((transaction, position) -> {
            System.out.println("💳 [EscrowFragment] Transacción seleccionada: " + transaction.getTransactionId());
        });
    }

    @Override
    protected void observeViewModel() {
        viewModel.getActiveEscrowContext().observe(context -> {
            if (context != null) {
                System.out.println("🔄 [EscrowFragment] Estado de custodia actualizado a: " + context.getCurrentState().getStateName());
            }
        });

        viewModel.getTransactionHistory().observe(transactions -> {
            adapter.updateList(transactions);
        });
    }

    public void onDepositClicked(String swapId, String payerId, String payeeId, double amount) {
        viewModel.startEscrowHold(swapId, payerId, payeeId, amount);
    }

    public void onReleaseClicked(String confirmationCode) {
        viewModel.releaseCredits(confirmationCode);
    }

    public void onDisputeClicked(String reason) {
        viewModel.raiseDispute(reason);
    }

    public EscrowAdapter getAdapter() {
        return adapter;
    }
}

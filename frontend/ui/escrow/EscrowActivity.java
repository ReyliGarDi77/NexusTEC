package ui.escrow;

import ui.base.BaseActivity;
import viewmodel.EscrowViewModel;

/**
 * Activity para el módulo Escrow (Créditos Halcón).
 * Gestiona la interfaz del monedero digital y la custodia de transacciones.
 */
public class EscrowActivity extends BaseActivity {
    private EscrowViewModel viewModel;
    private EscrowFragment fragment;

    @Override
    protected void initViews() {
        viewModel = new EscrowViewModel();
        fragment = new EscrowFragment(viewModel);
        fragment.onCreateView();
    }

    @Override
    protected void setupListeners() {
    }

    @Override
    protected void observeViewModel() {
        viewModel.getWalletBalance().observe(balance -> {
            System.out.println("💰 [EscrowActivity] Balance actual: " + balance + " Créditos Halcón");
        });

        viewModel.getErrorMessage().observe(err -> {
            if (err != null) System.err.println("⚠️ [EscrowActivity] Error: " + err);
        });

        viewModel.getSuccessMessage().observe(msg -> {
            if (msg != null) System.out.println("✅ [EscrowActivity] Éxito: " + msg);
        });
    }

    public EscrowViewModel getViewModel() {
        return viewModel;
    }
}


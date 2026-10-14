package ui.panicbutton;

import ui.base.BaseActivity;
import viewmodel.PanicButtonViewModel;

/**
 * Activity para el módulo de Botón de Pánico.
 * Brinda acceso directo y de alta prioridad para activación de alarmas de emergencia en campus.
 */
public class PanicButtonActivity extends BaseActivity {
    private PanicButtonViewModel viewModel;
    private PanicButtonFragment fragment;

    @Override
    protected void initViews() {
        viewModel = new PanicButtonViewModel();
        fragment = new PanicButtonFragment(viewModel);
        fragment.onCreateView();
    }

    @Override
    protected void setupListeners() {
    }

    @Override
    protected void observeViewModel() {
        viewModel.getCurrentAlert().observe(alert -> {
            if (alert != null) {
                System.out.println("🚨 [PanicButtonActivity] ALERTA ACTIVA: " + alert.getAlertId() + " en " + alert.getCampusBuildingZone());
            }
        });

        viewModel.getIsSilentMode().observe(silent -> {
            System.out.println("🔕 [PanicButtonActivity] Modo Silencioso: " + (Boolean.TRUE.equals(silent) ? "ACTIVADO" : "DESACTIVADO"));
        });

        viewModel.getSuccessMessage().observe(msg -> {
            if (msg != null) System.out.println("ℹ️ [PanicButtonActivity] " + msg);
        });
    }

    public PanicButtonViewModel getViewModel() {
        return viewModel;
    }
}


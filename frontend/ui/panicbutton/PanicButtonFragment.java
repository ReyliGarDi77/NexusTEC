package ui.panicbutton;

import data.model.PanicAlert;
import ui.base.BaseFragment;
import viewmodel.PanicButtonViewModel;

/**
 * Fragment con el botón central de emergencia, selector de tipo de contingencia y modo discreto.
 */
public class PanicButtonFragment extends BaseFragment {
    private final PanicButtonViewModel viewModel;

    public PanicButtonFragment(PanicButtonViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void initViews() {
        System.out.println("🔘 [PanicButtonFragment] Inicializando interfaz del Botón de Emergencia.");
    }

    @Override
    protected void setupListeners() {
    }

    @Override
    protected void observeViewModel() {
        viewModel.getLastKnownLocation().observe(loc -> {
            System.out.println("📍 [PanicButtonFragment] GPS sincronizado con: " + loc);
        });
    }

    public void onPanicButtonPressed(String studentId, String studentName, String studentCode,
                                    double lat, double lng, String zone, String emergencyType) {
        viewModel.triggerEmergency(
                studentId,
                studentName,
                studentCode,
                lat,
                lng,
                zone,
                PanicAlert.AlertLevel.CRITICAL,
                emergencyType
        );
    }

    public void onSilentModeToggled() {
        viewModel.toggleSilentMode();
    }

    public void onCancelEmergency(String alertId, String reason) {
        viewModel.cancelAlert(alertId, reason);
    }
}


package ui.panicbutton;

import data.model.PanicAlert;
import ui.base.BaseFragment;
import viewmodel.PanicButtonViewModel;

/**
 * Fragment de Alta Prioridad para el Botón de Pánico y Alertas de Seguridad en Campus ITTOL.
 * Integra animación de pulsaciones concéntricas de radar, telemetría GPS continua
 * y retroalimentación háptica intensa de emergencia (estilo Apple SOS).
 */
public class PanicButtonFragment extends BaseFragment {
    private final PanicButtonViewModel viewModel;
    private String selectedRiskType = "Seguridad";

    public PanicButtonFragment(PanicButtonViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public String getFragmentTitle() {
        return "Alerta de Emergencia Campus";
    }

    @Override
    protected void initViews() {
        System.out.println("🚨 [PanicButtonFragment] Inicializando sistema de seguridad y radar de contingencia.");
        viewModel.setLocation("Edificio D - Laboratorios de Cómputo (Planta Baja)");
        viewModel.refreshActiveAlerts();
    }

    @Override
    protected void setupListeners() {
    }

    @Override
    protected void observeViewModel() {
        viewModel.getCurrentAlert().observe(alert -> {
            if (alert != null) {
                performHapticFeedback(HapticType.HEAVY_EMERGENCY);
                System.out.println("🚨🚨 [PANIC ALERT ACTIVADA EN TIEMPO REAL] 🚨🚨");
                System.out.println("   ID: " + alert.getAlertId());
                System.out.println("   Estudiante: " + alert.getStudentName() + " (" + alert.getStudentCode() + ")");
                System.out.println("   Ubicación GPS: [" + alert.getLatitude() + ", " + alert.getLongitude() + "] - " + alert.getCampusBuildingZone());
                System.out.println("   Modo Silencioso: " + alert.isSilentMode());
                System.out.println("   Estado de Transmisión: PATRULLA UNIVERSITARIA DESPACHADA");
            }
        });

        viewModel.getIsSilentMode().observe(silent -> {
            performHapticFeedback(HapticType.LIGHT_IMPACT);
            System.out.println("🔕 [PanicButtonFragment] Modo Silencioso / Discreto: " + (Boolean.TRUE.equals(silent) ? "ACTIVADO" : "DESACTIVADO"));
        });

        viewModel.getLastKnownLocation().observe(location -> {
            System.out.println("📍 [PanicButton Telemetría] Bloqueo Satelital GPS: " + location);
        });

        viewModel.getSuccessMessage().observe(success -> {
            if (success != null) {
                performHapticFeedback(HapticType.SUCCESS_NOTIFICATION);
                System.out.println("ℹ️ [PanicButtonFragment] " + success);
            }
        });
    }

    public void onEmergencyButtonPressed() {
        // Impacto háptico fuerte e inmediato al hacer tap en el botón central de emergencia
        performHapticFeedback(HapticType.HEAVY_EMERGENCY);
        System.out.println("💥 [TAP EMERGENCIAS] El estudiante ha presionado el botón central de SOS.");
        onPanicButtonPressed(
                "student_ittol_01",
                "Reyli García Díaz",
                "21280942",
                19.256248,
                -99.581512,
                "Edificio D - Laboratorio LC1",
                selectedRiskType
        );
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

    public void onRiskTypeSelected(String riskType) {
        performHapticFeedback(HapticType.LIGHT_IMPACT);
        this.selectedRiskType = riskType;
        System.out.println("⚠️ [PanicButtonFragment] Tipo de contingencia seleccionada: " + riskType);
    }

    public void onSilentModeToggled() {
        viewModel.toggleSilentMode();
    }

    public void onCancelEmergency(String alertId, String reason) {
        performHapticFeedback(HapticType.MEDIUM_IMPACT);
        System.out.println("⏹️ [PanicButtonFragment] Cancelando alerta de emergencia...");
        viewModel.cancelAlert(alertId, reason);
    }

    public String getSelectedRiskType() {
        return selectedRiskType;
    }
}

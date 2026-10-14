package viewmodel;

import data.model.PanicAlert;
import data.repository.PanicAlertRepository;
import domain.observer.CampusHealthServiceObserver;
import domain.observer.CampusSecurityStationObserver;
import domain.observer.EmergencyNotificationCenterObserver;
import domain.observer.PanicAlertManager;
import domain.usecase.TriggerPanicAlertUseCase;

import java.util.List;

/**
 * ViewModel del módulo Botón de Pánico (Seguridad y Emergencias en Campus).
 * Administra el estado de activación, modo silencioso, geolocalización y despacho de alertas.
 */
public class PanicButtonViewModel extends BaseViewModel {
    private final PanicAlertRepository repository;
    private final TriggerPanicAlertUseCase triggerUseCase;
    private final PanicAlertManager alertManager;

    private final ObservableValue<PanicAlert> currentAlert = new ObservableValue<>(null);
    private final ObservableValue<Boolean> isSilentMode = new ObservableValue<>(false);
    private final ObservableValue<String> lastKnownLocation = new ObservableValue<>("Campus Central - Zona A");
    private final ObservableValue<List<PanicAlert>> activeAlertsList = new ObservableValue<>(null);

    public PanicButtonViewModel() {
        this.repository = PanicAlertRepository.getInstance();
        this.alertManager = PanicAlertManager.getInstance();
        this.triggerUseCase = new TriggerPanicAlertUseCase(repository, alertManager);
        setupDefaultObservers();
    }

    public PanicButtonViewModel(PanicAlertRepository repository, TriggerPanicAlertUseCase triggerUseCase, PanicAlertManager alertManager) {
        this.repository = repository;
        this.alertManager = alertManager;
        this.triggerUseCase = triggerUseCase;
        setupDefaultObservers();
    }

    private void setupDefaultObservers() {
        // Registro de los observadores del campus si no han sido registrados
        if (alertManager.getRegisteredObserverCount() == 0) {
            alertManager.registerObserver(new CampusSecurityStationObserver("01-Principal"));
            alertManager.registerObserver(new EmergencyNotificationCenterObserver(true, true));
            alertManager.registerObserver(new CampusHealthServiceObserver());
        }
    }

    public ObservableValue<PanicAlert> getCurrentAlert() {
        return currentAlert;
    }

    public ObservableValue<Boolean> getIsSilentMode() {
        return isSilentMode;
    }

    public ObservableValue<String> getLastKnownLocation() {
        return lastKnownLocation;
    }

    public ObservableValue<List<PanicAlert>> getActiveAlertsList() {
        return activeAlertsList;
    }

    public void toggleSilentMode() {
        boolean current = Boolean.TRUE.equals(isSilentMode.getValue());
        isSilentMode.setValue(!current);
    }

    public void setLocation(String location) {
        lastKnownLocation.setValue(location);
    }

    public void triggerEmergency(String studentId, String studentName, String studentCode,
                                 double latitude, double longitude, String campusZone,
                                 PanicAlert.AlertLevel level, String emergencyType) {
        setLoading(true);
        try {
            PanicAlert alert = new PanicAlert(
                    "alert_" + System.currentTimeMillis(),
                    studentId,
                    studentName,
                    studentCode,
                    latitude,
                    longitude,
                    campusZone,
                    level,
                    emergencyType
            );
            alert.setSilentMode(Boolean.TRUE.equals(isSilentMode.getValue()));

            PanicAlert triggered = triggerUseCase.execute(alert);
            currentAlert.setValue(triggered);
            setSuccess("🚨 ¡Alerta de emergencia emitida y transmitida a las unidades de vigilancia!");
            refreshActiveAlerts();
        } catch (Exception e) {
            setError("Fallo al emitir alerta de pánico: " + e.getMessage());
        } finally {
            setLoading(false);
        }
    }

    public void cancelAlert(String alertId, String cancellationReason) {
        setLoading(true);
        try {
            boolean cancelled = repository.cancelAlert(alertId, cancellationReason);
            if (cancelled) {
                currentAlert.setValue(null);
                setSuccess("Alerta cancelada satisfactoriamente.");
                refreshActiveAlerts();
            }
        } catch (Exception e) {
            setError("Error al cancelar alerta: " + e.getMessage());
        } finally {
            setLoading(false);
        }
    }

    public void refreshActiveAlerts() {
        try {
            List<PanicAlert> list = repository.getActiveAlerts();
            activeAlertsList.setValue(list);
        } catch (Exception e) {
            setError("Error al listar alertas: " + e.getMessage());
        }
    }
}


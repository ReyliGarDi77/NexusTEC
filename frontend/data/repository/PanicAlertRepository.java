package data.repository;

import data.model.PanicAlert;
import data.remote.ApiResponse;
import data.remote.NexusApiService;
import data.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Repositorio de datos para el módulo de Botón de Pánico y Alertas de Emergencia en Campus.
 */
public class PanicAlertRepository {
    private static volatile PanicAlertRepository instance;
    private final NexusApiService apiService;
    private final Map<String, PanicAlert> activeAlertsCache = new ConcurrentHashMap<>();

    public PanicAlertRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public static PanicAlertRepository getInstance() {
        if (instance == null) {
            synchronized (PanicAlertRepository.class) {
                if (instance == null) {
                    instance = new PanicAlertRepository();
                }
            }
        }
        return instance;
    }

    public PanicAlert triggerAlert(PanicAlert alert) {
        if (alert == null) return null;
        if (alert.getAlertId() == null || alert.getAlertId().isEmpty()) {
            alert.setAlertId("alert_" + System.currentTimeMillis());
        }
        activeAlertsCache.put(alert.getAlertId(), alert);

        if (apiService != null) {
            ApiResponse<PanicAlert> response = apiService.triggerPanicAlert(alert);
            if (response != null && response.isSuccess() && response.getData() != null) {
                activeAlertsCache.put(response.getData().getAlertId(), response.getData());
                return response.getData();
            }
        }
        return alert;
    }

    public boolean cancelAlert(String alertId, String reason) {
        PanicAlert alert = activeAlertsCache.get(alertId);
        if (alert != null) {
            alert.setStatus(PanicAlert.AlertStatus.FALSE_ALARM);
            alert.setNotes(reason);
            if (apiService != null) {
                apiService.cancelPanicAlert(alertId, reason);
            }
            return true;
        }
        return false;
    }

    public List<PanicAlert> getActiveAlerts() {
        if (apiService != null) {
            ApiResponse<List<PanicAlert>> response = apiService.getActiveCampusAlerts();
            if (response != null && response.isSuccess() && response.getData() != null) {
                for (PanicAlert a : response.getData()) {
                    activeAlertsCache.put(a.getAlertId(), a);
                }
                return response.getData();
            }
        }
        return new ArrayList<>(activeAlertsCache.values());
    }

    public PanicAlert getAlertById(String alertId) {
        return activeAlertsCache.get(alertId);
    }
}

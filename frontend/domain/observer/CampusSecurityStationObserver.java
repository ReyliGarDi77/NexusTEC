package domain.observer;

import data.model.PanicAlert;

import java.util.ArrayList;
import java.util.List;

/**
 * Observador Concreto: Caseta Central de Seguridad y Vigilancia del Campus Halcón.
 * Procesa coordenadas GPS, despliega patrullas universitarias y monitorea cámaras de la zona.
 */
public class CampusSecurityStationObserver implements PanicAlertObserver {
    private final String stationId;
    private final List<PanicAlert> dispatchQueue;

    public CampusSecurityStationObserver(String stationId) {
        this.stationId = stationId;
        this.dispatchQueue = new ArrayList<>();
    }

    @Override
    public void onPanicAlertReceived(PanicAlert alert) {
        dispatchQueue.add(alert);
        System.out.println("🚨 [" + getObserverName() + "] ALERTA RECIBIDA!");
        System.out.println("   Estudiante: " + alert.getStudentName() + " (" + alert.getStudentCode() + ")");
        System.out.println("   Ubicación GPS: [" + alert.getLatitude() + ", " + alert.getLongitude() + "] - Zona: " + alert.getCampusBuildingZone());
        System.out.println("   Nivel de Riesgo: " + alert.getLevel() + " | Modo Silencioso: " + alert.isSilentMode());
        System.out.println("   -> Despachando patrulla de vigilancia inmediata al sector...");
    }

    @Override
    public void onPanicAlertStatusChanged(PanicAlert alert) {
        System.out.println("ℹ️ [" + getObserverName() + "] Estado de alerta actualizado para " + alert.getAlertId() + ": " + alert.getStatus());
    }

    @Override
    public String getObserverName() {
        return "Caseta de Seguridad Universitaria #" + stationId;
    }

    public List<PanicAlert> getDispatchQueue() {
        return new ArrayList<>(dispatchQueue);
    }
}


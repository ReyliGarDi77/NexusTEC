package domain.observer;

import data.model.PanicAlert;

/**
 * Observador Concreto: Servicio Médico y Primeros Auxilios del Campus.
 * Prepara botiquines y unidades médicas cuando la emergencia es de tipo médica o de riesgo crítico.
 */
public class CampusHealthServiceObserver implements PanicAlertObserver {

    @Override
    public void onPanicAlertReceived(PanicAlert alert) {
        if ("MEDICA".equalsIgnoreCase(alert.getEmergencyType()) || 
            alert.getLevel() == PanicAlert.AlertLevel.CRITICAL) {
            System.out.println("🚑 [" + getObserverName() + "] PROTOCOLO MÉDICO ACTIVADO!");
            System.out.println("   -> Ambulancia/paramédicos de guardia en alerta hacia: " + alert.getCampusBuildingZone());
        }
    }

    @Override
    public void onPanicAlertStatusChanged(PanicAlert alert) {
        // Monitoreo pasivo
    }

    @Override
    public String getObserverName() {
        return "Servicio Médico y Primeros Auxilios";
    }
}

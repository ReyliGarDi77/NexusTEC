package domain.observer;

import data.model.PanicAlert;

/**
 * Observador Concreto: Centro de Notificaciones de Emergencia Institucional.
 * Emite notificaciones push prioritarias, correos institucionales y mensajes SMS a contactos de emergencia.
 */
public class EmergencyNotificationCenterObserver implements PanicAlertObserver {
    private final boolean pushEnabled;
    private final boolean smsEnabled;

    public EmergencyNotificationCenterObserver(boolean pushEnabled, boolean smsEnabled) {
        this.pushEnabled = pushEnabled;
        this.smsEnabled = smsEnabled;
    }

    @Override
    public void onPanicAlertReceived(PanicAlert alert) {
        System.out.println("📢 [" + getObserverName() + "] Difusión de Alerta de Emergencia:");
        if (pushEnabled) {
            System.out.println("   -> [PUSH] Enviando notificación instantánea al equipo directivo y prefectura.");
        }
        if (smsEnabled) {
            System.out.println("   -> [SMS] Notificando a los contactos de emergencia del estudiante " + alert.getStudentName());
        }
    }

    @Override
    public void onPanicAlertStatusChanged(PanicAlert alert) {
        System.out.println("📢 [" + getObserverName() + "] Notificación de cambio de estado: " + alert.getStatus());
    }

    @Override
    public String getObserverName() {
        return "Centro de Notificaciones y Enlace Institucional";
    }
}

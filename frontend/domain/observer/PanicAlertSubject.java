package domain.observer;

import data.model.PanicAlert;

/**
 * Interfaz Subject (Sujeto / Emisor) del patrón Observer para alertas de emergencia.
 */
public interface PanicAlertSubject {

    /**
     * Registra un nuevo observador en la lista de escuchas.
     */
    void registerObserver(PanicAlertObserver observer);

    /**
     * Remueve un observador previamente registrado.
     */
    void removeObserver(PanicAlertObserver observer);

    /**
     * Notifica a todos los observadores que una nueva alerta ha sido activada.
     */
    void notifyAlertTriggered(PanicAlert alert);

    /**
     * Notifica a todos los observadores sobre la actualización de estado de una alerta.
     */
    void notifyAlertStatusUpdated(PanicAlert alert);
}

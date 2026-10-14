package domain.observer;

import data.model.PanicAlert;

/**
 * Interfaz Observer para receptores y escuchas de Alertas de Emergencia del Campus (Botón de Pánico).
 */
public interface PanicAlertObserver {

    /**
     * Invocado cuando un estudiante detona una alerta de pánico en tiempo real.
     */
    void onPanicAlertReceived(PanicAlert alert);

    /**
     * Invocado cuando el estado de una alerta activa cambia (ej: atendida, patrulla en camino, resuelta).
     */
    void onPanicAlertStatusChanged(PanicAlert alert);

    /**
     * Nombre descriptivo del observador / estación de escucha.
     */
    String getObserverName();
}


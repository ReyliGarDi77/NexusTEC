package domain.usecase;

import data.model.PanicAlert;
import data.repository.PanicAlertRepository;
import domain.observer.PanicAlertManager;

/**
 * Caso de Uso: Disparar la emisión de una Alerta de Emergencia desde el Botón de Pánico.
 * Emite la notificación a todos los observadores registrados (Caseta de Seguridad, Prefectura, Médicos)
 * y persiste el evento en el repositorio de auditoría.
 */
public class TriggerPanicAlertUseCase {
    private final PanicAlertRepository repository;
    private final PanicAlertManager alertManager;

    public TriggerPanicAlertUseCase(PanicAlertRepository repository, PanicAlertManager alertManager) {
        this.repository = repository != null ? repository : PanicAlertRepository.getInstance();
        this.alertManager = alertManager != null ? alertManager : PanicAlertManager.getInstance();
    }

    public PanicAlert execute(PanicAlert alert) {
        if (alert == null) {
            throw new IllegalArgumentException("La información de la alerta no puede ser nula.");
        }
        if (alert.getStudentId() == null || alert.getStudentId().isEmpty()) {
            throw new IllegalArgumentException("Se requiere el identificador del estudiante.");
        }

        // Persistir en repositorio
        PanicAlert savedAlert = repository.triggerAlert(alert);

        // Notificar en tiempo real a todos los observadores suscritos mediante el patrón Observer
        alertManager.notifyAlertTriggered(savedAlert);

        return savedAlert;
    }
}


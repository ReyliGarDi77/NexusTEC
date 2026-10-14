package domain.observer;

import data.model.PanicAlert;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gestor y despachador central del patrón Observer para alertas del Botón de Pánico.
 * Mantiene la lista de observadores activos (estaciones de seguridad, paramédicos, etc.)
 * y propaga los eventos de emergencia de manera segura en hilos concurrentes.
 */
public class PanicAlertManager implements PanicAlertSubject {
    private static volatile PanicAlertManager instance;
    private final List<PanicAlertObserver> observers = new CopyOnWriteArrayList<>();

    private PanicAlertManager() {
    }

    public static PanicAlertManager getInstance() {
        if (instance == null) {
            synchronized (PanicAlertManager.class) {
                if (instance == null) {
                    instance = new PanicAlertManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void registerObserver(PanicAlertObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(PanicAlertObserver observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyAlertTriggered(PanicAlert alert) {
        if (alert == null) return;
        for (PanicAlertObserver observer : observers) {
            try {
                observer.onPanicAlertReceived(alert);
            } catch (Exception e) {
                System.err.println("Error notificando al observador [" + observer.getObserverName() + "]: " + e.getMessage());
            }
        }
    }

    @Override
    public void notifyAlertStatusUpdated(PanicAlert alert) {
        if (alert == null) return;
        for (PanicAlertObserver observer : observers) {
            try {
                observer.onPanicAlertStatusChanged(alert);
            } catch (Exception e) {
                System.err.println("Error actualizando al observador [" + observer.getObserverName() + "]: " + e.getMessage());
            }
        }
    }

    public int getRegisteredObserverCount() {
        return observers.size();
    }

    public void clearAllObservers() {
        observers.clear();
    }
}


package viewmodel;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstracción de contenedor reactivo semejante a LiveData / StateFlow en Android.
 * Permite a la capa de UI observar cambios de estado en el ViewModel desacopladamente.
 *
 * @param <T> Tipo del valor observable.
 */
public class ObservableValue<T> {

    public interface Observer<T> {
        void onChanged(T value);
    }

    private T value;
    private final List<Observer<T>> observers = new CopyOnWriteArrayList<>();

    public ObservableValue() {
    }

    public ObservableValue(T initialValue) {
        this.value = initialValue;
    }

    public synchronized T getValue() {
        return value;
    }

    public synchronized void setValue(T newValue) {
        this.value = newValue;
        notifyObservers();
    }

    public synchronized void postValue(T newValue) {
        setValue(newValue);
    }

    public void observe(Observer<T> observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            if (value != null) {
                observer.onChanged(value);
            }
        }
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer<T> observer : observers) {
            try {
                observer.onChanged(value);
            } catch (Exception e) {
                System.err.println("Error en observer: " + e.getMessage());
            }
        }
    }
}


package ui.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity base que modela el ciclo de vida de componentes Android en Java.
 * Proporciona métodos de ciclo de vida estándar (onCreate, onStart, onResume, onPause, onDestroy)
 * e inicialización limpia de arquitectura MVVM.
 */
public abstract class BaseActivity {
    private boolean isDestroyed = false;
    private final Map<Integer, Object> viewRegistry = new HashMap<>();

    public void onCreate() {
        isDestroyed = false;
        initViews();
        setupListeners();
        observeViewModel();
    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onDestroy() {
        isDestroyed = true;
        viewRegistry.clear();
    }

    public boolean isActivityDestroyed() {
        return isDestroyed;
    }

    /**
     * Inicializa los componentes visuales de la vista.
     */
    protected abstract void initViews();

    /**
     * Configura escuchadores de eventos e interacciones de usuario.
     */
    protected abstract void setupListeners();

    /**
     * Conecta los observadores reactivos del ViewModel correspondiente.
     */
    protected abstract void observeViewModel();

    protected void registerView(int viewId, Object view) {
        viewRegistry.put(viewId, view);
    }

    @SuppressWarnings("unchecked")
    protected <T> T findViewById(int viewId) {
        return (T) viewRegistry.get(viewId);
    }
}


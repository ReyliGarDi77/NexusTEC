package ui.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Fragment base que representa una sección modular de la interfaz de usuario en Android.
 * Proporciona soporte para microinteracciones táctiles, retroalimentación háptica
 * y registro desacoplado de componentes visuales.
 */
public abstract class BaseFragment {
    private boolean isViewCreated = false;
    private final Map<Integer, Object> viewRegistry = new HashMap<>();

    public void onCreateView() {
        isViewCreated = true;
        initViews();
        setupListeners();
        observeViewModel();
    }

    public void onViewCreated() {
    }

    public void onDestroyView() {
        isViewCreated = false;
        viewRegistry.clear();
    }

    public boolean isViewCreated() {
        return isViewCreated;
    }

    protected abstract void initViews();

    protected abstract void setupListeners();

    protected abstract void observeViewModel();

    public abstract String getFragmentTitle();

    protected void registerView(int viewId, Object view) {
        viewRegistry.put(viewId, view);
    }

    @SuppressWarnings("unchecked")
    protected <T> T findViewById(int viewId) {
        return (T) viewRegistry.get(viewId);
    }

    /**
     * Emula retroalimentación háptica (vibración de impacto sutil estilo Apple Taptic Engine).
     */
    protected void performHapticFeedback(HapticType type) {
        System.out.println("📳 [HapticEngine] Retroalimentación háptica ejecutada: " + type);
    }

    public enum HapticType {
        LIGHT_IMPACT,
        MEDIUM_IMPACT,
        HEAVY_EMERGENCY,
        SUCCESS_NOTIFICATION,
        WARNING_ALERT
    }
}

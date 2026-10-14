package ui.base;

/**
 * Fragment base que representa una sección modular de la interfaz de usuario en Android.
 */
public abstract class BaseFragment {
    private boolean isViewCreated = false;

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
    }

    public boolean isViewCreated() {
        return isViewCreated;
    }

    protected abstract void initViews();

    protected abstract void setupListeners();

    protected abstract void observeViewModel();
}

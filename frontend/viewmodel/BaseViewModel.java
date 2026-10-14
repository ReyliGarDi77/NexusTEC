package viewmodel;

/**
 * Clase base para los ViewModels de la arquitectura MVVM en la app Nexus-Tec.
 * Centraliza la gestión de estados globales como indicadores de carga y mensajes de error.
 */
public abstract class BaseViewModel {
    protected final ObservableValue<Boolean> isLoading = new ObservableValue<>(false);
    protected final ObservableValue<String> errorMessage = new ObservableValue<>(null);
    protected final ObservableValue<String> successMessage = new ObservableValue<>(null);

    public ObservableValue<Boolean> getIsLoading() {
        return isLoading;
    }

    public ObservableValue<String> getErrorMessage() {
        return errorMessage;
    }

    public ObservableValue<String> getSuccessMessage() {
        return successMessage;
    }

    protected void setLoading(boolean loading) {
        isLoading.postValue(loading);
    }

    protected void setError(String message) {
        errorMessage.postValue(message);
    }

    protected void setSuccess(String message) {
        successMessage.postValue(message);
    }

    public void clearMessages() {
        errorMessage.postValue(null);
        successMessage.postValue(null);
    }

    /**
     * Limpieza de recursos al destruir la vista (análogo a onCleared en AndroidX ViewModel).
     */
    public void onCleared() {
        clearMessages();
    }
}

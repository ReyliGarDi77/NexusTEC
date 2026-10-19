package data.remote;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Cliente Singleton configurador de la instancia Retrofit / HTTP para la aplicación Nexus-Tec.
 * Gestiona el Base URL, interceptores de autenticación y la instancia del servicio API.
 */
public class RetrofitClient {
    private static final String DEFAULT_BASE_URL = "https://api.nexustec.edu.mx/api/v1/";
    private static volatile RetrofitClient instance;

    private String baseUrl;
    private String authToken;
    private int connectTimeoutSeconds = 30;
    private int readTimeoutSeconds = 30;
    private NexusApiService apiService;

    public enum ApiMode {
        LIVE_BACKEND_BRIDGE,
        MOCK_OFFLINE
    }

    private ApiMode currentMode = ApiMode.LIVE_BACKEND_BRIDGE;
    private BackendNexusApiService backendApiService;
    private MockNexusApiService mockApiService;

    // Cache interno de instancias de servicio
    private final Map<Class<?>, Object> serviceCache = new ConcurrentHashMap<>();

    private RetrofitClient() {
        this.baseUrl = DEFAULT_BASE_URL;
        initServices();
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    /**
     * Inicializa los servicios con enlace directo al backend modular de Nexus-Tec.
     */
    private void initServices() {
        this.backendApiService = new BackendNexusApiService();
        this.mockApiService = new MockNexusApiService();
        this.apiService = backendApiService;
    }

    public void setApiMode(ApiMode mode) {
        this.currentMode = mode;
        if (mode == ApiMode.LIVE_BACKEND_BRIDGE) {
            if (backendApiService == null) {
                backendApiService = new BackendNexusApiService();
            }
            this.apiService = backendApiService;
        } else {
            if (mockApiService == null) {
                mockApiService = new MockNexusApiService();
            }
            this.apiService = mockApiService;
        }
    }

    public ApiMode getCurrentMode() {
        return currentMode;
    }

    public BackendNexusApiService getBackendApiService() {
        return backendApiService;
    }

    public MockNexusApiService getMockApiService() {
        return mockApiService;
    }

    public NexusApiService getApiService() {
        return apiService;
    }

    public void setApiService(NexusApiService apiService) {
        this.apiService = apiService;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getConnectTimeoutSeconds() {
        return connectTimeoutSeconds;
    }

    public void setConnectTimeoutSeconds(int connectTimeoutSeconds) {
        this.connectTimeoutSeconds = connectTimeoutSeconds;
    }

    public int getReadTimeoutSeconds() {
        return readTimeoutSeconds;
    }

    public void setReadTimeoutSeconds(int readTimeoutSeconds) {
        this.readTimeoutSeconds = readTimeoutSeconds;
    }
}


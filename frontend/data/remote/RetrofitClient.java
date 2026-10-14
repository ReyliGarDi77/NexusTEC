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

    // Cache interno de instancias de servicio
    private final Map<Class<?>, Object> serviceCache = new ConcurrentHashMap<>();

    private RetrofitClient() {
        this.baseUrl = DEFAULT_BASE_URL;
        initMockService();
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
     * Inicializa la implementación cliente de la interfaz de servicios.
     */
    private void initMockService() {
        // En entorno Android de producción, aquí se inicializa:
        // Retrofit retrofit = new Retrofit.Builder()
        //         .baseUrl(baseUrl)
        //         .addConverterFactory(GsonConverterFactory.create())
        //         .client(okHttpClient)
        //         .build();
        // this.apiService = retrofit.create(NexusApiService.class);
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

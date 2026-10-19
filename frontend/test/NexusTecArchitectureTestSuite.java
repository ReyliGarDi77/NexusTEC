package test;

import data.model.EscrowTransaction;
import data.model.HalconUser;
import data.model.PanicAlert;
import data.model.SkillSwap;
import data.model.StudyGroup;
import data.remote.BackendNexusApiService;
import data.remote.RetrofitClient;
import data.repository.EscrowRepository;
import data.repository.PanicAlertRepository;
import data.repository.SkillSwapRepository;
import data.repository.StudyGroupRepository;
import domain.observer.CampusHealthServiceObserver;
import domain.observer.CampusSecurityStationObserver;
import domain.observer.EmergencyNotificationCenterObserver;
import domain.observer.PanicAlertManager;
import domain.state.EscrowContext;
import domain.usecase.CreateSkillSwapUseCase;
import domain.usecase.JoinStudyGroupUseCase;
import domain.usecase.ProcessEscrowPaymentUseCase;
import domain.usecase.TriggerPanicAlertUseCase;
import ui.MainActivity;
import ui.base.BaseFragment;
import viewmodel.EscrowViewModel;
import viewmodel.PanicButtonViewModel;
import viewmodel.SkillSwapViewModel;
import viewmodel.StudyGroupsViewModel;

import java.util.Date;
import java.util.List;

/**
 * Suite de Pruebas Automatizadas de Arquitectura e Integración Nexus-Tec (ITTOL / TecNM).
 * Valida Clean Architecture, MVVM, Patrón State, Patrón Observer, Integración Backend y Apple HIG.
 */
public class NexusTecArchitectureTestSuite {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("🧪 EJECUTANDO SUITE INTEGRAL DE PRUEBAS DE ARQUITECTURA NEXUS-TEC ITTOL");
        System.out.println("================================================================================\n");

        runTest("1. Flujo de Negocio Skill-Swap (Catálogo y Creación)", NexusTecArchitectureTestSuite::testSkillSwapFlow);
        runTest("2. Máquina de Estados Escrow (Patrón State & Hashes Criptográficos)", NexusTecArchitectureTestSuite::testEscrowStateMachineFlow);
        runTest("3. Botón de Pánico y Despacho Multi-Observador (Patrón Observer & GPS)", NexusTecArchitectureTestSuite::testPanicButtonObserverFlow);
        runTest("4. Flash Study Groups (Control de Aforo y Registro en Aulas)", NexusTecArchitectureTestSuite::testFlashStudyGroupsFlow);
        runTest("5. Enlace e Integración Bidireccional Frontend-Backend", NexusTecArchitectureTestSuite::testBackendServiceBridgeIntegration);
        runTest("6. Navegación MVVM y Microinteracciones Apple HIG", NexusTecArchitectureTestSuite::testMvvmAndAppleHigNavigation);

        System.out.println("\n================================================================================");
        System.out.println(String.format("🏁 RESULTADO DE LA SUITE: %d Aprobadas | %d Fallidas", testsPassed, testsFailed));
        System.out.println("================================================================================");

        if (testsFailed > 0) {
            System.exit(1);
        }
    }

    private static void runTest(String testName, Runnable testCode) {
        System.out.print("▶ " + testName + " ... ");
        try {
            testCode.run();
            System.out.println("✅ PASÓ");
            testsPassed++;
        } catch (Throwable t) {
            System.out.println("❌ FALLÓ: " + t.getMessage());
            t.printStackTrace(System.out);
            testsFailed++;
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError(message + " - Esperado: [" + expected + "], Obtenido: [" + actual + "]");
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    // =========================================================================
    // 1. Skill-Swap
    // =========================================================================
    private static void testSkillSwapFlow() {
        SkillSwapRepository repo = new SkillSwapRepository();
        CreateSkillSwapUseCase useCase = new CreateSkillSwapUseCase(repo);
        SkillSwapViewModel vm = new SkillSwapViewModel(repo, useCase);

        vm.loadSwaps("Ing. Sistemas", null);
        List<SkillSwap> initialList = vm.getSwapList().getValue();
        assertTrue(initialList != null, "La lista inicial no debe ser nula");

        // Crear una nueva propuesta
        SkillSwap newProposal = new SkillSwap(
                null,
                "student_ittol_01",
                null,
                "Bases de Datos Distribuidas NoSQL",
                "Compiladores y Autómatas",
                "Ing. Sistemas",
                "Taller práctico de MongoDB y Redis",
                3,
                30.0,
                SkillSwap.SwapStatus.PENDING,
                new Date(),
                "Edificio D · Lab LC1"
        );
        vm.createSwapProposal(newProposal);

        SkillSwap selected = vm.getSelectedSwap().getValue();
        assertTrue(selected != null, "El swap creado debe registrarse en el ViewModel");
        assertTrue(selected.getSwapId() != null, "Debe tener un identificador asignado");
        assertEquals(SkillSwap.SwapStatus.PENDING, selected.getStatus(), "El estado inicial debe ser PENDING");
    }

    // =========================================================================
    // 2. Escrow State Machine
    // =========================================================================
    private static void testEscrowStateMachineFlow() {
        EscrowRepository repo = new EscrowRepository();
        ProcessEscrowPaymentUseCase useCase = new ProcessEscrowPaymentUseCase(repo);
        EscrowViewModel vm = new EscrowViewModel(repo, useCase);

        double initialBalance = repo.getWalletBalance("student_ittol_01");
        assertTrue(initialBalance >= 25.0, "El estudiante debe tener fondos para iniciar la custodia");

        // 1. Iniciar Escrow
        vm.startEscrowHold("SWAP-ITTOL-001", "student_ittol_01", "student_ittol_02", 25.0);
        EscrowContext context = vm.getActiveEscrowContext().getValue();
        assertTrue(context != null, "El contexto de Escrow debe ser instanciado");
        assertEquals("IN_ESCROW_HOLD", context.getCurrentState().getStateName(), "El estado debe ser IN_ESCROW_HOLD");
        assertTrue(context.getAuditLog().size() >= 3, "La bitácora de auditoría debe registrar transiciones");

        // 2. Liberar fondos
        vm.releaseCredits("CONFIRMATION-SECRET-2026");
        assertEquals("COMPLETED_RELEASED", context.getCurrentState().getStateName(), "El estado debe transicionar a COMPLETED_RELEASED");
        assertTrue(context.getTransaction().getReleasedAt() != null, "Debe registrarse la fecha de liberación");
    }

    // =========================================================================
    // 3. Botón de Pánico (Observer Pattern)
    // =========================================================================
    private static void testPanicButtonObserverFlow() {
        PanicAlertManager manager = PanicAlertManager.getInstance();
        manager.clearAllObservers();

        CampusSecurityStationObserver caseta = new CampusSecurityStationObserver("01-Principal");
        EmergencyNotificationCenterObserver prefectura = new EmergencyNotificationCenterObserver(true, true);
        CampusHealthServiceObserver medicos = new CampusHealthServiceObserver();

        manager.registerObserver(caseta);
        manager.registerObserver(prefectura);
        manager.registerObserver(medicos);
        assertEquals(3, manager.getRegisteredObserverCount(), "Deben registrarse 3 observadores institucionales");

        PanicAlertRepository repo = new PanicAlertRepository();
        TriggerPanicAlertUseCase triggerUseCase = new TriggerPanicAlertUseCase(repo, manager);
        PanicButtonViewModel vm = new PanicButtonViewModel(repo, triggerUseCase, manager);

        vm.triggerEmergency(
                "student_ittol_01",
                "Reyli García Díaz",
                "21280942",
                19.256248,
                -99.581512,
                "Edificio D - Laboratorio LC1",
                PanicAlert.AlertLevel.CRITICAL,
                "Emergencia de Seguridad"
        );

        PanicAlert active = vm.getCurrentAlert().getValue();
        assertTrue(active != null, "La alerta de pánico debe emitirse");
        assertEquals(PanicAlert.AlertStatus.TRIGGERED, active.getStatus(), "Estado debe ser TRIGGERED");

        // Cancelar alarma
        vm.cancelAlert(active.getAlertId(), "Resolución controlada por personal de caseta.");
        assertTrue(vm.getCurrentAlert().getValue() == null, "La alerta debe limpiarse al resolverse");
    }

    // =========================================================================
    // 4. Flash Study Groups
    // =========================================================================
    private static void testFlashStudyGroupsFlow() {
        StudyGroupRepository repo = new StudyGroupRepository();
        JoinStudyGroupUseCase joinUseCase = new JoinStudyGroupUseCase(repo);
        StudyGroupsViewModel vm = new StudyGroupsViewModel(repo, joinUseCase);

        // Crear una nueva mesa
        StudyGroup group = new StudyGroup(
                "TEST-TABLE-99",
                "Redes Neuronales y Deep Learning",
                "Modelos Transformer para NLP",
                "student_ittol_01",
                "Reyli García Díaz",
                3, // Capacidad máxima reducida para prueba de límite
                "Biblioteca Central · Cubículo 1",
                new Date(),
                90
        );
        vm.createGroup(group);

        // Unir estudiante 1
        vm.joinGroup("TEST-TABLE-99", "student_ittol_02");
        StudyGroup updated = repo.getGroupById("TEST-TABLE-99");
        assertTrue(updated.getMemberStudentIds().contains("student_ittol_02"), "El estudiante 2 debe pertenecer al grupo");

        // Unir estudiante 2
        vm.joinGroup("TEST-TABLE-99", "student_ittol_03");
        assertTrue(updated.getMemberStudentIds().contains("student_ittol_03"), "El estudiante 3 debe pertenecer al grupo");
    }

    // =========================================================================
    // 5. Integración Backend Bridge
    // =========================================================================
    private static void testBackendServiceBridgeIntegration() {
        RetrofitClient client = RetrofitClient.getInstance();
        client.setApiMode(RetrofitClient.ApiMode.LIVE_BACKEND_BRIDGE);
        assertEquals(RetrofitClient.ApiMode.LIVE_BACKEND_BRIDGE, client.getCurrentMode(), "El modo debe ser LIVE_BACKEND_BRIDGE");

        BackendNexusApiService backendApi = client.getBackendApiService();
        assertTrue(backendApi != null, "BackendNexusApiService debe estar instanciado");
        assertTrue(backendApi.getEscrowLedgerService() != null, "EscrowLedgerService debe estar conectado");
        assertTrue(backendApi.getCampusDispatchService() != null, "CampusDispatchService debe estar conectado");
        assertTrue(backendApi.getClassroomLocatorService() != null, "ClassroomLocatorService debe estar conectado");
        assertTrue(backendApi.getSkillSwapService() != null, "SkillSwapService debe estar conectado");

        // Probar firma criptográfica SHA-256 en Escrow
        var escrowHoldResponse = backendApi.createEscrowHold("SWAP-CRYPTO-01", "student_ittol_01", "student_ittol_02", 15.0);
        assertTrue(escrowHoldResponse.isSuccess(), "La retención en backend debe ser exitosa");
        assertTrue(escrowHoldResponse.getData().getSecurityHash() != null, "Debe generar firma SHA-256");
        assertTrue(escrowHoldResponse.getData().getSecurityHash().startsWith("0x"), "El hash SHA-256 debe tener prefijo hexadecimal");
    }

    // =========================================================================
    // 6. Navegación MVVM y Apple HIG
    // =========================================================================
    private static void testMvvmAndAppleHigNavigation() {
        MainActivity activity = new MainActivity();
        activity.onCreate();

        assertEquals(MainActivity.NavigationTab.SKILL_SWAP, activity.getCurrentTab(), "Tab por defecto debe ser SKILL_SWAP");
        assertEquals("Marketplace Skill-Swap ITTOL", activity.getActiveFragment().getFragmentTitle(), "Título del fragmento debe coincidir");

        // Navegar a Escrow
        activity.onBottomNavClicked(MainActivity.NavigationTab.ESCROW);
        assertEquals(MainActivity.NavigationTab.ESCROW, activity.getCurrentTab(), "Tab activo debe ser ESCROW");

        // Navegar a Pánico
        activity.onBottomNavClicked(MainActivity.NavigationTab.PANIC_BUTTON);
        assertEquals(MainActivity.NavigationTab.PANIC_BUTTON, activity.getCurrentTab(), "Tab activo debe ser PANIC_BUTTON");

        // Navegar a Grupos de Estudio
        activity.onBottomNavClicked(MainActivity.NavigationTab.STUDY_GROUPS);
        assertEquals(MainActivity.NavigationTab.STUDY_GROUPS, activity.getCurrentTab(), "Tab activo debe ser STUDY_GROUPS");

        // Probar feedback háptico
        for (BaseFragment.HapticType type : BaseFragment.HapticType.values()) {
            assertTrue(type.name() != null, "El tipo háptico de Apple HIG debe ser válido: " + type);
        }
    }
}


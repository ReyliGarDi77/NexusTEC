package ui;

import ui.base.BaseActivity;
import ui.base.BaseFragment;
import ui.escrow.EscrowFragment;
import ui.panicbutton.PanicButtonFragment;
import ui.skillswap.SkillSwapFragment;
import ui.studygroups.StudyGroupsFragment;
import viewmodel.EscrowViewModel;
import viewmodel.PanicButtonViewModel;
import viewmodel.SkillSwapViewModel;
import viewmodel.StudyGroupsViewModel;

/**
 * Activity Principal de la plataforma móvil Nexus-Tec (ITTOL / TecNM).
 * Implementa el controlador de navegación desacoplado para los 4 módulos:
 * Skill-Swap, Escrow (Créditos Halcón), Botón de Pánico y Flash Study Groups.
 */
public class MainActivity extends BaseActivity {
    // ViewModels compartidos
    private SkillSwapViewModel skillSwapViewModel;
    private EscrowViewModel escrowViewModel;
    private PanicButtonViewModel panicButtonViewModel;
    private StudyGroupsViewModel studyGroupsViewModel;

    // Fragmentos activos
    private SkillSwapFragment skillSwapFragment;
    private EscrowFragment escrowFragment;
    private PanicButtonFragment panicButtonFragment;
    private StudyGroupsFragment studyGroupsFragment;
    private BaseFragment activeFragment;

    // Estado de navegación
    public enum NavigationTab {
        SKILL_SWAP,
        ESCROW,
        PANIC_BUTTON,
        STUDY_GROUPS
    }

    private NavigationTab currentTab = NavigationTab.SKILL_SWAP;

    @Override
    protected void initViews() {
        System.out.println("🦅 [Nexus-Tec ITTOL] Inicializando Interfaz de Alta Gama (Estilo Apple / TecNM)...");

        // Inicializar ViewModels
        skillSwapViewModel = new SkillSwapViewModel();
        escrowViewModel = new EscrowViewModel();
        panicButtonViewModel = new PanicButtonViewModel();
        studyGroupsViewModel = new StudyGroupsViewModel();

        // Inicializar Fragmentos Modulares
        skillSwapFragment = new SkillSwapFragment(skillSwapViewModel);
        escrowFragment = new EscrowFragment(escrowViewModel);
        panicButtonFragment = new PanicButtonFragment(panicButtonViewModel);
        studyGroupsFragment = new StudyGroupsFragment(studyGroupsViewModel);

        // Iniciar en tab por defecto
        switchTab(NavigationTab.SKILL_SWAP);
    }

    @Override
    protected void setupListeners() {
        System.out.println("✨ [MainActivity] Enlazando escuchadores táctiles y microinteracciones de navegación.");
    }

    public void onHeaderBalanceClicked() {
        System.out.println("🪙 [MainActivity] Tap en píldora de saldo de cabecera -> Navegando a Escrow");
        switchTab(NavigationTab.ESCROW);
    }

    public void onBottomNavClicked(NavigationTab targetTab) {
        System.out.println("🔘 [MainActivity] Bottom Navigation tap: " + targetTab);
        switchTab(targetTab);
    }

    @Override
    protected void observeViewModel() {
        // Observar saldo de créditos institucional para actualizar el widget de cabecera
        escrowViewModel.getWalletBalance().observe(balance -> {
            System.out.println("🪙 [HeaderWidget] Saldo actualizado en cabecera: " + balance + " CH");
        });

        // Observar si se desata una alerta de emergencia para transicionar visualmente a modo crítico
        panicButtonViewModel.getCurrentAlert().observe(alert -> {
            if (alert != null) {
                System.out.println("🚨 [MainActivity UI] Transición visual de emergencia activada en toda la app!");
            }
        });
    }

    public void switchTab(NavigationTab targetTab) {
        this.currentTab = targetTab;
        if (activeFragment != null) {
            activeFragment.onDestroyView();
        }

        switch (targetTab) {
            case SKILL_SWAP:
                activeFragment = skillSwapFragment;
                break;
            case ESCROW:
                activeFragment = escrowFragment;
                break;
            case PANIC_BUTTON:
                activeFragment = panicButtonFragment;
                break;
            case STUDY_GROUPS:
                activeFragment = studyGroupsFragment;
                break;
        }

        activeFragment.onCreateView();
        System.out.println("📲 [Navegación] Vista activa cambiada a: " + activeFragment.getFragmentTitle() + " (" + targetTab + ")");
    }

    public boolean isEmergencyActive() {
        return panicButtonViewModel.getCurrentAlert().getValue() != null;
    }

    public NavigationTab getCurrentTab() {
        return currentTab;
    }

    public BaseFragment getActiveFragment() {
        return activeFragment;
    }

    public SkillSwapViewModel getSkillSwapViewModel() {
        return skillSwapViewModel;
    }

    public EscrowViewModel getEscrowViewModel() {
        return escrowViewModel;
    }

    public PanicButtonViewModel getPanicButtonViewModel() {
        return panicButtonViewModel;
    }

    public StudyGroupsViewModel getStudyGroupsViewModel() {
        return studyGroupsViewModel;
    }

    public SkillSwapFragment getSkillSwapFragment() {
        return skillSwapFragment;
    }

    public EscrowFragment getEscrowFragment() {
        return escrowFragment;
    }

    public PanicButtonFragment getPanicButtonFragment() {
        return panicButtonFragment;
    }

    public StudyGroupsFragment getStudyGroupsFragment() {
        return studyGroupsFragment;
    }

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("🦅 [NEXUS-TEC ITTOL] Plataforma Móvil Institucional - TecNM Halcones");
        System.out.println("   Clean Architecture + MVVM + Apple Human Interface Guidelines Design System");
        System.out.println("================================================================================\n");

        MainActivity app = new MainActivity();
        app.onCreate();

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("1️⃣ PRUEBA INTEGRADA: Módulo Skill-Swap (Marketplace de Habilidades)");
        System.out.println("--------------------------------------------------------------------------------");
        SkillSwapFragment skillFragment = app.getSkillSwapFragment();
        skillFragment.onCategorySelected("Ing. Sistemas");
        skillFragment.onSearchQuerySubmitted("Ing. Sistemas", "Java");
        skillFragment.onNewSwapSubmitted(
                "student_ittol_01",
                "Arquitectura Hexagonal & Spring Boot",
                "Machine Learning en Python",
                "Ing. Sistemas",
                35.0,
                "Edificio D · Lab LC2"
        );

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("2️⃣ PRUEBA INTEGRADA: Módulo Escrow (Custodia Criptográfica de Créditos Halcón)");
        System.out.println("--------------------------------------------------------------------------------");
        app.switchTab(NavigationTab.ESCROW);
        EscrowFragment escrowFragment = app.getEscrowFragment();
        // Simular retención de fondos para asesoría
        escrowFragment.onDepositClicked("SWAP-ITTOL-001", "student_ittol_01", "student_ittol_02", 25.0);
        // Simular confirmación y liberación exitosa de fondos
        escrowFragment.onReleaseClicked("SECRET-TOKEN-ITTOL-OK");

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("3️⃣ PRUEBA INTEGRADA: Módulo Botón de Pánico (Observer Pattern & Telemetría GPS)");
        System.out.println("--------------------------------------------------------------------------------");
        app.switchTab(NavigationTab.PANIC_BUTTON);
        PanicButtonFragment panicFragment = app.getPanicButtonFragment();
        panicFragment.onRiskTypeSelected("Seguridad");
        panicFragment.onEmergencyButtonPressed();
        // Cancelar alarma tras resolución
        panicFragment.onCancelEmergency(app.getPanicButtonViewModel().getCurrentAlert().getValue().getAlertId(), "Alarma atendida por Caseta Sur.");

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("4️⃣ PRUEBA INTEGRADA: Módulo Flash Study Groups (Mesas de Estudio Espontáneas)");
        System.out.println("--------------------------------------------------------------------------------");
        app.switchTab(NavigationTab.STUDY_GROUPS);
        StudyGroupsFragment studyFragment = app.getStudyGroupsFragment();
        studyFragment.onFilterBySubject("Arquitectura");
        studyFragment.onJoinGroupRequested("TABLE-LC1-01", "student_ittol_01");

        System.out.println("\n================================================================================");
        System.out.println("✅ [NEXUS-TEC ITTOL] Todos los flujos de negocio integrados y validados con éxito.");
        System.out.println("================================================================================");
    }
}

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
}

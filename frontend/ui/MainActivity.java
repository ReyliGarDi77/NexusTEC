package ui;

import ui.base.BaseActivity;
import ui.escrow.EscrowActivity;
import ui.panicbutton.PanicButtonActivity;
import ui.skillswap.SkillSwapActivity;
import ui.studygroups.StudyGroupsActivity;

/**
 * Activity Principal de la plataforma móvil Nexus-Tec.
 * Controla el menú de navegación y coordina los accesos directos a los módulos:
 * Skill-Swap, Escrow (Créditos Halcón), Botón de Pánico y Flash Study Groups.
 */
public class MainActivity extends BaseActivity {
    private SkillSwapActivity skillSwapActivity;
    private EscrowActivity escrowActivity;
    private PanicButtonActivity panicButtonActivity;
    private StudyGroupsActivity studyGroupsActivity;

    @Override
    protected void initViews() {
        System.out.println("🚀 [MainActivity] Inicializando Dashboard Nexus-Tec Halcones...");
        skillSwapActivity = new SkillSwapActivity();
        escrowActivity = new EscrowActivity();
        panicButtonActivity = new PanicButtonActivity();
        studyGroupsActivity = new StudyGroupsActivity();
    }

    @Override
    protected void setupListeners() {
        System.out.println("📱 [MainActivity] Configurando eventos de BottomNavigationView.");
    }

    @Override
    protected void observeViewModel() {
        System.out.println("📊 [MainActivity] Observando estados globales de la aplicación.");
    }

    public void navigateToSkillSwap() {
        System.out.println("-> Navegando a Módulo Skill-Swap...");
        skillSwapActivity.onCreate();
    }

    public void navigateToEscrow() {
        System.out.println("-> Navegando a Módulo Escrow (Créditos Halcón)...");
        escrowActivity.onCreate();
    }

    public void navigateToPanicButton() {
        System.out.println("-> Navegando a Módulo Botón de Pánico...");
        panicButtonActivity.onCreate();
    }

    public void navigateToStudyGroups() {
        System.out.println("-> Navegando a Módulo Flash Study Groups...");
        studyGroupsActivity.onCreate();
    }
}


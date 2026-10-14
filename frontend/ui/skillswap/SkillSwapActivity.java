package ui.skillswap;

import ui.base.BaseActivity;
import viewmodel.SkillSwapViewModel;

/**
 * Activity para el módulo Skill-Swap.
 * Aloja los fragmentos de catálogo de habilidades e intercambio entre estudiantes.
 */
public class SkillSwapActivity extends BaseActivity {
    private SkillSwapViewModel viewModel;
    private SkillSwapFragment fragment;

    @Override
    protected void initViews() {
        viewModel = new SkillSwapViewModel();
        fragment = new SkillSwapFragment(viewModel);
        fragment.onCreateView();
    }

    @Override
    protected void setupListeners() {
        // Configuración de Toolbar y botones de acción
    }

    @Override
    protected void observeViewModel() {
        viewModel.getIsLoading().observe(loading -> {
            if (Boolean.TRUE.equals(loading)) {
                System.out.println("🔄 [SkillSwapActivity] Cargando catálogo de habilidades...");
            }
        });

        viewModel.getErrorMessage().observe(error -> {
            if (error != null) {
                System.err.println("❌ [SkillSwapActivity] Error: " + error);
            }
        });

        viewModel.getSuccessMessage().observe(success -> {
            if (success != null) {
                System.out.println("✅ [SkillSwapActivity] Notificación: " + success);
            }
        });
    }

    public SkillSwapViewModel getViewModel() {
        return viewModel;
    }
}


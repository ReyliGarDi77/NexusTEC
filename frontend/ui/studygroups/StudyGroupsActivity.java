package ui.studygroups;

import ui.base.BaseActivity;
import viewmodel.StudyGroupsViewModel;

/**
 * Activity para el módulo Flash Study Groups.
 * Permite a los estudiantes descubrir, organizar y unirse a sesiones de estudio espontáneas en campus.
 */
public class StudyGroupsActivity extends BaseActivity {
    private StudyGroupsViewModel viewModel;
    private StudyGroupsFragment fragment;

    @Override
    protected void initViews() {
        viewModel = new StudyGroupsViewModel();
        fragment = new StudyGroupsFragment(viewModel);
        fragment.onCreateView();
    }

    @Override
    protected void setupListeners() {
    }

    @Override
    protected void observeViewModel() {
        viewModel.getIsLoading().observe(loading -> {
            if (Boolean.TRUE.equals(loading)) {
                System.out.println("🔄 [StudyGroupsActivity] Cargando grupos de estudio...");
            }
        });

        viewModel.getErrorMessage().observe(err -> {
            if (err != null) System.err.println("⚠️ [StudyGroupsActivity] " + err);
        });

        viewModel.getSuccessMessage().observe(msg -> {
            if (msg != null) System.out.println("✅ [StudyGroupsActivity] " + msg);
        });
    }

    public StudyGroupsViewModel getViewModel() {
        return viewModel;
    }
}


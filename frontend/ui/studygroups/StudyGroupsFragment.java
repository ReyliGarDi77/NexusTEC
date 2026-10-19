package ui.studygroups;

import data.model.StudyGroup;
import ui.base.BaseFragment;
import viewmodel.StudyGroupsViewModel;

import java.util.Date;

/**
 * Fragment visual para Flash Study Groups (Mesas de Trabajo y Estudio Espontáneas en ITTOL).
 * Incluye filtrado rápido por áreas de ingeniería, indicador de cupos en tiempo real
 * y confirmación táctil al unirse a una sesión.
 */
public class StudyGroupsFragment extends BaseFragment {
    private final StudyGroupsViewModel viewModel;
    private StudyGroupAdapter adapter;

    public StudyGroupsFragment(StudyGroupsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public String getFragmentTitle() {
        return "Grupos de Estudio Flash ITTOL";
    }

    @Override
    protected void initViews() {
        adapter = new StudyGroupAdapter();
        viewModel.loadGroups(null);
    }

    @Override
    protected void setupListeners() {
        adapter.setOnItemClickListener((group, position) -> {
            performHapticFeedback(HapticType.LIGHT_IMPACT);
            System.out.println("👥 [StudyGroupsFragment] Mesa de estudio seleccionada: " + group.getSubjectName() + " en " + group.getMeetingLocation());
            viewModel.selectGroup(group);
        });

        adapter.setOnJoinClickListener((group, position) -> {
            performHapticFeedback(HapticType.MEDIUM_IMPACT);
            System.out.println("🤝 [StudyGroupsFragment] Tap en 'Unirme' para la mesa: " + group.getSubjectName());
            onJoinGroupRequested(group.getGroupId(), "student_ittol_01");
        });
    }

    @Override
    protected void observeViewModel() {
        viewModel.getStudyGroupsList().observe(groups -> {
            adapter.updateList(groups);
            System.out.println("📚 [StudyGroupsFragment] Mostrando " + (groups != null ? groups.size() : 0) + " salas de estudio abiertas en campus ITTOL.");
        });

        viewModel.getSuccessMessage().observe(success -> {
            if (success != null) {
                performHapticFeedback(HapticType.SUCCESS_NOTIFICATION);
                System.out.println("🎉 [StudyGroupsFragment] " + success);
            }
        });

        viewModel.getErrorMessage().observe(err -> {
            if (err != null) {
                performHapticFeedback(HapticType.WARNING_ALERT);
                System.err.println("⚠️ [StudyGroupsFragment] " + err);
            }
        });
    }

    public void onFilterBySubject(String subject) {
        performHapticFeedback(HapticType.LIGHT_IMPACT);
        System.out.println("🔍 [StudyGroupsFragment] Filtrando por carrera / materia: " + subject);
        viewModel.loadGroups(subject);
    }

    public void onCreateNewGroup(String subject, String topic, String creatorId, String creatorName,
                                 int capacity, String location, int durationMinutes) {
        performHapticFeedback(HapticType.SUCCESS_NOTIFICATION);
        StudyGroup newGroup = new StudyGroup(
                null,
                subject,
                topic,
                creatorId,
                creatorName,
                capacity,
                location != null ? location : "Edificio F · Cubículo 2",
                new Date(),
                durationMinutes
        );
        viewModel.createGroup(newGroup);
    }

    public void onJoinGroupRequested(String groupId, String studentId) {
        performHapticFeedback(HapticType.MEDIUM_IMPACT);
        System.out.println("🤝 [StudyGroupsFragment] Solicitando unirse a la sala " + groupId + " para estudiante " + studentId);
        viewModel.joinGroup(groupId, studentId);
    }

    public StudyGroupAdapter getAdapter() {
        return adapter;
    }
}

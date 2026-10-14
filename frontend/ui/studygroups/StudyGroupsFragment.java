package ui.studygroups;

import data.model.StudyGroup;
import ui.base.BaseFragment;
import viewmodel.StudyGroupsViewModel;

import java.util.Date;

/**
 * Fragment para explorar y gestionar grupos de estudio flash activos.
 */
public class StudyGroupsFragment extends BaseFragment {
    private final StudyGroupsViewModel viewModel;
    private StudyGroupAdapter adapter;

    public StudyGroupsFragment(StudyGroupsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void initViews() {
        adapter = new StudyGroupAdapter();
    }

    @Override
    protected void setupListeners() {
        adapter.setOnItemClickListener((group, position) -> {
            System.out.println("👥 [StudyGroupsFragment] Grupo seleccionado: " + group.getSubjectName());
            viewModel.selectGroup(group);
        });
    }

    @Override
    protected void observeViewModel() {
        viewModel.getStudyGroupsList().observe(groups -> {
            adapter.updateList(groups);
            System.out.println("📚 [StudyGroupsFragment] Mostrando " + (groups != null ? groups.size() : 0) + " grupos de estudio.");
        });
    }

    public void onFilterBySubject(String subject) {
        viewModel.loadGroups(subject);
    }

    public void onCreateNewGroup(String subject, String topic, String creatorId, String creatorName,
                                 int capacity, String location, int durationMinutes) {
        StudyGroup newGroup = new StudyGroup(
                null,
                subject,
                topic,
                creatorId,
                creatorName,
                capacity,
                location,
                new Date(),
                durationMinutes
        );
        viewModel.createGroup(newGroup);
    }

    public void onJoinGroupRequested(String groupId, String studentId) {
        viewModel.joinGroup(groupId, studentId);
    }

    public StudyGroupAdapter getAdapter() {
        return adapter;
    }
}

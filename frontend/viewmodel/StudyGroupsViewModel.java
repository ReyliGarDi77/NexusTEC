package viewmodel;

import data.model.StudyGroup;
import data.repository.StudyGroupRepository;
import domain.usecase.JoinStudyGroupUseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel del módulo Flash Study Groups.
 * Gestiona el listado interactivo de grupos de estudio, filtros de materia y proceso de unión de estudiantes.
 */
public class StudyGroupsViewModel extends BaseViewModel {
    private final StudyGroupRepository repository;
    private final JoinStudyGroupUseCase joinUseCase;

    private final ObservableValue<List<StudyGroup>> studyGroupsList = new ObservableValue<>(new ArrayList<>());
    private final ObservableValue<StudyGroup> selectedGroup = new ObservableValue<>(null);

    public StudyGroupsViewModel() {
        this.repository = StudyGroupRepository.getInstance();
        this.joinUseCase = new JoinStudyGroupUseCase(repository);
    }

    public StudyGroupsViewModel(StudyGroupRepository repository, JoinStudyGroupUseCase joinUseCase) {
        this.repository = repository;
        this.joinUseCase = joinUseCase;
    }

    public ObservableValue<List<StudyGroup>> getStudyGroupsList() {
        return studyGroupsList;
    }

    public ObservableValue<StudyGroup> getSelectedGroup() {
        return selectedGroup;
    }

    public void loadGroups(String subjectFilter) {
        setLoading(true);
        try {
            List<StudyGroup> list = repository.getOpenGroups(subjectFilter);
            studyGroupsList.setValue(list);
        } catch (Exception e) {
            setError("Error cargando grupos de estudio: " + e.getMessage());
        } finally {
            setLoading(false);
        }
    }

    public void createGroup(StudyGroup group) {
        setLoading(true);
        try {
            StudyGroup created = repository.createGroup(group);
            setSuccess("Grupo de estudio creado con éxito.");
            selectedGroup.setValue(created);
            loadGroups(null);
        } catch (Exception e) {
            setError("Error al crear grupo de estudio: " + e.getMessage());
        } finally {
            setLoading(false);
        }
    }

    public void joinGroup(String groupId, String studentId) {
        setLoading(true);
        try {
            boolean success = joinUseCase.execute(groupId, studentId);
            if (success) {
                setSuccess("¡Te has unido exitosamente al grupo de estudio!");
                loadGroups(null);
            }
        } catch (Exception e) {
            setError(e.getMessage());
        } finally {
            setLoading(false);
        }
    }

    public void selectGroup(StudyGroup group) {
        selectedGroup.setValue(group);
    }
}


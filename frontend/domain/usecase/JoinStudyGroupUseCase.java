package domain.usecase;

import data.model.StudyGroup;
import data.repository.StudyGroupRepository;

/**
 * Caso de Uso: Unirse a un Grupo de Estudio Flash.
 * Aplica validaciones de cupo disponible y duplicidad de inscripción.
 */
public class JoinStudyGroupUseCase {
    private final StudyGroupRepository repository;

    public JoinStudyGroupUseCase(StudyGroupRepository repository) {
        this.repository = repository != null ? repository : StudyGroupRepository.getInstance();
    }

    public boolean execute(String groupId, String studentId) {
        if (groupId == null || groupId.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificador de grupo inválido.");
        }
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Identificador de estudiante inválido.");
        }

        StudyGroup group = repository.getGroupById(groupId);
        if (group == null) {
            throw new IllegalStateException("El grupo de estudio no fue encontrado.");
        }
        if (group.getStatus() == StudyGroup.GroupStatus.FULL) {
            throw new IllegalStateException("El grupo de estudio ya ha alcanzado su capacidad máxima.");
        }
        if (group.getMemberStudentIds().contains(studentId)) {
            throw new IllegalStateException("El estudiante ya es miembro activo de este grupo de estudio.");
        }

        return repository.joinGroup(groupId, studentId);
    }
}

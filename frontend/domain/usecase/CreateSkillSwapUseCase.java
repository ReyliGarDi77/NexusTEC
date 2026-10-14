package domain.usecase;

import data.model.SkillSwap;
import data.repository.SkillSwapRepository;

/**
 * Caso de Uso: Registrar una nueva solicitud o propuesta de intercambio de habilidades (Skill-Swap).
 * Aplica reglas de negocio sobre créditos mínimos y validación académica.
 */
public class CreateSkillSwapUseCase {
    private final SkillSwapRepository repository;

    public CreateSkillSwapUseCase(SkillSwapRepository repository) {
        this.repository = repository != null ? repository : SkillSwapRepository.getInstance();
    }

    public SkillSwap execute(SkillSwap swap) {
        if (swap == null) {
            throw new IllegalArgumentException("La propuesta de intercambio no puede ser nula.");
        }
        if (swap.getOfferedSkill() == null || swap.getOfferedSkill().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar la habilidad que ofrece enseñar.");
        }
        if (swap.getRequestedSkill() == null || swap.getRequestedSkill().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar la habilidad que solicita aprender.");
        }
        if (swap.getAgreedHalconCredits() < 0) {
            throw new IllegalArgumentException("El valor de Créditos Halcón no puede ser negativo.");
        }

        swap.setStatus(SkillSwap.SwapStatus.PENDING);
        return repository.createSwap(swap);
    }
}

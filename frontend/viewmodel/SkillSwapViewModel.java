package viewmodel;

import data.model.SkillSwap;
import data.repository.SkillSwapRepository;
import domain.usecase.CreateSkillSwapUseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel del módulo Skill-Swap.
 * Administra el catálogo de intercambios disponibles, filtrado por categorías y la creación de propuestas.
 */
public class SkillSwapViewModel extends BaseViewModel {
    private final SkillSwapRepository repository;
    private final CreateSkillSwapUseCase createUseCase;

    private final ObservableValue<List<SkillSwap>> swapList = new ObservableValue<>(new ArrayList<>());
    private final ObservableValue<SkillSwap> selectedSwap = new ObservableValue<>(null);

    public SkillSwapViewModel() {
        this.repository = SkillSwapRepository.getInstance();
        this.createUseCase = new CreateSkillSwapUseCase(repository);
    }

    public SkillSwapViewModel(SkillSwapRepository repository, CreateSkillSwapUseCase createUseCase) {
        this.repository = repository;
        this.createUseCase = createUseCase;
    }

    public ObservableValue<List<SkillSwap>> getSwapList() {
        return swapList;
    }

    public ObservableValue<SkillSwap> getSelectedSwap() {
        return selectedSwap;
    }

    public void loadSwaps(String category, String keyword) {
        setLoading(true);
        try {
            List<SkillSwap> results = repository.getAvailableSwaps(category, keyword);
            swapList.setValue(results);
        } catch (Exception e) {
            setError("Error al cargar intercambios: " + e.getMessage());
        } finally {
            setLoading(false);
        }
    }

    public void createSwapProposal(SkillSwap swap) {
        setLoading(true);
        try {
            SkillSwap created = createUseCase.execute(swap);
            setSuccess("Propuesta de Skill-Swap publicada exitosamente.");
            selectedSwap.setValue(created);
            loadSwaps(null, null);
        } catch (Exception e) {
            setError(e.getMessage());
        } finally {
            setLoading(false);
        }
    }

    public void selectSwap(SkillSwap swap) {
        selectedSwap.setValue(swap);
    }
}


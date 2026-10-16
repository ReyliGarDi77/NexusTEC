package ui.skillswap;

import data.model.SkillSwap;
import ui.base.BaseFragment;
import viewmodel.SkillSwapViewModel;

import java.util.Date;

/**
 * Fragment visual de alta gama para el Marketplace de Habilidades (Skill-Swap).
 * Soporta búsqueda en tiempo real, filtrado por materias y microinteracciones táctiles.
 */
public class SkillSwapFragment extends BaseFragment {
    private final SkillSwapViewModel viewModel;
    private SkillSwapAdapter adapter;
    private String selectedCategory = "Todos";

    public SkillSwapFragment(SkillSwapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public String getFragmentTitle() {
        return "Marketplace Skill-Swap ITTOL";
    }

    @Override
    protected void initViews() {
        adapter = new SkillSwapAdapter();
        viewModel.loadSwaps(null, null);
    }

    @Override
    protected void setupListeners() {
        adapter.setOnItemClickListener((swap, position) -> {
            performHapticFeedback(HapticType.LIGHT_IMPACT);
            System.out.println("👉 [SkillSwapFragment] Intercambio seleccionado: " + swap.getOfferedSkill() + " por " + swap.getRequestedSkill());
            viewModel.selectSwap(swap);
        });
    }

    @Override
    protected void observeViewModel() {
        viewModel.getSwapList().observe(swaps -> {
            adapter.updateList(swaps);
            System.out.println("📋 [SkillSwapFragment] Catálogo actualizado con " + (swaps != null ? swaps.size() : 0) + " intercambios disponibles.");
        });

        viewModel.getIsLoading().observe(loading -> {
            if (Boolean.TRUE.equals(loading)) {
                System.out.println("🔄 [SkillSwapFragment] Cargando intercambios con animación de shimmer...");
            }
        });
    }

    public void onCategorySelected(String category) {
        performHapticFeedback(HapticType.LIGHT_IMPACT);
        this.selectedCategory = category;
        System.out.println("🏷️ [SkillSwapFragment] Filtro de categoría: " + category);
        viewModel.loadSwaps(category, null);
    }

    public void onSearchQuerySubmitted(String category, String keyword) {
        viewModel.loadSwaps(category != null ? category : selectedCategory, keyword);
    }

    public void onNewSwapSubmitted(String requesterId, String offered, String requested, String career, double credits, String location) {
        performHapticFeedback(HapticType.SUCCESS_NOTIFICATION);
        SkillSwap newSwap = new SkillSwap(
                null,
                requesterId,
                null,
                offered,
                requested,
                career,
                "Asesoría y vinculación entre pares en ITTOL.",
                2,
                credits,
                SkillSwap.SwapStatus.PENDING,
                new Date(),
                location != null ? location : "Biblioteca Central ITTOL · Cubículo 3"
        );
        viewModel.createSwapProposal(newSwap);
    }

    public SkillSwapAdapter getAdapter() {
        return adapter;
    }
}

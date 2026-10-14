package ui.skillswap;

import data.model.SkillSwap;
import ui.base.BaseFragment;
import viewmodel.SkillSwapViewModel;

import java.util.Date;

/**
 * Fragment visual para listar y filtrar intercambios de habilidades (Skill-Swap).
 */
public class SkillSwapFragment extends BaseFragment {
    private final SkillSwapViewModel viewModel;
    private SkillSwapAdapter adapter;

    public SkillSwapFragment(SkillSwapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    protected void initViews() {
        adapter = new SkillSwapAdapter();
    }

    @Override
    protected void setupListeners() {
        adapter.setOnItemClickListener((swap, position) -> {
            System.out.println("👉 [SkillSwapFragment] Intercambio seleccionado: " + swap.getOfferedSkill() + " por " + swap.getRequestedSkill());
            viewModel.selectSwap(swap);
        });
    }

    @Override
    protected void observeViewModel() {
        viewModel.getSwapList().observe(swaps -> {
            adapter.updateList(swaps);
            System.out.println("📋 [SkillSwapFragment] Lista actualizada con " + (swaps != null ? swaps.size() : 0) + " intercambios.");
        });
    }

    public void onSearchQuerySubmitted(String category, String keyword) {
        viewModel.loadSwaps(category, keyword);
    }

    public void onNewSwapSubmitted(String requesterId, String offered, String requested, double credits) {
        SkillSwap newSwap = new SkillSwap(
                null,
                requesterId,
                null,
                offered,
                requested,
                "Académico",
                "Intercambio formativo",
                2,
                credits,
                SkillSwap.SwapStatus.PENDING,
                new Date(),
                "Biblioteca Campus"
        );
        viewModel.createSwapProposal(newSwap);
    }

    public SkillSwapAdapter getAdapter() {
        return adapter;
    }
}


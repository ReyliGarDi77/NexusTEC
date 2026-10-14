package ui.skillswap;

import data.model.SkillSwap;
import ui.base.BaseAdapter;

/**
 * Adaptador para renderizar tarjetas de intercambios Skill-Swap en RecyclerView.
 */
public class SkillSwapAdapter extends BaseAdapter<SkillSwap, SkillSwapAdapter.SkillSwapViewHolder> {

    public static class SkillSwapViewHolder extends BaseAdapter.BaseViewHolder {
        public String titleText;
        public String creditsText;
        public String statusBadgeText;

        public SkillSwapViewHolder(int itemViewId) {
            super(itemViewId);
        }
    }

    @Override
    public SkillSwapViewHolder onCreateViewHolder(int position) {
        return new SkillSwapViewHolder(position);
    }

    @Override
    public void onBindViewHolder(SkillSwapViewHolder holder, int position) {
        SkillSwap item = getItem(position);
        if (item != null) {
            holder.titleText = "Ofrece: " + item.getOfferedSkill() + " | Busca: " + item.getRequestedSkill();
            holder.creditsText = item.getAgreedHalconCredits() + " Créditos Halcón";
            holder.statusBadgeText = item.getStatus().name();
        }
    }
}

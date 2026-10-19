package ui.skillswap;

import data.model.SkillSwap;
import ui.base.BaseAdapter;

/**
 * Adaptador para renderizar tarjetas de intercambios Skill-Swap en RecyclerView.
 */
public class SkillSwapAdapter extends BaseAdapter<SkillSwap, SkillSwapAdapter.SkillSwapViewHolder> {

    public static class SkillSwapViewHolder extends BaseAdapter.BaseViewHolder {
        public String categoryText;
        public String creditsText;
        public String offeredText;
        public String requestedText;
        public String locationText;
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
            holder.categoryText = item.getCategory() != null ? item.getCategory().toUpperCase() : "GENERAL";
            holder.creditsText = String.format("%.0f CH", item.getAgreedHalconCredits());
            holder.offeredText = "Ofrece: " + item.getOfferedSkill();
            holder.requestedText = "Busca: " + item.getRequestedSkill();
            holder.locationText = item.getMeetingLocation() != null ? item.getMeetingLocation() : "Campus ITTOL";
            holder.statusBadgeText = item.getStatus() != null ? item.getStatus().name() : "PENDING";
        }
    }
}


package ui.studygroups;

import data.model.StudyGroup;
import ui.base.BaseAdapter;

/**
 * Adaptador para listar tarjetas de grupos de estudio flash en RecyclerView.
 */
public class StudyGroupAdapter extends BaseAdapter<StudyGroup, StudyGroupAdapter.StudyGroupViewHolder> {

    public interface OnJoinClickListener {
        void onJoinClick(StudyGroup group, int position);
    }

    private OnJoinClickListener joinClickListener;

    public void setOnJoinClickListener(OnJoinClickListener listener) {
        this.joinClickListener = listener;
    }

    public void triggerJoinClick(int position) {
        StudyGroup group = getItem(position);
        if (group != null && joinClickListener != null) {
            joinClickListener.onJoinClick(group, position);
        }
    }

    public static class StudyGroupViewHolder extends BaseAdapter.BaseViewHolder {
        public String subjectText;
        public String topicText;
        public String capacityBadge;
        public String locationText;
        public boolean isFull;
        public String joinButtonText;

        public StudyGroupViewHolder(int itemViewId) {
            super(itemViewId);
        }
    }

    @Override
    public StudyGroupViewHolder onCreateViewHolder(int position) {
        return new StudyGroupViewHolder(position);
    }

    @Override
    public void onBindViewHolder(StudyGroupViewHolder holder, int position) {
        StudyGroup item = getItem(position);
        if (item != null) {
            holder.subjectText = item.getSubjectName();
            holder.topicText = item.getTopicDescription();
            int current = item.getMemberStudentIds() != null ? item.getMemberStudentIds().size() : 0;
            holder.isFull = current >= item.getMaxCapacity();
            holder.capacityBadge = current + "/" + item.getMaxCapacity() + " Miembros";
            holder.locationText = "📍 " + (item.getMeetingLocation() != null ? item.getMeetingLocation() : "Campus ITTOL");
            holder.joinButtonText = holder.isFull ? "Cupo Lleno" : "Unirme";
        }
    }
}


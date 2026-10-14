package ui.studygroups;

import data.model.StudyGroup;
import ui.base.BaseAdapter;

/**
 * Adaptador para listar tarjetas de grupos de estudio flash en RecyclerView.
 */
public class StudyGroupAdapter extends BaseAdapter<StudyGroup, StudyGroupAdapter.StudyGroupViewHolder> {

    public static class StudyGroupViewHolder extends BaseAdapter.BaseViewHolder {
        public String subjectText;
        public String topicText;
        public String capacityBadge;
        public String locationText;

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
            holder.capacityBadge = current + "/" + item.getMaxCapacity() + " Miembros";
            holder.locationText = "📍 " + item.getMeetingLocation();
        }
    }
}

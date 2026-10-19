package ui.panicbutton;

import data.model.PanicAlert;
import ui.base.BaseAdapter;

/**
 * Adaptador para listar alertas de emergencia y eventos del campus en RecyclerView (item_panic_alert.xml).
 */
public class PanicAlertAdapter extends BaseAdapter<PanicAlert, PanicAlertAdapter.PanicAlertViewHolder> {

    public static class PanicAlertViewHolder extends BaseAdapter.BaseViewHolder {
        public String titleText;
        public String statusText;
        public String studentInfoText;
        public String locationText;

        public PanicAlertViewHolder(int itemViewId) {
            super(itemViewId);
        }
    }

    @Override
    public PanicAlertViewHolder onCreateViewHolder(int position) {
        return new PanicAlertViewHolder(position);
    }

    @Override
    public void onBindViewHolder(PanicAlertViewHolder holder, int position) {
        PanicAlert item = getItem(position);
        if (item != null) {
            String level = item.getAlertLevel() != null ? item.getAlertLevel().name() : "CRITICAL";
            holder.titleText = "🚨 ALERTA " + level + " #" + (item.getAlertId() != null ? item.getAlertId() : "NEXUS");
            holder.statusText = item.getStatus() != null ? item.getStatus().name() : "DESPACHADA";
            holder.studentInfoText = "Estudiante: " + item.getStudentName() + " · Matrícula: " + item.getStudentCode();
            holder.locationText = "📍 Ubicación: " + (item.getCampusBuildingZone() != null ? item.getCampusBuildingZone() : "Campus Central") +
                    " (Lat " + String.format("%.4f", item.getLatitude()) + ", Lon " + String.format("%.4f", item.getLongitude()) + ")";
        }
    }
}

package ui.escrow;

import data.model.EscrowTransaction;
import ui.base.BaseAdapter;

/**
 * Adaptador para mostrar historial de transacciones y estados de custodia de Créditos Halcón.
 */
public class EscrowAdapter extends BaseAdapter<EscrowTransaction, EscrowAdapter.EscrowViewHolder> {

    public static class EscrowViewHolder extends BaseAdapter.BaseViewHolder {
        public String idText;
        public String amountText;
        public String descriptionText;
        public String timestampText;
        public String statusPillText;
        public String securityHashSnippet;

        public EscrowViewHolder(int itemViewId) {
            super(itemViewId);
        }
    }

    @Override
    public EscrowViewHolder onCreateViewHolder(int position) {
        return new EscrowViewHolder(position);
    }

    @Override
    public void onBindViewHolder(EscrowViewHolder holder, int position) {
        EscrowTransaction item = getItem(position);
        if (item != null) {
            holder.idText = "TX: #" + item.getTransactionId();
            holder.amountText = String.format("%.2f CH", item.getCreditsAmount());
            holder.descriptionText = "Custodia por intercambio: " + (item.getRelatedSwapId() != null ? item.getRelatedSwapId() : "Asesoría ITTOL");
            holder.timestampText = item.getCreatedAt() != null ? item.getCreatedAt().toString() : "Fecha reciente";
            holder.statusPillText = item.getStatus() != null ? item.getStatus().name() : "IN_ESCROW_HOLD";
            holder.securityHashSnippet = item.getSecurityHash() != null && item.getSecurityHash().length() >= 12
                    ? item.getSecurityHash().substring(0, 12) + "..."
                    : "0xVERIFIED";
        }
    }
}


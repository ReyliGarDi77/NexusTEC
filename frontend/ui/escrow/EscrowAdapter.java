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
        public String statusText;

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
            holder.idText = "ID: " + item.getTransactionId();
            holder.amountText = item.getCreditsAmount() + " Halcón Credits";
            holder.statusText = item.getStatus().name();
        }
    }
}

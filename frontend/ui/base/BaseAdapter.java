package ui.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador base para listas en Android siguiendo el patrón ViewHolder de RecyclerView.
 *
 * @param <T>  Tipo de datos del elemento de la lista.
 * @param <VH> Tipo del ViewHolder.
 */
public abstract class BaseAdapter<T, VH extends BaseAdapter.BaseViewHolder> {

    public interface OnItemClickListener<T> {
        void onItemClick(T item, int position);
    }

    protected final List<T> items = new ArrayList<>();
    protected OnItemClickListener<T> clickListener;

    public static abstract class BaseViewHolder {
        public final int itemViewId;

        public BaseViewHolder(int itemViewId) {
            this.itemViewId = itemViewId;
        }
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.clickListener = listener;
    }

    public void updateList(List<T> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        if (position >= 0 && position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    public abstract VH onCreateViewHolder(int position);

    public abstract void onBindViewHolder(VH holder, int position);

    public void notifyDataSetChanged() {
        // En Android real, notifica al RecyclerView para redibujar
    }
}


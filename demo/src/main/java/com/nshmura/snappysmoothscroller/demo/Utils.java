package com.nshmura.snappysmoothscroller.demo;

import android.support.v7.widget.RecyclerView;

public class Utils {
    public static void resetSelected(RecyclerView recyclerView, int position) {

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int count = recyclerView.getAdapter().getItemCount();
        for (int i = 0; i < count; i++) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
            if (holder != null) {
                holder.itemView.setSelected(i == position);
            } else {
                adapter.notifyItemChanged(i);
            }
        }
    }
}

package com.nshmura.snappysmoothscroller.demo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class BasicAdapter extends RecyclerView.Adapter<BasicAdapter.ViewHolder> {

    private int selectedPosition;
    private List<String> items = new ArrayList<>();
    private OnItemClickListener listener;

    public BasicAdapter() {
        for(int i = 0; i < 100; i++) {
            items.add(String.format("%s", i));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_item_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClickItem(ViewHolder holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView titleView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleView = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setSelected(true);
                    if (listener != null) {
                        listener.onClickItem(ViewHolder.this);
                    }
                }
            });
        }

        public void bind(String title) {
            titleView.setText(title);
            itemView.setSelected(selectedPosition == getAdapterPosition());
        }
    }
}

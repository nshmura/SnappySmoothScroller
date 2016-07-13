package com.nshmura.snappysmoothscroller.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private int layoutRes;
    private int selectedPosition;
    private OnItemClickListener listener;
    private List<String> items = new ArrayList<>();
    private List<Integer> dimens = new ArrayList<>();

    private boolean randomHeight;
    private boolean randomWidth;

    public DetailAdapter(Context context) {
        dimens.add(context.getResources().getDimensionPixelSize(R.dimen.row_detail_size1));
        dimens.add(context.getResources().getDimensionPixelSize(R.dimen.row_detail_size2));
        dimens.add(context.getResources().getDimensionPixelSize(R.dimen.row_detail_size3));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutRes, parent, false);
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

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setRandomWidth() {
        randomWidth = true;
    }

    public void setRandomHeight() {
        randomHeight = true;
    }

    public void setVertical(boolean vertical) {
        if (vertical) {
            layoutRes = R.layout.row_item_vertical;
        } else {
            layoutRes = R.layout.row_item_horizontal;
        }
        for(int i = 0; i < 100; i++) {
            items.add(String.format("%s", i));
        }
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

            if (randomWidth) {
                itemView.setMinimumWidth(getRandomSize());
            }
            if (randomHeight) {
                itemView.setMinimumHeight(getRandomSize());
            }
        }

        private int getRandomSize() {
            int index = (int)(Math.random() * 3);
            return dimens.get(index);
        }
    }
}

package com.nshmura.snappysmoothscroller.demo;

import com.nshmura.snappysmoothscroller.LinearLayoutScrollVectorDetector;
import com.nshmura.snappysmoothscroller.SnappySmoothScroller;
import com.nshmura.snappysmoothscroller.SnapType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class Basic2Activity extends BaseActivity {

    private RecyclerView recyclerView;
    private BasicAdapter adapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, Basic2Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        setupActionBar(DemoType.Basic2);

        adapter = new BasicAdapter();
        adapter.setListener(new BasicAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(BasicAdapter.ViewHolder holder) {
                moveTo(holder.getAdapterPosition());
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                SnappySmoothScroller scroller = new SnappySmoothScroller.Builder()
                        .setSnapType(SnapType.CENTER)
                        .setPosition(position)
                        .setScrollVectorDetector(new LinearLayoutScrollVectorDetector(this))
                        .build(recyclerView.getContext());

                startSmoothScroll(scroller);
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void moveTo(int position) {
        recyclerView.smoothScrollToPosition(position);
        adapter.setSelectedPosition(position);
        Utils.resetSelected(recyclerView, position);
    }
}

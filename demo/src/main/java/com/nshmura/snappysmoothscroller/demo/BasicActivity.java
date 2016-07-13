package com.nshmura.snappysmoothscroller.demo;

import com.nshmura.snappysmoothscroller.SnappyLinearLayoutManager;
import com.nshmura.snappysmoothscroller.SnapType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class BasicActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private BasicAdapter adapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, BasicActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        setupActionBar(DemoType.Basic);

        adapter = new BasicAdapter();
        adapter.setListener(new BasicAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(BasicAdapter.ViewHolder holder) {
                moveTo(holder.getAdapterPosition());
            }
        });

        SnappyLinearLayoutManager layoutManager = new SnappyLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSnapType(SnapType.CENTER);

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

package com.nshmura.snappysmoothscroller.app;

import com.nshmura.snappysmoothscroller.R;
import com.nshmura.snappysmoothscroller.SnappyLinearLayoutManager;
import com.nshmura.snappysmoothscroller.SnappyType;

import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SnappyLinearLayoutManager layoutManager;
    private DemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupList();
        setupPositions();
        setupInterpolator();
        setupGotoButtons();
    }

    private void setupList() {
        layoutManager = new SnappyLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSnapPadding(getResources().getDimensionPixelOffset(R.dimen.snap_padding));
        layoutManager.setSeekDuration(1000);

        adapter = new DemoAdapter();
        adapter.setListener(new DemoAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(DemoAdapter.ViewHolder holder) {
                moveTo(holder.getAdapterPosition());
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setupPositions() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.positions_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_position);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        layoutManager.setSnapType(SnappyType.START);
                        break;
                    case 1:
                        layoutManager.setSnapType(SnappyType.CENTER);
                        break;
                    case 2:
                        layoutManager.setSnapType(SnappyType.END);
                        break;
                    case 3:
                        layoutManager.setSnapType(SnappyType.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupInterpolator() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.interpolator_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_interpolator);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        layoutManager.setSnapDuration(500);
                        layoutManager.setSnapInterpolator(new DecelerateInterpolator());
                        break;
                    case 1:
                        layoutManager.setSnapDuration(500);
                        layoutManager.setSnapInterpolator(new FastOutSlowInInterpolator());
                        break;
                    case 2:
                        layoutManager.setSnapDuration(700);
                        layoutManager.setSnapInterpolator(new OvershootInterpolator());
                        break;
                    case 3:
                        layoutManager.setSnapDuration(1100);
                        layoutManager.setSnapInterpolator(new BounceInterpolator());
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupGotoButtons() {
        findViewById(R.id.go_up30).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Math.max(0, adapter.getSelectedPosition() - 30);
                moveTo(position);
            }
        });

        findViewById(R.id.go_down30).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Math.min(adapter.getItemCount() - 1, adapter.getSelectedPosition() + 30);
                moveTo(position);
            }
        });
    }

    private void moveTo(int position) {
        recyclerView.smoothScrollToPosition(position);

        DemoAdapter.ViewHolder holder;
        holder = (DemoAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(adapter.getSelectedPosition());
        if (holder != null) {
            holder.itemView.setSelected(false);
        }

        holder = (DemoAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (holder != null) {
            holder.itemView.setSelected(true);
        }

        adapter.setSelectedPosition(position);
    }
}

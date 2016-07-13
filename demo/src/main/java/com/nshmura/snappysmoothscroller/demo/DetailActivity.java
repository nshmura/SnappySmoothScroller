package com.nshmura.snappysmoothscroller.demo;

import com.nshmura.snappysmoothscroller.SnapType;
import com.nshmura.snappysmoothscroller.SnappyGridLayoutManager;
import com.nshmura.snappysmoothscroller.SnappyLayoutManager;
import com.nshmura.snappysmoothscroller.SnappyLinearLayoutManager;
import com.nshmura.snappysmoothscroller.SnappyStaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DetailActivity extends BaseActivity {

    private static final String EXTRA_DEMO = "EXTRA_DEMO";

    private DemoType demoType;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DetailAdapter adapter;

    public static void start(Context context, DemoType demoType) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(EXTRA_DEMO, demoType);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        demoType = (DemoType) getIntent().getSerializableExtra(EXTRA_DEMO);
        setupActionBar(demoType);

        setupList();
        setupPositions();
        setupInterpolator();
        setupGotoButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_option:
                View optionPos = findViewById(R.id.option_position);
                View optionInterp = findViewById(R.id.option_interpolator);

                if (optionPos.getVisibility() == View.VISIBLE) {
                    optionPos.setVisibility(View.GONE);
                    optionInterp.setVisibility(View.GONE);
                } else {
                    optionPos.setVisibility(View.VISIBLE);
                    optionInterp.setVisibility(View.VISIBLE);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupList() {

        adapter = new DetailAdapter(this);
        adapter.setListener(new DetailAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(DetailAdapter.ViewHolder holder) {
                moveTo(holder.getAdapterPosition());
            }
        });
        switch (demoType) {
            case LinearVertical:
                layoutManager = new SnappyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                adapter.setVertical(true);
                break;

            case LinearHorizontal:
                layoutManager = new SnappyLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                adapter.setVertical(false);
                break;

            case GridVertival:
                layoutManager = new SnappyGridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
                adapter.setVertical(true);
                break;

            case GridHorizontal:
                layoutManager = new SnappyGridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
                adapter.setVertical(false);
                break;

            case StaggeredGridVertival:
                layoutManager = new SnappyStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                adapter.setVertical(true);
                adapter.setRandomHeight();
                break;

            case StaggeredGridHorizontal:
                layoutManager = new SnappyStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
                adapter.setVertical(false);
                adapter.setRandomWidth();
                break;
        }

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
        spinner.setSelection(1);//center
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ((SnappyLayoutManager) layoutManager).setSnapType(SnapType.START);
                        break;
                    case 1:
                        ((SnappyLayoutManager) layoutManager).setSnapType(SnapType.CENTER);
                        break;
                    case 2:
                        ((SnappyLayoutManager) layoutManager).setSnapType(SnapType.END);
                        break;
                    case 3:
                        ((SnappyLayoutManager) layoutManager).setSnapType(SnapType.VISIBLE);
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
                        ((SnappyLayoutManager) layoutManager).setSnapDuration(500);
                        ((SnappyLayoutManager) layoutManager).setSnapInterpolator(new DecelerateInterpolator());
                        break;
                    case 1:
                        ((SnappyLayoutManager) layoutManager).setSnapDuration(500);
                        ((SnappyLayoutManager) layoutManager).setSnapInterpolator(new FastOutSlowInInterpolator());
                        break;
                    case 2:
                        ((SnappyLayoutManager) layoutManager).setSnapDuration(700);
                        ((SnappyLayoutManager) layoutManager).setSnapInterpolator(new OvershootInterpolator());
                        break;
                    case 3:
                        ((SnappyLayoutManager) layoutManager).setSnapDuration(1100);
                        ((SnappyLayoutManager) layoutManager).setSnapInterpolator(new BounceInterpolator());
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
        adapter.setSelectedPosition(position);
        Utils.resetSelected(recyclerView, position);
    }
}

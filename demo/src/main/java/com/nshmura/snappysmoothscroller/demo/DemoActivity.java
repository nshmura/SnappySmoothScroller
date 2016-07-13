package com.nshmura.snappysmoothscroller.demo;

import com.nshmura.snappysmoothscroller.SnappyLinearLayoutManager;
import com.nshmura.snappysmoothscroller.SnapType;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;

import jp.co.cyberagent.android.gpuimage.GPUImage;

public class DemoActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private DemoAdapter adapter;
    private GLSurfaceView imageView;
    private GPUImage gpuImage;
    private Bitmap srcBitmap;

    public static void start(Context context) {
        Intent starter = new Intent(context, DemoActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        imageView = (GLSurfaceView) findViewById(R.id.filtered_image);
        setupActionBar(DemoType.Demo);

        adapter = new DemoAdapter(getApplicationContext());
        adapter.setListener(new DemoAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(DemoAdapter.ViewHolder holder) {
                changeFilter(holder.getAdapterPosition());
            }
        });

        SnappyLinearLayoutManager layoutManager = new SnappyLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setSnapType(SnapType.CENTER);
        layoutManager.setSnapPadding(getResources().getDimensionPixelSize(R.dimen.demo_snap_padding));
        layoutManager.setSnapInterpolator(new OvershootInterpolator());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 2;
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sample, opts);
        gpuImage = new GPUImage(this);
        gpuImage.setImage(srcBitmap);
        gpuImage.setGLSurfaceView(imageView);
    }

    private void changeFilter(int position) {
        recyclerView.smoothScrollToPosition(position);
        adapter.setSelectedPosition(position);
        Utils.resetSelected(recyclerView, position);

        DemoAdapter.Item item = adapter.getItem(position);
        gpuImage.setFilter(item.filter);
    }
}

package com.nshmura.snappysmoothscroller.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.GPUImagePosterizeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;

class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.ViewHolder> {

    private int selectedPosition;
    private List<Item> items = new ArrayList<>();
    private OnItemClickListener listener;

    public DemoAdapter(Context context) {

        items.add(new Item("Contrast", new GPUImageContrastFilter(2.0f)));
        items.add(new Item("Invert", new GPUImageColorInvertFilter()));
        items.add(new Item("Hue", new GPUImageHueFilter(90.0f)));
        items.add(new Item("Gamma", new GPUImageGammaFilter(2.0f)));
        items.add(new Item("Sepia", new GPUImageSepiaFilter()));
        items.add(new Item("Grayscale", new GPUImageGrayscaleFilter()));
        items.add(new Item("Emboss", new GPUImageEmbossFilter()));
        items.add(new Item("Posterize", new GPUImagePosterizeFilter()));
        items.add(new Item("Blur", new GPUImageGaussianBlurFilter()));
        items.add(new Item("Sketch", new GPUImageSketchFilter()));
        items.add(new Item("TOON", new GPUImageToonFilter()));

        createFilteredImage(context);
    }

    private void createFilteredImage(final Context context) {
        final Handler handler = new Handler();

        Executors.newCachedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 4;

                Bitmap src = BitmapFactory.decodeResource(context.getResources(), R.drawable.sample, opts);

                for(int i = 0; i < items.size(); i++) {
                    final Item item = items.get(i);

                    GPUImage gpuImage = new GPUImage(context);
                    gpuImage.setImage(src);
                    gpuImage.setFilter(item.filter);

                    final Bitmap result = gpuImage.getBitmapWithFilterApplied();
                    final int index = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            item.bitmap = result;
                            notifyItemChanged(index);
                        }
                    });
                }

                src.recycle();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_demo, parent, false);
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

    public Item getItem(int position) {
        return items.get(position);
    }

    public interface OnItemClickListener {
        void onClickItem(ViewHolder holder);
    }

    public class Item {
        String title;
        GPUImageFilter filter;
        Bitmap bitmap;

        public Item(String title, GPUImageFilter filter) {
            this.title = title;
            this.filter = filter;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView titleView;
        public final ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleView = (TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.image);

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

        public void bind(Item item) {
            titleView.setText(item.title);
            itemView.setSelected(selectedPosition == getAdapterPosition());
            imageView.setImageBitmap(item.bitmap);
        }
    }
}

package com.chengzi.app.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nevermore.oceans.uits.ImageLoader;
import com.chengzi.app.R;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;

import java.util.ArrayList;
import java.util.Arrays;

import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_INDEX;
import static com.chengzi.app.ui.common.activity.PictureSwitcherActivity.PICTURE_URLS;

public class NineGridView2 extends RecyclerView {

    public NineGridView2(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public NineGridView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NineGridView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }

    public static class NineGridAdapter extends Adapter<NineGridViewHolder> {

        private static final int ITEM_TYPE_NORMAL = 0;
        private static final int ITEM_TYPE_ADD_PIC = 1;

        private OnAddClickListener listener;

        private @DrawableRes
        int addPic;
        private @DrawableRes
        int delPic;
        private ArrayList<String> data = new ArrayList<>();

        public NineGridAdapter(int addPic, int delPic, ArrayList<String> data) {
            this.addPic = addPic;
            this.delPic = delPic;
            this.data = data;
        }

        public NineGridAdapter(int addPic, int delPic) {
            this.addPic = addPic;
            this.delPic = delPic;
        }

        public void setData(ArrayList<String> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        public void addItem(ArrayList<String> items) {
            int start = data.size();
            this.data.addAll(items);
//            notifyItemRangeInserted(start, items.size());
            notifyDataSetChanged();
        }

        public void addItem(String... items) {
            int start = data.size();
            this.data.addAll(Arrays.asList(items));

//            notifyItemRangeInserted(start, items.length);
            notifyDataSetChanged();
        }

        public void remoteItem(String item) {
            if (item == null) {
                throw new IllegalArgumentException();
            }
            int index = data.indexOf(item);
            this.data.remove(item);
//            notifyItemRemoved(index);
            notifyDataSetChanged();
        }

        public void setListener(OnAddClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public NineGridViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.item_nine_grid2, viewGroup, false);
            return new NineGridViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull NineGridViewHolder viewHolder, int i) {
            Context context = viewHolder.itemView.getContext();
            viewHolder.root.setAspectRatio(1.0f);
            int itemViewType = getItemViewType(i);
            viewHolder.btnDel.setImageResource(delPic);
            if (itemViewType == ITEM_TYPE_NORMAL) {
                viewHolder.btnDel.setVisibility(View.VISIBLE);
                String item = data.get(i);
//                ImagePicker.getInstance()
//                        .getImageLoader()
//                        .displayImage(context, item, viewHolder.imageView);
                ImageLoader.loadImage(viewHolder.imageView, item, addPic);

                viewHolder.imageView.setOnClickListener(v -> {
                    context.startActivity(new Intent(context, PictureSwitcherActivity.class)
                            .putExtra(PICTURE_URLS, data)
                            .putExtra(PICTURE_INDEX, i)
                    );

                });

            } else {
                viewHolder.imageView.setImageResource(addPic);
                viewHolder.btnDel.setVisibility(View.GONE);
                viewHolder.imageView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onClick(v);
                    }
                });
            }

            viewHolder.btnDel.setOnClickListener(v -> {
//                data.remove(i);
//                this.notifyItemRemoved(i);
//                notifyDataSetChanged();
                try {
                    int position = viewHolder.getAdapterPosition();
                    data.remove(position);
                    notifyItemRemoved(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (data == null || data.isEmpty()) {
                return 1;       ///----->
            }
            if (data.size() < 9) {
                return data.size() + 1;
            }
            return data.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (data.size() >= 9) {
                return ITEM_TYPE_NORMAL;
            } else {
                if (position < data.size()) {
                    return ITEM_TYPE_NORMAL;
                } else {
                    return ITEM_TYPE_ADD_PIC;
                }
            }
        }
    }

    static class NineGridViewHolder extends ViewHolder {
        AspectRatioFrameLayout root;
        ImageView imageView;
        ImageView btnDel;

        public NineGridViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            imageView = itemView.findViewById(R.id.image_item);
            btnDel = itemView.findViewById(R.id.btn_del);
        }
    }

    public static class NineItem {
        private String localPath;  //  SD卡路径
        private String url;     // 上传后的网络路径
        private int type;  //  1：图片 2：视频

        public NineItem() {
        }

        public NineItem(String localPath) {
            this.localPath = localPath;
        }

        public NineItem(String localPath, int type) {
            this.localPath = localPath;
            this.type = type;
        }

        public String getLocalPath() {
            return localPath;
        }

        public void setLocalPath(String localPath) {
            this.localPath = localPath;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public interface OnAddClickListener {
        void onClick(View view);
    }
}

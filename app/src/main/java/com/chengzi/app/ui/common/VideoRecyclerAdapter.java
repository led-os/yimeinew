package com.chengzi.app.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.SuperCheckBox;
import com.chengzi.app.R;
import com.chengzi.app.ui.common.activity.VideoPlayActivity;
import com.chengzi.app.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.VideoHolder> {

    private List<VideoItem> mList = new ArrayList<>();

    private int mImageSize;               //每个条目的大小
    private Activity mActivity;
    private ImagePicker imagePicker;
    private VideoHolder previousSelected;


    public VideoRecyclerAdapter(Activity activity) {
        mActivity = activity;
        mImageSize = Utils.getImageItemWidth(mActivity);
        imagePicker = ImagePicker.getInstance();
    }

    public void refreshData(List<VideoItem> data) {
        mList.clear();
        mList.addAll(data);
        notifyDataSetChanged();
    }

    public VideoItem getSelectedItem(){
        if (previousSelected != null) {
            int adapterPosition = previousSelected.getAdapterPosition();
            return mList.get(adapterPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(R.layout.hand_adapter_image_list_item, parent, false);
        return new VideoHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public VideoItem getItem(int position){
        return mList.get(position);
    }

     class VideoHolder extends RecyclerView.ViewHolder {

        View rootView;
        ImageView ivThumb;
        View mask;
        SuperCheckBox cbCheck;
        TextView durationText;

        VideoHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            ivThumb =  itemView.findViewById(R.id.iv_thumb);
            mask = itemView.findViewById(R.id.mask);
            cbCheck =  itemView.findViewById(R.id.cb_check);
            itemView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mImageSize)); //让图片是个正方形
            durationText = itemView.findViewById(R.id.tv_duration);
        }

         void bind(final int position){
             final VideoItem videoItem = getItem(position);
             ivThumb.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Context context = v.getContext();
                     context.startActivity(new Intent(context,VideoPlayActivity.class)
                     .putExtra(VideoPlayActivity.VIDEO_PATH,videoItem.path));
                 }
             });
             cbCheck.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     mask.setVisibility(View.VISIBLE);
                     if (previousSelected != null) {
                         previousSelected.setChecked(false);
                     }
                     previousSelected = VideoHolder.this;
                 }
             });

             imagePicker.getImageLoader().displayImage(mActivity, videoItem.path, ivThumb, mImageSize, mImageSize); //显示图片

             durationText.setText(DateUtils.timeParse(videoItem.duration));
         }

         public void setChecked(boolean checked){
             cbCheck.setChecked(checked);
             mask.setVisibility(checked?View.VISIBLE:View.GONE);
         }

     }
}

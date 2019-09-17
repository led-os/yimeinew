package com.chengzi.app.ui.common;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class VideoDataSource implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String DURATION = "duration";
    private static final String ORDER_BY = MediaStore.Files.FileColumns._ID + " DESC";
    private boolean loadSuccess;
    private OnVideosLoadedListener mOnVideosLoadedListener;

    // 媒体文件数据库字段
    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,
            DURATION};

    // 查询条件(音视频)
    private static String getSelectionArgsForSingleMediaCondition() {
        return MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0";
    }

    private static String[] getSelectionArgsForSingleMediaType(int mediaType) {
        return new String[]{String.valueOf(mediaType)};
    }

//    private final String[] projection = new String[]{
//            MediaStore.Video.VideoColumns.DATA,
//            MediaStore.Video.VideoColumns.DISPLAY_NAME,
//            MediaStore.Video.VideoColumns.DURATION,
//            MediaStore.Video.VideoColumns.MIME_TYPE,
//            MediaStore.Video.VideoColumns._ID,
//    };

    private FragmentActivity mActivity;

    public VideoDataSource(OnVideosLoadedListener onVideosLoadedListener, FragmentActivity activity) {
        mOnVideosLoadedListener = onVideosLoadedListener;
        mActivity = activity;
        LoaderManager loaderManager = mActivity.getSupportLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String video_condition = getSelectionArgsForSingleMediaCondition();
        String[] MEDIA_TYPE_VIDEO = getSelectionArgsForSingleMediaType(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO);
        return new CursorLoader(mActivity, QUERY_URI, PROJECTION,
                video_condition, MEDIA_TYPE_VIDEO, ORDER_BY);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || loadSuccess) {
            return;
        }

        List<VideoItem> items = new ArrayList<>();
        int count = cursor.getCount();
        Log.d("aaa", "onLoadFinished: " + count);
        cursor.moveToFirst();
        // count = 0 时候 取值会导致数组下标越界
        if (count > 0) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(PROJECTION[0]));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(PROJECTION[1]));
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(PROJECTION[2]));
                String mineType = cursor.getString(cursor.getColumnIndexOrThrow(PROJECTION[3]));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(PROJECTION[6]));

                VideoItem item = new VideoItem(id, path, displayName, duration, mineType);
                items.add(item);

                Log.d("aaa", "onLoadFinished: " + item);

            } while (cursor.moveToNext());
        }


        if (mOnVideosLoadedListener != null) {
            mOnVideosLoadedListener.onVideosLoaded(items);
        }

        loadSuccess = true;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }


    public interface OnVideosLoadedListener {
        void onVideosLoaded(List<VideoItem> items);
    }
}

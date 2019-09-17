package com.chengzi.app.ui.common;

import java.io.Serializable;
import java.util.Objects;

public class VideoItem implements Serializable{

    public long id;
    public String path;
    public String displayName;
    public long duration;
    public String mineType;

    public VideoItem() {
    }

    public VideoItem(long id, String path, String displayName, long duration, String mineType) {
        this.id = id;
        this.path = path;
        this.displayName = displayName;
        this.duration = duration;
        this.mineType = mineType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoItem)) return false;
        VideoItem videoItem = (VideoItem) o;
        return id == videoItem.id &&
                duration == videoItem.duration &&
                Objects.equals(path, videoItem.path) &&
                Objects.equals(displayName, videoItem.displayName) &&
                Objects.equals(mineType, videoItem.mineType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, path, displayName, duration, mineType);
    }

    @Override
    public String toString() {
        return "VideoItem{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", displayName='" + displayName + '\'' +
                ", duration=" + duration +
                ", mineType='" + mineType + '\'' +
                '}';
    }
}

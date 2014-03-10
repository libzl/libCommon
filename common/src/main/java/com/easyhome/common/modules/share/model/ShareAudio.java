package com.easyhome.common.modules.share.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;

import com.easyhome.common.utils.TextUtil;
import com.tencent.mm.sdk.platformtools.Util;

/**
 * 音频数据源
 *
 * @author zhoulu
 * @date 13-12-11
 */
public class ShareAudio extends BaseShareObject {

    Bitmap albumImage;
    String trackTitle;
    String description;
    String mediaUrl;
    String lowBandMediaUrl;
    String detailUrl;

    int duration;

    public ShareAudio() {
    }
    /**
     * 构造
     * @param albumImage 专辑图片
     * @param trackTitle 歌曲名
     * @param description 描述
     * @param mediaUrl 试听url
     * @param lowBandMediaUrl 低码率试听url
     * @param detailUrl 详情url
     * @param duration 音频时长
     */
    public ShareAudio(Bitmap albumImage, String trackTitle, String description, String mediaUrl, String lowBandMediaUrl, String detailUrl, int duration) {
        this.albumImage = albumImage;
        this.trackTitle = trackTitle;
        this.description = description;
        this.mediaUrl = mediaUrl;
        this.lowBandMediaUrl = lowBandMediaUrl;
        this.duration = duration;
        this.detailUrl = detailUrl;
    }

    public ShareAudio(Parcel source) {
        super(source);
        int length = source.readInt();
        if (length > 0) {
            byte[] image = new byte[length];
            source.readByteArray(image);
            albumImage = BitmapFactory.decodeByteArray(image, 0, length);
        }
        trackTitle = source.readString();
        description = source.readString();
        mediaUrl = source.readString();
        lowBandMediaUrl = source.readString();
        detailUrl = source.readString();
        duration = source.readInt();
    }

    public void setAlbumImage(Bitmap albumImage) {
        this.albumImage = albumImage;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setLowBandMediaUrl(String lowBandMediaUrl) {
        this.lowBandMediaUrl = lowBandMediaUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public TYPE getType() {
        return TYPE.TYPE_MUSIC;
    }

    @Override
    public String getMediaUrl() {
        if (TextUtil.isEmpty(mediaUrl)) {
            return lowBandMediaUrl;
        }
        return mediaUrl;
    }

    @Override
    public String getLowBandMediaUrl() {
        return lowBandMediaUrl;
    }

    @Override
    public String getTitle() {
        return trackTitle;
    }

    @Override
    public String getSecondTitle() {
        return description;
    }

    @Override
    public Bitmap getThumbnail() {
        return albumImage;
    }

    @Override
    public int getContentSize() {
        return duration;
    }

    @Override
    public String getRedirectUrl() {
        return detailUrl;
    }

    public static Creator<ShareAudio> CREATOR = new Creator<ShareAudio>() {
        @Override
        public ShareAudio createFromParcel(Parcel source) {
            return new ShareAudio(source);
        }

        @Override
        public ShareAudio[] newArray(int size) {
            return new ShareAudio[0];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        if (albumImage != null && !albumImage.isRecycled()) {
            byte[] image = Util.bmpToByteArray(albumImage, true);
            dest.writeInt(image.length);
            dest.writeByteArray(image);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(trackTitle);
        dest.writeString(description);
        dest.writeString(mediaUrl);
        dest.writeString(lowBandMediaUrl);
        dest.writeString(detailUrl);
        dest.writeInt(duration);
    }

}

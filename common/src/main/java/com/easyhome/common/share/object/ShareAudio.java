package com.easyhome.common.share.object;


import android.graphics.Bitmap;

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

    @Override
    public TYPE getType() {
        return TYPE.TYPE_MUSIC;
    }

    @Override
    public String getMediaUrl() {
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

}

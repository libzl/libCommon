package com.easyhome.common.share.object;

import android.graphics.Bitmap;

/**
 * 视频
 *
 * @author zhoulu
 * @date 13-12-11
 */
public class ShareVideo extends BaseShareObject {

    int duration;
    Bitmap videoImage;
    String trackTitle;
    String description;
    String mediaUrl;
    String lowBandMediaUrl;
    String detailUrl;

    /**
     * 构造
     * @param videoImage MV图片
     * @param trackTitle MV名
     * @param description 描述
     * @param mediaUrl 播放url
     * @param lowBandMediaUrl 低码率播放url
     * @param detailUrl 详情url
     * @param duration 音频时长
     */
    public ShareVideo(Bitmap videoImage, String trackTitle, String description, String mediaUrl, String lowBandMediaUrl, String detailUrl, int duration) {
        this.videoImage = videoImage;
        this.trackTitle = trackTitle;
        this.description = description;
        this.mediaUrl = mediaUrl;
        this.lowBandMediaUrl = lowBandMediaUrl;
        this.duration = duration;
        this.detailUrl = detailUrl;
    }
    /**
     * 类型
     *
     * @return
     */
    @Override
    public TYPE getType() {
        return TYPE.TYPE_VIDEO;
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
        return videoImage;
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

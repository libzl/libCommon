package com.easyhome.common.share.object;

import android.graphics.Bitmap;

/**
 * 图片数据源
 *
 * @author zhoulu
 * @date 13-12-11
 */
public class ShareImage extends BaseShareObject {

    Bitmap mShareImage;
    String[] mShareImageUrl;

    /**
     * 构造
     * @param shareImage 图片
     */
    public ShareImage(Bitmap shareImage) {
        mShareImage = shareImage;
    }

    /**
     * 构造
     * @param shareImageUrl 图片url
     */
    public ShareImage(String[] shareImageUrl) {
        mShareImageUrl = shareImageUrl;
    }

    /**
     * 类型
     *
     * @return
     */
    @Override
    public TYPE getType() {
        return TYPE.TYPE_IMAGE;
    }

    @Override
    public Bitmap getThumbnail() {
        return mShareImage;
    }

    @Override
    public String[] getThumbnailUrl() {
        return mShareImageUrl;
    }

    @Override
    public String getMessage() {
        return "share a image";
    }
}

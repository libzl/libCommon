package com.easyhome.common.modules.share.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;

import com.tencent.mm.sdk.platformtools.Util;

/**
 * 数据源基类
 *
 * @author zhoulu
 * @date 13-12-11
 */
public abstract class BaseShareObject implements IShareObject {

    private String title = "";
    private String secondTitle = "";
    private String message = "";
    private Bitmap bitmap = null;
    private String redirectUrl;
    private String[] thumbnailUrls;

    public BaseShareObject() {
    }

    public BaseShareObject(Parcel source) {
        title = source.readString();
        secondTitle = source.readString();
        message = source.readString();
        int length = source.readInt();
        if (length > 0) {
            byte[] image = new byte[length];
            source.readByteArray(image);
            bitmap = BitmapFactory.decodeByteArray(image, 0, length);
        }
        redirectUrl = source.readString();
        int N = source.readInt();
        if (N > 0) {
            thumbnailUrls = new String[N];
            source.readStringArray(thumbnailUrls);
        }
    }
    /**
     * 设置标题
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * 设置副标题
     * @param secondTitle
     */
    public void setSecondTitle(String secondTitle) {
        this.secondTitle = secondTitle;
    }
    /**
     * 设置评论描述
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public void setThumbnail(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setThumbnailUrls(String[] thumbnailUrls) {
        this.thumbnailUrls = thumbnailUrls;
    }
    /**
     * 获得一个媒体链接：音乐、视频、网址
     *
     * @return
     */
    @Override
    public String getMediaUrl() {
        return "";
    }

    /**
     * 获得一个低品质的媒体链接：音乐、视频、网址
     *
     * @return
     */
    @Override
    public String getLowBandMediaUrl() {
        return "";
    }

    /**
     * 主标题：歌曲名称等
     *
     * @return
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * 副标题：歌手名等（可选）
     *
     * @return
     */
    @Override
    public String getSecondTitle() {
        return secondTitle;
    }

    /**
     * 评论描述
     *
     * @return
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 图片
     *
     * @return
     */
    @Override
    public Bitmap getThumbnail() {
        return bitmap;
    }

    @Override
    public String[] getThumbnailUrl() {
        return thumbnailUrls;
    }

    @Override
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * 数据大小：音频长度，视频长度
     *
     * @return
     */
    @Override
    public int getContentSize() {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(secondTitle);
        dest.writeString(message);

        if (bitmap != null && !bitmap.isRecycled()) {
            byte[] image = Util.bmpToByteArray(bitmap, true);
            dest.writeInt(image.length);
            dest.writeByteArray(image);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(redirectUrl);
        dest.writeInt(thumbnailUrls == null ? -1 : thumbnailUrls.length);
        if (thumbnailUrls != null && thumbnailUrls.length != 0) {
            dest.writeStringArray(thumbnailUrls);
        }
    }
}

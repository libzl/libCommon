package com.easyhome.common.share.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;

import com.tencent.mm.sdk.platformtools.Util;

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

    public ShareImage(Parcel source) {
        super(source);
        int length = source.readInt();
        if (length > 0) {
            byte[] image = new byte[length];
            source.readByteArray(image);
            mShareImage = BitmapFactory.decodeByteArray(image, 0, length);
        }

        int N = source.readInt();
        if (N > 0) {
            mShareImageUrl = new String[N];
            source.readStringArray(mShareImageUrl);
        }
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

    public static Creator<ShareImage> CREATOR = new Creator<ShareImage>() {
        @Override
        public ShareImage createFromParcel(Parcel source) {
            return new ShareImage(source);
        }

        @Override
        public ShareImage[] newArray(int size) {
            return new ShareImage[0];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        if (mShareImage != null && !mShareImage.isRecycled()) {
            byte[] image = Util.bmpToByteArray(mShareImage, true);
            dest.writeInt(image.length);
            dest.writeByteArray(image);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(mShareImageUrl == null ? -1 : mShareImageUrl.length);
        if (mShareImageUrl != null && mShareImageUrl.length != 0) {
            dest.writeStringArray(mShareImageUrl);
        }
    }
}

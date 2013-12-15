package com.easyhome.common.share.option;

import android.app.Activity;
import android.os.Bundle;

import com.easyhome.common.R;
import com.easyhome.common.share.IShareObject;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

/**
 * QQ空间
 */
public class QQZone extends QQConnect {

    @Override
    public int getIcon() {
        return R.drawable.bt_share_qqzone;
    }

    @Override
    public String getName() {
        return getString(R.string.action_qqzone);
    }

    @Override
    public void onShare(IShareObject... shareObject) {
        if (shareObject == null || shareObject.length == 0) {
            return;
        }

        //QZONE只支持发表图文
        IShareObject object = shareObject[0];
        final Bundle params = new Bundle();
        ArrayList<String> imageUrls = new ArrayList<String>();
        if (object.getThumbnailUrl() != null) {
            for (String imageUrl : object.getThumbnailUrl()) {
                imageUrls.add(imageUrl);
            }
        }

        switch (object.getType()) {
            case TYPE_IMAGE:
                // 支持传多个imageUrl
                params.putString(Tencent.SHARE_TO_QQ_TITLE, "test to qqzone of title");//object.getMessage()
                params.putString(Tencent.SHARE_TO_QQ_SUMMARY, "test to qqzone of summary");//object.getSecondTitle()
                params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com");//object.getMediaUrl()
                break;
        }

        params.putStringArrayList(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrls);


        mTencent.shareToQzone((Activity) getContext(), params, this);
    }

    @Override
    public boolean validateCheck(IShareObject... shareObject) {
        return false;
    }

}

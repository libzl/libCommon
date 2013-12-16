package com.easyhome.common.share.option;

import android.app.Activity;
import android.os.Bundle;

import com.easyhome.common.R;
import com.easyhome.common.share.ShareConfiguration;
import com.easyhome.common.share.object.IShareObject;
import com.easyhome.common.utils.TextUtil;
import com.easyhome.common.utils.URIUtil;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

/**
 * QQ空间
 */
public class QQZone extends QQConnect {

    @Override
    public int getIcon() {
        return ShareConfiguration.QQCONNECT.QQZONE_ICON_ID;
    }

    @Override
    public String getName() {
        return getString(ShareConfiguration.QQCONNECT.QQZONE_NAME_ID);
    }

    @Override
    public void onShare(IShareObject... shareObject) {
        if (shareObject == null || shareObject.length == 0) {
            return;
        }

        IShareObject object = shareObject[0];
        final Bundle params = new Bundle();
        ArrayList<String> imageUrls = new ArrayList<String>();
        if (object.getThumbnailUrl() != null) {
            for (String imageUrl : object.getThumbnailUrl()) {
                imageUrls.add(imageUrl);
            }
        }
        params.putString(Tencent.SHARE_TO_QQ_TITLE, object.getTitle());
        params.putString(Tencent.SHARE_TO_QQ_SUMMARY, object.getSecondTitle());
        params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, object.getRedirectUrl());

        params.putStringArrayList(Tencent.SHARE_TO_QQ_IMAGE_URL, imageUrls);


        mTencent.shareToQzone((Activity) getContext(), params, this);
    }

    @Override
    public boolean validateCheck(IShareObject... shareObjects) {
        if (shareObjects == null || shareObjects.length == 0) {
            logE("数据为NULL或者空");
            notifyEvent(getString(R.string.share_invalidate_datas));
            return false;
        }

        for (IShareObject shareObject : shareObjects) {
            if (shareObject == null) {
                notifyEvent(getString(R.string.share_invalidate_datas));
                return false;
            }

            if (TextUtil.isEmpty(shareObject.getTitle())) {
                notifyEvent(getString(R.string.share_image_title_empty));
                return false;
            } else if(TextUtil.isEmpty(shareObject.getSecondTitle())){
                notifyEvent(getString(R.string.share_image_desc_empty));
                return false;
            } else if(TextUtil.isEmpty(shareObject.getRedirectUrl())){
                notifyEvent(getString(R.string.share_image_invalid_detail_url));
                return false;
            } else {
                if (shareObject.getThumbnailUrl() != null) {
                    for (String imageUrl : shareObject.getThumbnailUrl()) {
                        if (TextUtil.isEmpty(imageUrl) || !URIUtil.isValidHttpUri(imageUrl)) {
                            notifyEvent(getString(R.string.share_image_empty));
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}

package com.easyhome.common.share.logic;

import android.content.Context;

import com.easyhome.common.share.ShareConfiguration;


/**
 * QQ空间
 */
public class QQZone extends QQFriends {

    public QQZone(Context context) {
        super(context);
    }


	@Override
    public int getIcon() {
        return ShareConfiguration.QQCONNECT.QQZONE_ICON_ID;
    }

    @Override
    public String getName() {
        return getString(ShareConfiguration.QQCONNECT.QQZONE_NAME_ID);
    }

    @Override
    protected boolean isUseQzone() {
        return true;
    }

}

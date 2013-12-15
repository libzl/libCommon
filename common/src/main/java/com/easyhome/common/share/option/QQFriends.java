package com.easyhome.common.share.option;

import android.app.Activity;
import android.os.Bundle;

import com.easyhome.common.R;
import com.easyhome.common.share.IShareObject;
import com.easyhome.common.share.ShareConfiguration;
import com.tencent.tauth.Tencent;

/**
 * QQ好友
 *
 * @author zhoulu
 * @date 13-12-13
 */
public class QQFriends extends QQConnect {

    private int mExtarFlag;

    @Override
    public int getIcon() {
        return R.drawable.bt_share_qq;
    }

    @Override
    public String getName() {
        return getString(R.string.action_qqfriends);
    }

    @Override
    public void onShare(IShareObject... shareObject) {

        if (shareObject == null || shareObject.length == 0) {
            return;
        }

        IShareObject object = shareObject[0];

        final Bundle params = new Bundle();
        int shareType = Tencent.SHARE_TO_QQ_TYPE_DEFAULT;
        switch (object.getType()) {
            case TYPE_IMAGE://只支持本地
                shareType = Tencent.SHARE_TO_QQ_TYPE_IMAGE;
                params.putString(Tencent.SHARE_TO_QQ_TITLE, "分享音乐,来自下豆瓣FM：The Chordettes" + object.getTitle());
                params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://douban.fm/?start=8508g3c27g-3&cid=-3" + object.getMediaUrl());
                params.putString(Tencent.SHARE_TO_QQ_SUMMARY, "豆瓣音乐-Lollipop <Stand By Me> 1990 字数不够？再加点？" + object.getSecondTitle());
                params.putString(Tencent.SHARE_TO_QQ_IMAGE_LOCAL_URL, "/storage/emulated/0/tencent/ReaderZone/Adv/1228_m.png");// + object.getThumbnailUrl()
                break;
            case TYPE_TEXT:
            case TYPE_WEBURL:
                params.putString(Tencent.SHARE_TO_QQ_TITLE, "分享音乐,来自下豆瓣FM：The Chordettes");// + object.getTitle()
                params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://douban.fm/?start=8508g3c27g-3&cid=-3");// + object.getMediaUrl()
                params.putString(Tencent.SHARE_TO_QQ_SUMMARY, "豆瓣音乐-Lollipop <Stand By Me> 1990 字数不够？再加点？");// + object.getSecondTitle()
                params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, "http://img3.douban.com/lpic/s3635685.jpg");// + object.getThumbnailUrl()
                break;
            case TYPE_MUSIC:
            case TYPE_VIDEO:
                shareType = Tencent.SHARE_TO_QQ_TYPE_AUDIO;
                params.putString(Tencent.SHARE_TO_QQ_TITLE, "不要说话" + object.getTitle());
                params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, "http://y.qq.com/i/song.html?songid=XXX&source=mobileQQ#wechat_redirect");// + object.getMediaUrl()
                params.putString(Tencent.SHARE_TO_QQ_SUMMARY, "专辑名：不想放手歌手名：陈奕迅" + object.getSecondTitle());
                params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/music/photo/mid_album_300/V/E/000J1pJ50cDCVE.jpg"); // + object.getThumbnailUrl()
                params.putString(Tencent.SHARE_TO_QQ_AUDIO_URL, "http://stream14.qqmusic.qq.com/30432451.mp3?key=ABD30A88B30BA76C1435598BC67F69EA741DE4082BF8E089&qqmusic_fromtag=15");//object.getMediaUrl()
                break;
        }

        params.putString(Tencent.SHARE_TO_QQ_APP_NAME, ShareConfiguration.APPNAME);
        params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE, shareType);

        if (!ShareConfiguration.ENABLE_QQFRIENDS_SHOW_QQZONE_ITEM) {
            if (ShareConfiguration.ENABLE_QQFRIENDS_SHOW_QQZONE_DIALOG) {
                // 最后一个二进制位置为1, 其他位不变
                mExtarFlag |= Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN;
            } else {
                // 最后一个二进制位置为0, 其他位不变
                mExtarFlag &= (0xFFFFFFFF - Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            }
        } else if (!ShareConfiguration.ENABLE_QQFRIENDS_SHOW_QQZONE_DIALOG) {
            if (ShareConfiguration.ENABLE_QQFRIENDS_SHOW_QQZONE_ITEM) {
                // 倒数第二位置为1, 其他位不变
                mExtarFlag |= Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE;
            } else {
                // 倒数第二位置为0, 其他位不变
                mExtarFlag &= (0xFFFFFFFF - Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
            }
        }
        params.putInt(Tencent.SHARE_TO_QQ_EXT_INT, mExtarFlag);

        mTencent.shareToQQ((Activity) getContext(), params, this);
    }

    @Override
    public boolean validateCheck(IShareObject... shareObject) {
        return false;
    }
}

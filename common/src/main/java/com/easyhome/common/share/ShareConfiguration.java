package com.easyhome.common.share;

import com.easyhome.common.R;

/**
 * 分享配置文件
 */
public class ShareConfiguration {

    /* 填写对应软件名称 */
    public static final String APPNAME = "公共组件";

    public static final boolean ENABLE_QQFRIENDS = true;

    public static final boolean ENABLE_QQWEIBO = true;

    public static final boolean ENABLE_QQZONE = true;

    public static final boolean ENABLE_RENREN = false;

    public static final boolean ENABLE_WEIBLOG = true;

    public static final boolean ENABLE_WEICHAT = true;

    /*允许微信分享到朋友圈 */
    public static final boolean ENABLE_WEICHAT_SHARE_FIRENDS = true;

    /*允许分享到QQ好友时弹出QQZone的窗口，与{@link #ENABLE_QQFRIENDS_SHOW_QQZONE_ITEM} 互斥 */
    public static final boolean ENABLE_QQFRIENDS_SHOW_QQZONE_DIALOG = true;

    /*允许分享到QQ好友时在第一列显示QQZone，与{@link #ENABLE_QQFRIENDS_SHOW_QQZONE_DIALOG} 互斥*/
    public static final boolean ENABLE_QQFRIENDS_SHOW_QQZONE_ITEM = false;

    /**
     * QQ互联
     */
    public static interface QQCONNECT {
        /* 请修改的时候把AndroidMainest.xml中 com.tencent.tauth.AuthActivity 的scheme对应值*/
        public static final String APPID = "100575846";
        /* 授权时申请的权限范围 */
        public static final String SCOPE = "all";

        public static final int QQWEIBO_NAME_ID = R.string.action_qqweibo;

        public static final int QQWEIBO_ICON_ID = R.drawable.bt_share_qqweibo;

        public static final int QQ_NAME_ID = R.string.action_qqfriends;

        public static final int QQ_ICON_ID = R.drawable.bt_share_qq;

        public static final int QQZONE_NAME_ID = R.string.action_qqzone;

        public static final int QQZONE_ICON_ID = R.drawable.bt_share_qqzone;
    }

    /**
     * 微信
     */
    public static interface WEICHAT {
        public static final String APPID = "wx8c1fdbb2dd272ed3";

        public static final int WEIXIN_NAME_ID = R.string.action_weichat;

        public static final int WEIXIN_ICON_ID = R.drawable.bt_share_wechat;

        public static final int WEIXIN_FREINDS_NAME_ID = R.string.action_weichat_friends;

        public static final int WEIXIN_FREINDS_ICON_ID = R.drawable.bt_share_circleoffriends;
    }

    /**
     * 微博
     */
    public static interface WEIBLOG {
        public static final String APPID = "2994481980";

        public static final int NAME_ID = R.string.action_weiblog;

        public static final int ICON_ID = R.drawable.bt_share_sinaweibo;

        public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

        /* 授权时申请的权限范围 */
        public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
                + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                + "follow_app_official_microblog,invitation_write";

        /*  APP_SECRET */
        public static final String WEIBO_DEMO_APP_SECRET = "1f75158f0939fcd9406feb8daee63f2a";

        /* 通过 code 获取 Token 的 URL */
        public static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";
    }
}

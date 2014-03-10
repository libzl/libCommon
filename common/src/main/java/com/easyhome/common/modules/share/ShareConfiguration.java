package com.easyhome.common.modules.share;


import com.easyhome.sample.R;

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

	/*是否开启消息通知*/
	public static boolean ENABLE_SHOW_NOTIFY = true;

    /**
     * QQ互联
     */
    public static interface QQCONNECT {
        /* 请修改的时候把AndroidMainest.xml中 com.tencent.tauth.AuthActivity 的scheme对应值*/
        public static final String APPID = "100363659";
        /* 授权时申请的权限范围 */
        public static final String SCOPE = "all";

		public static final int APP_NAME_ID = R.string.app_qq;

        public static final int QQWEIBO_NAME_ID = R.string.action_qqweibo;

        public static final int QQWEIBO_ICON_ID = R.drawable.ic_share_qqweibo;
		public static final int QQ_NAME_ID = R.string.action_qqfriends;

        public static final int QQ_ICON_ID = R.drawable.ic_share_qq;

        public static final int QQZONE_NAME_ID = R.string.action_qqzone;

        public static final int QQZONE_ICON_ID = R.drawable.ic_share_qqzone;
    }

    /**
     * 微信
     */
    public static interface WEICHAT {
        public static final String APPID = "wx68f613bbb4b21c3e";

		public static final int APP_NAME_ID = R.string.app_weichat;

        public static final int WEIXIN_NAME_ID = R.string.action_weichat;

        public static final int WEIXIN_ICON_ID = R.drawable.ic_share_wechat;

        public static final int WEIXIN_FREINDS_NAME_ID = R.string.action_weichat_friends;

        public static final int WEIXIN_FREINDS_ICON_ID = R.drawable.ic_share_wechat_friend;

	}

    /**
     * 微博
     */
    public static interface WEIBLOG {

        public static final String APPID = "3156384834";
        /*  APP_SECRET */
        public static final String APP_SECRET = "5085b73ecdf612360d6542c632521d1e";

		public static final int APP_NAME_ID = R.string.app_weiblog;

        public static final int NAME_ID = R.string.action_weiblog;

        public static final int ICON_ID = R.drawable.ic_share_sina;

        public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

        /* 授权时申请的权限范围 */
        public static final String SCOPE = "";

        /* 通过 code 获取 Token 的 URL */
        public static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";
        public static final String OAUTH2_CODE_URL = "https://open.weibo.cn/oauth2/authorize";
    }
}

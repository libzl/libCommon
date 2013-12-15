package com.easyhome.common.share.object;

/**
 * 网址
 *
 * @author zhoulu
 * @date 13-12-11
 */
public class ShareWebpage extends BaseShareObject {
    private String url;

    /**
     * 构造
     * @param url 跳转的url
     */
    public ShareWebpage(String url) {
        this.url = url;
    }

    /**
     * 类型
     *
     * @return
     */
    @Override
    public TYPE getType() {
        return TYPE.TYPE_WEBURL;
    }

    @Override
    public String getRedirectUrl() {
        return url;
    }

}

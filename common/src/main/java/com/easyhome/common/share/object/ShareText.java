package com.easyhome.common.share.object;

/**
 * 分享文本
 *
 * @author zhoulu
 * @date 13-12-11
 */
public class ShareText extends BaseShareObject {

    private String mText;

    /**
     * 构造
     * @param text 文本内容
     */
    public ShareText(String text){
        mText = text;
    }

    /**
     * 类型
     *
     * @return
     */
    @Override
    public TYPE getType() {
        return TYPE.TYPE_TEXT;
    }

    @Override
    public String getMessage() {
        return mText;
    }
}

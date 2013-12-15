package com.easyhome.common.share.object;

import android.graphics.Bitmap;

import com.easyhome.common.share.IShareObject;

/**
 * 数据源基类
 *
 * @author zhoulu
 * @date 13-12-11
 */
public abstract class BaseShareObject implements IShareObject{

    private String title = "";
    private String secondTitle = "";
    private String message = "";

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
        return null;
    }

    @Override
    public String[] getThumbnailUrl() {
        return null;
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
}

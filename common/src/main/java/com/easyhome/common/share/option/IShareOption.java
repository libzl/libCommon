package com.easyhome.common.share.option;

import android.content.Context;
import android.content.Intent;

import com.easyhome.common.share.object.IShareObject;

/**
 * 分享选项接口
 */
public interface IShareOption {

    /**
     * 注册监听
     * @param shareListener
     */
    void setShareListener(IShareListener shareListener);

    /**
     * 获得APPID
     * @return
     */
    String getAppId();

    /**
     * 是否支持以SSO进行分享
     * @return
     */
    boolean isSupportSSO();

    /**
     * 是否支持以Web形式进行分享
     * @return
     */
    boolean isSupportWeb();

    /**
     * 是否安装软件
     * @return
     */
    boolean isInstalledApp();

    /**
     * 获得ICON资源ID
     * @return
     */
    int getIcon();

    /**
     * 获得功能名字
     * @return
     */
    String getName();

    /**
     * 检查API-SDK是否有效
     *
     * @return
     */
    boolean onCreate();

    /**
     * 授权过程
     */
    void onAuth();

    /**
     * 帐号登录
     */
    void onLogin();

    /**
     * 帐号注销
     */
    void onLogout();

    /**
     * 消息响应监听
     * @param context
     * @param intent
     * @return
     */
    boolean onEvent(Context context, Intent intent);

    /**
     * 开始执行分享过程
     */
    void onShare(IShareObject... shareObject);

    /**
     * 取消授权
     */
    void onCancelAuth();

    /**
     * 检查数据有效性
     * @return
     */
    boolean validateCheck(IShareObject... shareObject);

    public interface IShareListener {

        /**
         * 分享结果反馈
         * @param success
         * @param message
         */
        void onResponceShare(boolean success, String message);
    }
}

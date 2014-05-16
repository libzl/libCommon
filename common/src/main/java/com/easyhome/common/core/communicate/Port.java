package com.easyhome.common.core.communicate;

/**
 * TODO {Description}
 *
 * @author by kevin on 14-5-13.
 */
public interface Port {
    /**
     * 创建客户端插口
     */
    void buildClient();

    /**
     * 创建服务端插口
     */
    void buildServer();

    /**
     * 创建链接通道
     */
    void createPipeline();

    /**
     * 管道通信连接
     * @param request
     */
    void connectPipeline(Pipeline pipeline, Request request);
}

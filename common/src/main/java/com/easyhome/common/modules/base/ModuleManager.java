package com.easyhome.common.modules.base;

/**
 * 管理模块列表
 *
 * @author by kevin on 5/5/14.
 */
public class ModuleManager {

    private static ModuleManager ourInstance = new ModuleManager();

    public static ModuleManager getInstance() {
        return ourInstance;
    }

    private ModuleManager() {
    }
}

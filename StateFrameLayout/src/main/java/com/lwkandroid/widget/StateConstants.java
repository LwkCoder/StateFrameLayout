package com.lwkandroid.widget;

/**
 * Description:状态常量
 *
 * @author LWK
 * @date 2019/6/11
 */
final class StateConstants
{
    /**
     * 无状态，只在StateFrameLayout初始化时作占位，防止初始化后无法切到INIT状态
     */
    static final int NONE = 100;
    /**
     * 初始状态，不显示任何View
     */
    static final int INIT = NONE << 1;
    /**
     * 请求数据
     */
    static final int LOADING = NONE << 2;
    /**
     * 空数据
     */
    static final int EMPTY = NONE << 3;
    /**
     * 网络异常
     */
    static final int NET_ERROR = NONE << 4;
    /**
     * 正常显示
     */
    static final int CONTENT = NONE << 5;

    static final String TAG_INIT = "init";
    static final String TAG_LOADING = "loading";
    static final String TAG_EMPTY = "empty";
    static final String TAG_ERROR = "error";
    static final String TAG_CONTENT = "content";
}

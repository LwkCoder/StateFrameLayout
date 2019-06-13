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
     * 初始状态，不显示任何View
     */
    static final int INIT = 0X1;
    /**
     * 请求数据
     */
    static final int LOADING = 0X2;
    /**
     * 空数据
     */
    static final int EMPTY = 0X3;
    /**
     * 网络异常
     */
    static final int NET_ERROR = 0X4;
    /**
     * 正常显示
     */
    static final int CONTENT = 0X5;
}

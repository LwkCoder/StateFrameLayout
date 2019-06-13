package com.lwkandroid.widget;

/**
 * Description:管理类
 *
 * @author LWK
 * @date 2019/6/12
 */
public final class StateFrameLayoutManager
{
    static
    {
        GLOBAL_OPTIONS = new StateGlobalOptions();
    }

    private static final StateGlobalOptions GLOBAL_OPTIONS;

    private StateFrameLayoutManager()
    {
    }

    public static StateGlobalOptions getGlobalOptions()
    {
        return GLOBAL_OPTIONS;
    }
}

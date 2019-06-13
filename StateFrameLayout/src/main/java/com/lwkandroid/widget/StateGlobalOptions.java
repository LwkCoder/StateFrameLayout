package com.lwkandroid.widget;

import androidx.annotation.LayoutRes;

/**
 * Description: 全局配置
 *
 * @author LWK
 * @date 2019/6/11
 */
public final class StateGlobalOptions
{
    private @LayoutRes
    int mEmptyLayoutId = -1;
    private @LayoutRes
    int mNetErrorLayoutId = -1;
    private @LayoutRes
    int mLoadingLayoutId = -1;
    private boolean mEnableContentAnim;
    private BaseContentAnimation mContentAnim = new AlphaContentAnimation();

    public int getEmptyLayoutId()
    {
        return mEmptyLayoutId;
    }

    public StateGlobalOptions setEmptyLayoutId(@LayoutRes int layoutId)
    {
        this.mEmptyLayoutId = layoutId;
        return this;
    }

    public int getNetErrorLayoutId()
    {
        return mNetErrorLayoutId;
    }

    public StateGlobalOptions setNetErrorLayoutId(@LayoutRes int layoutId)
    {
        this.mNetErrorLayoutId = layoutId;
        return this;
    }

    public int getLoadingLayoutId()
    {
        return mLoadingLayoutId;
    }

    public StateGlobalOptions setLoadingLayoutId(@LayoutRes int layoutId)
    {
        this.mLoadingLayoutId = layoutId;
        return this;
    }

    public boolean isEnableContentAnim()
    {
        return mEnableContentAnim;
    }

    public StateGlobalOptions setEnableContentAnim(boolean enable)
    {
        this.mEnableContentAnim = enable;
        return this;
    }

    public BaseContentAnimation getContentAnim()
    {
        return mContentAnim;
    }

    public StateGlobalOptions setContentAnim(BaseContentAnimation animation)
    {
        this.mContentAnim = animation;
        return this;
    }
}

package com.lwkandroid.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.lwkandroid.stateframelayout.R;

import androidx.annotation.LayoutRes;

/**
 * 配合各状态切换的FrameLayout
 *
 * @author LWK
 */
public class StateFrameLayout extends FrameLayout
{
    /**
     * Loading视图布局id
     */
    private int mLoadingLayoutId = -1;
    /**
     * Empty视图布局id
     */
    private int mEmptyLayoutId = -1;
    /**
     * NetError视图布局id
     */
    private int mNetErrorLayoutId = -1;
    /**
     * 内容视图显示动画
     */
    private boolean mEnableContentAnim = StateFrameLayoutManager.getGlobalOptions().isEnableContentAnim();
    /**
     * 当前状态
     */
    @LayoutState
    private int mCurState = StateConstants.NONE;
    /**
     * 内容视图动画
     */
    private BaseContentAnimation mContentAnimation = StateFrameLayoutManager.getGlobalOptions().getContentAnim();
    /**
     * 是否初始化完成
     */
    private boolean mHasInit = false;

    public StateFrameLayout(Context context)
    {
        super(context);
        init(context, null);
    }

    public StateFrameLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        if (!mHasInit)
        {
            if (getChildCount() > 0)
            {
                getChildAt(0).setTag(StateConstants.TAG_CONTENT);
            }
            setChildViewWithTag(StateConstants.TAG_EMPTY, inflateChildLayout(mEmptyLayoutId));
            setChildViewWithTag(StateConstants.TAG_LOADING, inflateChildLayout(mLoadingLayoutId));
            setChildViewWithTag(StateConstants.TAG_ERROR, inflateChildLayout(mNetErrorLayoutId));

            //切换到初始状态
            switchToInitState();
            mHasInit = true;
        }
    }

    private void init(Context context, AttributeSet attrs)
    {
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StateFrameLayout);
        if (ta != null)
        {
            mLoadingLayoutId = ta.getResourceId(R.styleable.StateFrameLayout_loadingLayoutResId,
                    StateFrameLayoutManager.getGlobalOptions().getLoadingLayoutId());
            mEmptyLayoutId = ta.getResourceId(R.styleable.StateFrameLayout_emptyLayoutResId,
                    StateFrameLayoutManager.getGlobalOptions().getEmptyLayoutId());
            mNetErrorLayoutId = ta.getResourceId(R.styleable.StateFrameLayout_netErrorLayoutResId,
                    StateFrameLayoutManager.getGlobalOptions().getNetErrorLayoutId());
            mEnableContentAnim = ta.getBoolean(R.styleable.StateFrameLayout_enableContentAnim,
                    StateFrameLayoutManager.getGlobalOptions().isEnableContentAnim());
            ta.recycle();
        }
    }

    /**
     * 切换到初始状态，所有View都隐藏
     */
    public void switchToInitState()
    {
        realSwitchState(StateConstants.INIT);
    }

    /**
     * 切换到Empty状态
     */
    public void switchToEmptyState()
    {
        realSwitchState(StateConstants.EMPTY);
    }

    /**
     * 切换到Loading状态
     */
    public void switchToLoadingState()
    {
        realSwitchState(StateConstants.LOADING);
    }

    /**
     * 切换到NetError状态
     */
    public void switchToNetErrorState()
    {
        realSwitchState(StateConstants.NET_ERROR);
    }

    /**
     * 切换到Content状态
     */
    public void switchToContentState()
    {
        realSwitchState(StateConstants.CONTENT);
    }

    /**
     * 设置Empty状态的布局id
     *
     * @param layoutId
     */
    public void setEmptyLayoutId(@LayoutRes int layoutId)
    {
        this.mEmptyLayoutId = layoutId;
        setEmptyView(inflateChildLayout(mEmptyLayoutId));
    }

    /**
     * 【2.1.0版本新增】
     * 设置Empty状态对应的View
     *
     * @param view
     */
    public void setEmptyView(View view)
    {
        setChildViewWithTag(StateConstants.TAG_EMPTY, view);
    }

    /**
     * 设置Loading状态的布局id
     *
     * @param layoutId
     */
    public void setLoadingLayoutId(@LayoutRes int layoutId)
    {
        this.mLoadingLayoutId = layoutId;
        setLoadingView(inflateChildLayout(mLoadingLayoutId));
    }

    /**
     * 【2.1.0版本新增】
     * 设置Loading状态对应的View
     *
     * @param view
     */
    public void setLoadingView(View view)
    {
        setChildViewWithTag(StateConstants.TAG_LOADING, view);
    }

    /**
     * 设置NetError状态的布局id
     *
     * @param layoutId
     */
    public void setNetErrorLayoutId(@LayoutRes int layoutId)
    {
        this.mNetErrorLayoutId = layoutId;
        setNetErrorView(inflateChildLayout(mNetErrorLayoutId));
    }

    /**
     * 【2.1.0版本新增】
     * 设置NetError状态对应的View
     *
     * @param view
     */
    public void setNetErrorView(View view)
    {
        setChildViewWithTag(StateConstants.TAG_ERROR, view);
    }

    /**
     * 设置内容视图的显示动画
     *
     * @param animation
     */
    public void setContentAnimation(BaseContentAnimation animation)
    {
        this.mContentAnimation = animation;
    }

    private View inflateChildLayout(@LayoutRes int layoutId)
    {
        View view = null;
        try
        {
            view = LayoutInflater.from(getContext()).inflate(layoutId, this, false);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

    /**
     * 除了指定Tag的View外，隐藏其他全部View
     *
     * @param tag
     * @return 指定的View
     */
    private View hideViewsExcept(Object tag)
    {
        View indexChild = null;
        for (int i = 0, count = getChildCount(); i < count; i++)
        {
            View child = getChildAt(i);
            if (tag != null && tag == child.getTag())
            {
                child.setVisibility(VISIBLE);
                indexChild = child;
            } else
            {
                child.setVisibility(GONE);
            }
        }
        return indexChild;
    }

    /**
     * 获取当前状态
     */
    public int getCurrentState()
    {
        return mCurState;
    }

    /**
     * 是否开启内容布局显示动画
     */
    public void enableContentAnim(boolean enable)
    {
        this.mEnableContentAnim = enable;
    }

    /**
     * 内部执行切换状态的方法
     *
     * @param targetState 目标状态
     */
    private void realSwitchState(int targetState)
    {
        if (mCurState == targetState)
        {
            return;
        }

        if (StateConstants.INIT == targetState)
        {
            hideViewsExcept(StateConstants.TAG_INIT);
        } else if (StateConstants.EMPTY == targetState)
        {
            hideViewsExcept(StateConstants.TAG_EMPTY);
        } else if (StateConstants.LOADING == targetState)
        {
            hideViewsExcept(StateConstants.TAG_LOADING);
        } else if (StateConstants.NET_ERROR == targetState)
        {
            hideViewsExcept(StateConstants.TAG_ERROR);
        } else if (StateConstants.CONTENT == targetState)
        {
            View contentView = hideViewsExcept(StateConstants.TAG_CONTENT);
            if (mEnableContentAnim && mContentAnimation != null
                    && contentView != null)
            {
                mContentAnimation.startAnim(contentView);
            }
        }
        mCurState = targetState;
    }

    /**
     * 根据Tag设置子View
     *
     * @param tag
     * @param childView
     */
    private void setChildViewWithTag(Object tag, View childView)
    {
        int visibility = View.GONE;
        for (int i = 0, count = getChildCount(); i < count; i++)
        {
            View child = getChildAt(i);
            if (tag == child.getTag())
            {
                //存在相同tag的view需要先移除
                visibility = child.getVisibility();
                removeView(child);
                break;
            }
        }

        if (childView != null)
        {
            childView.setTag(tag);
            addView(childView);
            childView.setVisibility(visibility);
        }
    }

    /****************************************************** 状态恢复 ***********************************************************************/

    @Override
    protected Parcelable onSaveInstanceState()
    {
        SavedViewState state = new SavedViewState(super.onSaveInstanceState());
        state.loadingId = mLoadingLayoutId;
        state.emptyId = mEmptyLayoutId;
        state.netErrorId = mNetErrorLayoutId;
        state.lastState = mCurState;
        state.enableAnim = mEnableContentAnim;
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state)
    {
        if (!(state instanceof SavedViewState))
        {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedViewState ss = (SavedViewState) state;
        super.onRestoreInstanceState(ss);

        mLoadingLayoutId = ss.loadingId;
        mEmptyLayoutId = ss.emptyId;
        mNetErrorLayoutId = ss.netErrorId;
        mEnableContentAnim = ss.enableAnim;
        realSwitchState(ss.lastState);
    }

    static class SavedViewState extends BaseSavedState
    {
        int loadingId;
        int emptyId;
        int netErrorId;
        int lastState;
        boolean enableAnim;

        SavedViewState(Parcelable superState)
        {
            super(superState);
        }

        private SavedViewState(Parcel source)
        {
            super(source);
            loadingId = source.readInt();
            emptyId = source.readInt();
            netErrorId = source.readInt();
            lastState = source.readInt();
            enableAnim = source.readByte() == (byte) 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags)
        {
            super.writeToParcel(out, flags);
            out.writeInt(loadingId);
            out.writeInt(emptyId);
            out.writeInt(netErrorId);
            out.writeInt(lastState);
            out.writeByte(enableAnim ? (byte) 1 : (byte) 0);
        }

        public static final Parcelable.Creator<SavedViewState> CREATOR = new Creator<SavedViewState>()
        {
            @Override
            public SavedViewState createFromParcel(Parcel source)
            {
                return new SavedViewState(source);
            }

            @Override
            public SavedViewState[] newArray(int size)
            {
                return new SavedViewState[size];
            }
        };
    }
}

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
     * Loading视图
     */
    private View mLoadingView;
    /**
     * Empty视图
     */
    private View mEmptyView;
    /**
     * NetError视图
     */
    private View mNetErrorView;
    /**
     * 内容视图
     */
    private View mContentView;
    /**
     * 内容视图显示动画
     */
    private boolean mEnableContentAnim = StateFrameLayoutManager.getGlobalOptions().isEnableContentAnim();
    /**
     * 当前状态
     */
    @LayoutState
    private int mCurState;
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

    private void init(Context context, AttributeSet attrs)
    {
        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StateFrameLayout);
        if (ta != null)
        {
            int count = ta.getIndexCount();
            for (int i = 0; i < count; i++)
            {
                int index = ta.getIndex(i);
                if (index == R.styleable.StateFrameLayout_loadingLayoutResId)
                {
                    mLoadingLayoutId = ta.getResourceId(index, StateFrameLayoutManager.getGlobalOptions().getLoadingLayoutId());
                } else if (index == R.styleable.StateFrameLayout_emptyLayoutResId)
                {
                    mEmptyLayoutId = ta.getResourceId(index, StateFrameLayoutManager.getGlobalOptions().getEmptyLayoutId());
                } else if (index == R.styleable.StateFrameLayout_netErrorLayoutResId)
                {
                    mNetErrorLayoutId = ta.getResourceId(index, StateFrameLayoutManager.getGlobalOptions().getNetErrorLayoutId());
                } else if (index == R.styleable.StateFrameLayout_enableContentAnim)
                {
                    mEnableContentAnim = ta.getBoolean(index, StateFrameLayoutManager.getGlobalOptions().isEnableContentAnim());
                }
            }
            ta.recycle();
        }
    }

    /**
     * 切换到初始状态，所有View都隐藏
     */
    public void switchToInitState()
    {
        hideAllViews();
        mCurState = StateConstants.INIT;
    }

    /**
     * 切换到Empty状态
     */
    public void switchToEmptyState()
    {
        hideAllViews();
        if (mEmptyView == null)
        {
            throw new IllegalArgumentException("You can't switch to Empty State with no layout resource id assigned.");
        }
        mEmptyView.setVisibility(VISIBLE);
        mCurState = StateConstants.EMPTY;
    }

    /**
     * 切换到Loading状态
     */
    public void switchToLoadingState()
    {
        hideAllViews();
        if (mLoadingView == null)
        {
            throw new IllegalArgumentException("You can't switch to Loading State with no layout resource id assigned.");
        }
        mLoadingView.setVisibility(VISIBLE);
        mCurState = StateConstants.LOADING;
    }

    /**
     * 切换到NetError状态
     */
    public void switchToNetErrorState()
    {
        hideAllViews();
        if (mNetErrorView == null)
        {
            throw new IllegalArgumentException("You can't switch to NetError State with no layout resource id assigned.");
        }
        mNetErrorView.setVisibility(VISIBLE);
        mCurState = StateConstants.NET_ERROR;
    }

    /**
     * 切换到Content状态
     */
    public void switchToContentState()
    {
        hideAllViews();
        if (mContentView == null)
        {
            if (getChildCount() == 0)
            {
                throw new IllegalArgumentException("You have to set a Content Layout into StateFrameLayout at xml resource.");
            }
            //内容视图必须是最先加入的,index=0
            mContentView = getChildAt(0);
            if (mContentView == null)
            {
                throw new IllegalArgumentException("You have to set a Content Layout into StateFrameLayout at xml resource.");
            }
        }
        if (mEnableContentAnim)
        {
            mContentAnimation.startAnim(mContentView);
        }
        mContentView.setVisibility(VISIBLE);
        mCurState = StateConstants.CONTENT;
    }

    /**
     * 设置Empty状态的布局id
     *
     * @param layoutId
     */
    public void setEmptyLayoutId(@LayoutRes int layoutId)
    {
        if (mEmptyLayoutId == layoutId)
        {
            return;
        }
        this.mEmptyLayoutId = layoutId;
        initEmptyView();
        if (mCurState == StateConstants.EMPTY)
        {
            switchToEmptyState();
        }
    }

    /**
     * 设置Loading状态的布局id
     *
     * @param layoutId
     */
    public void setLoadingLayoutId(@LayoutRes int layoutId)
    {
        if (mLoadingLayoutId == layoutId)
        {
            return;
        }
        this.mLoadingLayoutId = layoutId;
        initLoadingView();
        if (mCurState == StateConstants.LOADING)
        {
            switchToLoadingState();
        }
    }

    /**
     * 设置NetError状态的布局id
     *
     * @param layoutId
     */
    public void setNetErrorLayoutId(@LayoutRes int layoutId)
    {
        if (mNetErrorLayoutId == layoutId)
        {
            return;
        }
        this.mNetErrorLayoutId = layoutId;
        initNetErrorView();
        if (mCurState == StateConstants.NET_ERROR)
        {
            switchToNetErrorState();
        }
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

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        if (!mHasInit)
        {
            initEmptyView();
            initLoadingView();
            initNetErrorView();
            //切换到初始状态
            switchToInitState();
            mHasInit = true;
        }
    }

    /**
     * 初始化EmptyView
     */
    private void initEmptyView()
    {
        if (mEmptyView != null && mEmptyView.getParent() != null)
        {
            removeView(mEmptyView);
            mEmptyView = null;
        }
        if (mEmptyLayoutId == -1)
        {
            mEmptyLayoutId = StateFrameLayoutManager.getGlobalOptions().getEmptyLayoutId();
        }
        if (mEmptyLayoutId != -1)
        {
            mEmptyView = inflateChildLayout(mEmptyLayoutId);
            addView(mEmptyView);
        }
    }

    private void initLoadingView()
    {
        if (mLoadingView != null && mLoadingView.getParent() != null)
        {
            removeView(mLoadingView);
            mLoadingView = null;
        }
        if (mLoadingLayoutId == -1)
        {
            mLoadingLayoutId = StateFrameLayoutManager.getGlobalOptions().getLoadingLayoutId();
        }
        if (mLoadingLayoutId != -1)
        {
            mLoadingView = inflateChildLayout(mLoadingLayoutId);
            addView(mLoadingView);
        }
    }

    private void initNetErrorView()
    {
        if (mNetErrorView != null && mNetErrorView.getParent() != null)
        {
            removeView(mNetErrorView);
            mNetErrorView = null;
        }
        if (mNetErrorLayoutId == -1)
        {
            mNetErrorLayoutId = StateFrameLayoutManager.getGlobalOptions().getNetErrorLayoutId();
        }
        if (mNetErrorLayoutId != -1)
        {
            mNetErrorView = inflateChildLayout(mNetErrorLayoutId);
            addView(mNetErrorView);
        }
    }

    /**
     * 切换状态
     *
     * @param state
     */
    protected void changeState(@LayoutState int state)
    {
        if (state == StateConstants.INIT)
        {
            switchToInitState();
        } else if (state == StateConstants.EMPTY)
        {
            switchToEmptyState();
        } else if (state == StateConstants.LOADING)
        {
            switchToLoadingState();
        } else if (state == StateConstants.CONTENT)
        {
            switchToContentState();
        } else if (state == StateConstants.NET_ERROR)
        {
            switchToNetErrorState();
        }
    }

    private View inflateChildLayout(@LayoutRes int layoutId)
    {
        return LayoutInflater.from(getContext()).inflate(layoutId, this, false);
    }

    /**
     * 隐藏所有布局
     */
    private void hideAllViews()
    {
        for (int i = 0, count = getChildCount(); i < count; i++)
        {
            getChildAt(i).setVisibility(GONE);
        }
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
        changeState(ss.lastState);
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

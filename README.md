# StateFrameLayout


## 简介
项目中经常遇到这样一种情况，新打开的界面需要加载数据，存在多种状态的结果，需要根据不同结果展示界面，这个过程归纳起来可以分为五种状态：**初始状态、请求状态、空数据状态、网络错误状态、成功请求状态。** 如果多个界面都存在这个流程，那么封装整个过程的调用就很有必要了，既可以简化调用过程，又可以很方便的管理整个流程。

`StateFrameLayout` 继承自`FrameLayout`，内部实现了一句代码切换各种状态的功能（各状态的布局均需要在xml指定，具有高度定制性），并且内部实现了状态缓存，无需担心内存回收后重新打开界面会导致状态被重置（可打开手机开发者选项中的“不保留活动”来验证）。

## 使用

Gradle中引用
```
    compile 'com.lwkandroid:StateFrameLayout:1.0.0'
```

### xml中定义

```
    <com.lwkandroid.stateframelayout.StateFrameLayout
        android:id="@+id/stateLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:enableContentAnim="true"   //是否在展示内容布局的时候开启动画（200ms的Alpha动画）
        app:emptyLayoutResId="@layout/layout_empty"   //这里指定空数据布局
        app:loadingLayoutResId="@layout/layout_loading" //这里指定加载过程的布局
        app:netErrorLayoutResId="@layout/layout_net_error" //这里指定网络错误的布局
        >

        <!--在这里定义内容布局，内容布局只能有一个-->
        <FrameLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/holo_green_dark">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:text="我是内容"
                android:textAppearance="?android:textAppearanceLarge"/>

        </FrameLayout>

    </com.lwkandroid.stateframelayout.StateFrameLayout>
```
**备注：** 此外，对于空数据和网络错误的状态提供了**重试**接口，需要在空数据或网络错误布局中指定触发的view对应的id：`android:id="@id/id_sfl_empty_retry"`或`android:id="@id/id_sfl_net_error_retry"`

<br />

### 代码中使用

**五种状态：** <br />

1. `StateFrameLayout.INIT`：初始状态，所有布局隐藏，默认切换的状态 <br />
2. `StateFrameLayout.LOADING`：只显示请求Loading布局 <br />
3. `StateFrameLayout.EMPTY`：只显示空数据布局 <br />
4. `StateFrameLayout.NET_ERROR`：只显示网络错误布局 <br />
5. `StateFrameLayout.SUCCESS`：只显示请求成功后的内容布局 <br />

```
    StateFrameLayout mStateFrameLayout = (StateFrameLayout) findViewById(R.id.stateLayout);

    //切换各种状态
    mStateFrameLayout.changeState(上面五种状态中任何一个);
    //是否在展示内容布局的时候开启动画（200ms的Alpha动画）
    mStateFrameLayout.enableContentAnim(true);

    //设置网络错误重试监听
    mStateFrameLayout.setOnNetErrorRetryListener(new StateFrameLayout.OnNetErrorRetryListener()
    {
        @Override
        public void onNetErrorRetry()
        {
            //TODO 在这里相应重试操作
        }
    });
    //设置空数据重试监听
    mStateFrameLayout.setOnEmptyRetryListener(new StateFrameLayout.OnEmptyRetryListener()
    {
        @Override
        public void onEmptyRetry()
        {
            //TODO 在这里相应重试操作
        }
    });
```

<br >

## 效果图
![](https://github.com/Vanish136/StateFrameLayout/raw/master/pics/sample01.png)
![](https://github.com/Vanish136/StateFrameLayout/raw/master/pics/sample02.png)
![](https://github.com/Vanish136/StateFrameLayout/raw/master/pics/sample03.png)
![](https://github.com/Vanish136/StateFrameLayout/raw/master/pics/sample04.png)

<br />

### 参考
MaterialPageStateLayout: https://github.com/Syehunter/MaterialPageStateLayout <br />
感谢所有为开源做出贡献的人们！
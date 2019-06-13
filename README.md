# StateFrameLayout


## 简介
项目中经常遇到这样一种情况，新打开的界面需要加载数据，存在多种状态的结果，需要根据不同结果展示界面，这个过程归纳起来可以分为五种状态：**初始状态Init、请求状态Loading、空数据状态Empty、网络错误状态NetError、成功请求状态Content。** 如果多个界面都存在这个流程，那么封装整个过程的调用就很有必要了，既可以简化调用过程，又可以很方便的管理整个流程。

`StateFrameLayout` 继承自`FrameLayout`，内部实现了一句代码切换各种状态的功能，并且内部实现了状态缓存，无需担心内存回收后重新打开界面会导致状态被重置（可打开手机开发者选项中的“不保留活动”来验证）。

## 使用

**2.0.0版本和之前不兼容**

【最新版本号以[这里](https://github.com/Vanish136/StateFrameLayout/releases)为准】

Gradle中引用
```
    #last-version请查看上面的最新版本号
    implementation 'com.lwkandroid.widget:StateFrameLayout:last-version'
```

### 全局配置
2.0.0版本开始支持全局配置，可统一设置公共配置，示例如下：
```
# 在Application入口处设置
public class DemoApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        //全局配置
        StateFrameLayoutManager.getGlobalOptions()
                .setEmptyLayoutId(R.layout.layout_empty) //全局Empty状态的视图id
                .setLoadingLayoutId(R.layout.layout_loading) //全局Loading状态的视图id
                .setNetErrorLayoutId(R.layout.layout_net_error) //全局NetError状态的视图id
                .setEnableContentAnim(true) //是否允许内容视图显示动画
                .setContentAnim(new BaseContentAnimation()); //全局内容视图显示的动画对象，可继承BaseContentAnimation对象自定义 
    }
}
```

### xml中定义
```
    <com.lwkandroid.widget.StateFrameLayout
        android:id="@+id/stateLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:enableContentAnim="true"   //是否在展示内容布局的时候开启动画（默认200ms的Alpha动画）
        app:emptyLayoutResId="@layout/layout_empty"   //Empty状态的视图id
        app:loadingLayoutResId="@layout/layout_loading" //Loading状态的视图id
        app:netErrorLayoutResId="@layout/layout_net_error" //NetError状态的视图id
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

    </com.lwkandroid.widget.StateFrameLayout>
```

### 代码中使用
```
    StateFrameLayout mStateFrameLayout = (StateFrameLayout) findViewById(R.id.stateLayout);
    //Empty状态的视图id
    mStateFrameLayout.setEmptyLayoutId(R.layout.layout_empty2);
    //Loading状态的视图id
    mStateFrameLayout.setLoadingLayoutId(R.layout.layout_loading);
    //NetError状态的视图id
    mStateFrameLayout.setNetErrorLayoutId(R.layout.layout_net_error);
    //设置内容视图的显示动画
    mStateFrameLayout.setContentAnimation(BaseContentAnimation animation);
    
    //切换到Init初始状态，所有视图均不可见，这也是StateFrameLayout加载完成后的默认状态
    mStateFrameLayout.switchToInitState();
    //切换到Empty状态
    mStateFrameLayout.switchToEmptyState();
    //切换到Loading状态
    mStateFrameLayout.switchToLoadingState();
    //切换到NetError状态
    mStateFrameLayout.switchToNetErrorState();
    //切换到Content状态
    mStateFrameLayout.switchToContentState();
```

<br >

## 效果图
![](https://github.com/Vanish136/StateFrameLayout/raw/master/pics/sample01.png)
![](https://github.com/Vanish136/StateFrameLayout/raw/master/pics/sample02.png)
![](https://github.com/Vanish136/StateFrameLayout/raw/master/pics/sample03.png)
![](https://github.com/Vanish136/StateFrameLayout/raw/master/pics/sample04.png)

<br />

### 混淆配置
```
-dontwarn com.lwkandroid.widget.stateframelayout.**
-keep class com.lwkandroid.widget.stateframelayout.**{*;}
```
<br />

### 参考
MaterialPageStateLayout: https://github.com/Syehunter/MaterialPageStateLayout <br />
感谢所有为开源做出贡献的人们！
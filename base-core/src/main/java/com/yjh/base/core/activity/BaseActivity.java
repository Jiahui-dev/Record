package com.yjh.base.core.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yjh.base.core.lifecycle.Lifecycle;
import com.yjh.base.core.lifecycle.LifecycleEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by youjiahui on 2026/7/11
 */
public abstract class BaseActivity extends AppCompatActivity {

    private final List<Lifecycle> mControllers=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutId=getLayoutId();

    }

    protected void registerController(Lifecycle controller){
        if(controller!=null&&!mControllers.contains(controller)){
            mControllers.add(controller);
        }
    }

    /**
     * 核心分发逻辑：遍历所有控制器，驱动他们的状态机流转
     */
    private void dispatchLifecycleEvent(LifecycleEvent event){
        for(Lifecycle controller:mControllers){
            controller.onLifecycleChanged(event);
        }
    }

    protected abstract void onRegisterControllers();
    protected abstract int getLayoutId();
    protected void initView() {}
    protected void initListener() {}
    protected void initData() {}

    @Override
    protected void onStart() {
        super.onStart();
        dispatchLifecycleEvent(LifecycleEvent.ON_START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dispatchLifecycleEvent(LifecycleEvent.ON_RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dispatchLifecycleEvent(LifecycleEvent.ON_PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dispatchLifecycleEvent(LifecycleEvent.ON_STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 通知所有控制器进行最后的销毁和内存清理
        dispatchLifecycleEvent(LifecycleEvent.ON_DESTROY);
        // 强行清空集合，彻底断开Activity对所有控制器的强引用，确保0内存泄漏
        mControllers.clear();
    }
}

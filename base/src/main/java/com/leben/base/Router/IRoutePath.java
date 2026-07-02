package com.leben.base.Router;

import android.app.Activity;

/**
 * 路由安全通行证，通过接口类型来寻找真正的Activity
 */
public interface IRoutePath<T extends Activity> {
    Class<T> getTargetClass();
}

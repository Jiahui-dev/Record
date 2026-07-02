package com.leben.base.Router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.Postcard;

public class BaseRouter {
    public static class Postcard<T extends Activity>{
        private final Class<T> targetClass;
        private final Bundle bundle=new Bundle();

        public <T extends Activity> Postcard<T> build(IRoutePath<T> routePath){
            return new Postcard<>(routePath.getTargetClass());
        }

        protected void navigation(Context context,Postcard<?> postcard){
            Class<?> targetClass=postcard.targetClass;
            if (targetClass == null) {
                return;
            }
            Intent intent=new Intent(context,targetClass);
            intent.putExtras(postcard.bundle);
            if(!(context instanceof Activity)){
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }

        public Postcard(Class<T> targetClass){
            this.targetClass=targetClass;
        }

        public Postcard<T> withString(String key,String value){
            bundle.putString(key,value);
            return this;
        }

        public void navigation(Context context){
            BaseRouter.
        }
    }
}

package com.demo.ms.pullscaleviewdemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.HashMap;

/**
 *
 * Created by ms on 2016/7/6.
 */
public class MApplication extends Application {

    public static HashMap<String,String> baseQueryMap = new HashMap<String,String>();
    static {
        baseQueryMap.put("from","android");
        baseQueryMap.put("version","5.6.5.6");
        baseQueryMap.put("format","json");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }


}

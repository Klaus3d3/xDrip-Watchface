package com.klaus3d3.xDripwatchface;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class xDripwatchface extends Application{
        /**
         * Keeps a reference of the application context
         */
        private static xDripwatchface mAppInstance=null;
        public static Context appContext;

        public static xDripwatchface getInstance(){
            return mAppInstance;
        }
        public static xDripwatchface get(){
            return get(appContext);
        }

        public static xDripwatchface get(Context context){
            return (xDripwatchface) context.getApplicationContext();
        }

    public interface myContext{
            public void getappcontext();

    }
        @Override
        public void onCreate() {



            super.onCreate();
            mAppInstance=this;
            xDripwatchface.appContext=this.getApplicationContext();


        }


}
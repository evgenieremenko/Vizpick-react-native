package com.vizpickrn;

import android.util.Log;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class ArucoModule extends ReactContextBaseJavaModule {
    // Add the following lines
    public static ReactApplicationContext reactContext = null;

    public ArucoModule(ReactApplicationContext reactContext) {
        super(reactContext); //required by React Native
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Aruco"; //Aruco is how this module will be referred to from React Native
    }

    @ReactMethod
    public void setRNScreenDimensions(double x, double y) {
        VispickCameraView.Companion.setRnXDim(x);
        VispickCameraView.Companion.setRnYDim(y);
        Log.d("setRNScreenDimens_X","" + x  );
        Log.d("setRNScreenDimens_Y","" + y  );
    }



}
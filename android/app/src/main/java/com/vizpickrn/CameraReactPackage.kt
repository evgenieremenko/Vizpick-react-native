package com.vizpickrn;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class CameraReactPackage: ReactPackage {

    override fun createNativeModules(reactContext: ReactApplicationContext): kotlin.collections.List<NativeModule> {
        return Arrays.asList<NativeModule>(
                ALPRCameraManager(reactContext),
                ArucoModule(reactContext)
        )
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): kotlin.collections.List<ViewManager<*,*>> {
        return Arrays.asList<ViewManager<*,*>>(
                ReactCameraManager(reactContext)
        )
    }

}
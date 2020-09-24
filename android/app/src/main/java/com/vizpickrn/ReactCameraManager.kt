package com.vizpickrn;

import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.lang.ref.WeakReference;

class ReactCameraManager: SimpleViewManager<FrameLayout>,  LifecycleEventListener {

    val TAG = ReactContextBaseJavaModule::class.java.simpleName
    var layoutRef: WeakReference<ViewGroup>? = null
    val REACT_CLASS = "VispickCamera"

    constructor(reactContext: ReactApplicationContext):super(){
        reactContext.addLifecycleEventListener(this)
    }

    override fun getName(): String {
        return REACT_CLASS;
    }


    override fun createViewInstance(context: ThemedReactContext): FrameLayout {

        val inflater = LayoutInflater.from(context)
        val preview = inflater.inflate(R.layout.camera_layout, null) as FrameLayout
        layoutRef = WeakReference(preview)
        val camera = preview.findViewById<VispickCameraView>(R.id.camera_view)
        return preview
    }

    @ReactProp(name = "captureQuality")
    fun setCaptureQuality(view: FrameLayout, @Nullable captureQuality: Int) {
        val camera = view.findViewById<VispickCameraView>(R.id.camera_view)
        camera.setQuality(captureQuality)
    }


    @ReactProp(name = "aspect")
   fun setAspect(view:FrameLayout, @Nullable aspect: Int) {
        val camera = view.findViewById<VispickCameraView>(R.id.camera_view);
        Log.d("setAspect", "setaspect reactprop")
        camera.setAspect(aspect);
    }


    @ReactProp(name = "rotateMode")
    fun setRotateMode(view: FrameLayout , @Nullable rotateMode: Int) {
        val camera = view.findViewById<VispickCameraView>(R.id.camera_view)
        camera.setRotateMode(rotateMode == ALPRCameraManager.ALPRCameraRotateMode.ALPRCameraRotateModeOn);
    }

    override fun onDropViewInstance(view: FrameLayout) {
        super.onDropViewInstance(view)
        val camera = view.findViewById<VispickCameraView>(R.id.camera_view)
        camera.disableCameraView();
    }

    override fun onHostResume() {
        Log.d(TAG, "onHostResume$$");
        if (layoutRef == null) return;
        val layout = layoutRef?.get() ?: return
        val camera = layout.findViewById<VispickCameraView>(R.id.camera_view)
        camera.onResumeALPR();
    }

    override fun onHostPause() {
        Log.d(TAG, "onHostPause$$");
        if (layoutRef == null) return;
        val layout = layoutRef?.get();
        if (layout == null) return;
        val camera = layout.findViewById<VispickCameraView>(R.id.camera_view);
        camera.disableCameraView();
    }

    override fun onHostDestroy() {
        Log.d(TAG, "onHostDestroy");
        if (layoutRef == null) return;
        val layout = layoutRef?.get() ?: return
    }

}
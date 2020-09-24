package com.vizpickrn;

import android.Manifest;
import android.content.Intent
import android.content.pm.PackageManager;
import android.content.res.Configuration
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.WindowManager
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import com.swmansion.gesturehandler.react.RNGestureHandlerEnabledRootView;

class MainActivity: ReactActivity() {


        /**
         * Returns the name of the main component registered from JavaScript.
         * This is used to schedule rendering of the component.
         */
        override fun getMainComponentName(): String {
        return "VizpickRN"
        }

        override fun onCreate(savedInstanceState: Bundle?){
                super.onCreate(savedInstanceState)
                requestCameraPermission()
                window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        }

        fun requestCameraPermission(){
        System.out.println("$$$$$$ Requesting Camera Permission")
        val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1
        if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.CAMERA)) {
        Log.i("Vispick", "CAMERA PERMISSION NOT GRANTED")

        } else {
        ActivityCompat.requestPermissions(this,
        arrayOf(Manifest.permission.CAMERA),
        MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        Log.i("Vispick", "CAMERA PERMISSION GRANTED")
        }
        } else {
        Log.i("Vispick", "CAMERA PERMISSION ALREADY GRANTED")
        }

        }

        override fun createReactActivityDelegate(): ReactActivityDelegate {
        return object : ReactActivityDelegate(this, mainComponentName) {
        override fun createRootView(): ReactRootView {
        return RNGestureHandlerEnabledRootView(this@MainActivity)
        }
        }
        }

        override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig);
        var intent = Intent("onConfigurationChanged");
        intent.putExtra("newConfig", newConfig);
        this.sendBroadcast(intent);
        }

        }

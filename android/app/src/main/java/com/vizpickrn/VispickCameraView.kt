package com.vizpickrn;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Resources
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View
import android.view.WindowManager;
import com.vizpickrn.utils.Constants.*
import com.facebook.react.uimanager.ThemedReactContext;
import org.opencv.android.*

import org.opencv.aruco.Marker;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import com.vizpickrn.utils.Constants.MARKER_OUTLINE_SIZE;
import com.vizpickrn.utils.Constants.MARKER_OUTLINE_THICKNESS;
import com.vizpickrn.utils.Constants.PICKABLE_MARKER_COLOR;
import com.vizpickrn.utils.normalize
import com.vizpickrn.utils.point
import com.vizpickrn.utils.rotate
import com.vizpickrn.utils.translate
import com.vizpickrn.model.SessionModel
import com.facebook.react.modules.core.DeviceEventManagerModule
import org.opencv.aruco.Aruco.detectVispickMarkers
import org.opencv.core.Scalar
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.facebook.react.bridge.*
import kotlin.math.max


class VispickCameraView : JavaCameraView, ICameraView {

    private var quality = Quality.MEDIUM
    private var highResolution: Camera.Size? = null
    private var mediumResolution: Camera.Size? = null
    private var lowResolution: Camera.Size? = null
    private var rotation: Int = 0
//    val sessionModel: SessionModel = SessionModel()
    var count = 0
//    val rnXDim = 411.0
//    val rnYDim = 808.0
//    val canvasUp = -1 * heightOffset/2
//    val canvasDown = mFrameWidth + heightOffset/2
//    val canvasHeight = canvasDown - canvasUp

    interface Quality {
        companion object {
            val LOW = 0
            val MEDIUM = 1
            val HIGH = 2
        }
    }

    var canvasLeft: Float = 0.0f
    var canvasRight: Float = 0.0f
    var canvasWidth: Float = 0.0f

    var canvasUp: Float = 0.0f
    var canvasDown: Float = 0.0f
    var canvasHeight: Float = 0.0f


    constructor(context: Context, cameraId: Int) : super(context, cameraId) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    private fun createCvCameraViewListener(): CvCameraViewListener2 {
        return object : CvCameraViewListener2 {
            override fun onCameraViewStarted(width: Int, height: Int) {
                rotation = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
                initResolutions()
            }

            override fun onCameraViewStopped() {

            }

            override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame): Mat {

                val canvas = inputFrame.rgba()
//                Log.d("canvas dim", "height: " + canvas.height())
//                Log.d("canvas dim", "width: " + canvas.width())
//                Log.d("canvas dim", "rows: " + canvas.rows())
//                Log.d("canvas dim", "cols: " + canvas.cols())
                Log.d("rnXDim, YDim", "" + rnXDim + ", " + rnYDim)

                sessionModel.pushMarkers(detectVispickMarkers(canvas))


                if(rnXDim > 0 && rnYDim > 0){
//                    Log.d("mFrameW, mFrameH", "" + mFrameWidth + ", " + mFrameHeight)



//                    canvas.drawSquare(5, 5, lastTouchPoint(), Scalar(255.0, 0.0, 0.0))

                    val event = Arguments.createMap()
                    val eventName = "CAMERA_FRAME_EVENT"
                    val markerArray = Arguments.createArray()

//                    Log.d("mFrameHeight", "" + mFrameHeight)
//                    Log.d("mFrameWidth", "" + mFrameWidth)

//                    var canvasLeft: Float
//                    var canvasRight: Float
//                    var canvasWidth: Float
//
//                    var canvasUp: Float
//                    var canvasDown: Float
//                    var canvasHeight: Float

                    if(rnXDim < rnYDim){
                        canvasLeft = mFrameHeight + widthOffset/2 //445
                        canvasRight = -1 * widthOffset/2    //35
                        canvasWidth = canvasLeft - canvasRight

                        canvasUp = -1 * heightOffset/2
                        canvasDown = mFrameWidth + heightOffset/2
                        canvasHeight = canvasDown - canvasUp
                    }else{
                        Log.d("landscape mode1", "hi")

                        canvasLeft = -1 * widthOffset/2
                        canvasRight = mFrameWidth + widthOffset/2
                        canvasWidth = canvasRight - canvasLeft

                        canvasUp = -1 * heightOffset/2
                        canvasDown = mFrameHeight + heightOffset/2
                        canvasHeight = canvasDown - canvasUp

                    }


                    sessionModel.markersSeen().forEach {

                        var rnDisplacementX: Double
                        var rnDisplacementY: Double
                        var rnX: Double
                        var rnY: Double

                        //map canvasX to rnX
                        if(rnXDim < rnYDim){
                            val canvasX = it.region.center().y
                            val canvasY = it.region.center().x
                            rnDisplacementX = ((canvasX - canvasRight)/canvasWidth)*(rnXDim)
                            rnX = rnXDim - rnDisplacementX

                            //map canvasY to rnY
                            rnDisplacementY = ((canvasY - canvasUp)/(canvasHeight))*(rnYDim)
                            rnY = rnDisplacementY
                        }else{
                            val canvasX = it.region.center().x
                            val canvasY = it.region.center().y
                            rnDisplacementX = ((canvasX - canvasLeft)/canvasWidth)*(rnXDim)
                            rnX = rnDisplacementX

                            //map canvasY to rnY
                            rnDisplacementY = ((canvasY - canvasUp)/(canvasHeight))*(rnYDim)
                            rnY = rnDisplacementY
                        }

                        // Send Data (id, X, Y) to React Native
                        val marker = Arguments.createMap()
                        marker.putInt("id", it.id)
                        marker.putDouble("rnx", rnX)
                        marker.putDouble("rny", rnY)
                        markerArray.pushMap(marker)
                    }

                    event.putArray("markers", markerArray)
                    ArucoModule.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit(eventName, event)
                }

                return canvas
            }
        }
    }

    fun opencvToRnCoordX(canvasX: Double): Double{
        if(rnXDim < rnYDim){
            return (canvasX/canvasWidth)*rnXDim
        }else{
            return (canvasX/canvasWidth)*rnXDim
        }

//        return (canvasX - canvasRight/canvasWidth)*(rnXDim)


    }
    fun opencvToRnCoordY(canvasY: Double): Double{
//        return ((canvasY - canvasUp)/(canvasHeight))*(rnYDim)
        return (canvasY/canvasHeight)*rnYDim
    }

//    fun opencvToRnCoordX(canvasX: Double): Double{
//        val canvasLeft = mFrameHeight + widthOffset/2
//        val canvasRight = -1 * widthOffset/2
//        val canvasWidth = canvasLeft - canvasRight
////        return (canvasX - canvasRight/canvasWidth)*(rnXDim)
//        return (canvasX/mFrameHeight)*rnXDim
//
//    }
//    fun opencvToRnCoordY(canvasY: Double): Double{
//        val canvasUp = -1 * heightOffset/2
//        val canvasDown = mFrameWidth + heightOffset/2
//        val canvasHeight = canvasDown - canvasUp
////        return ((canvasY - canvasUp)/(canvasHeight))*(rnYDim)
//        return (canvasY/mFrameWidth)*rnYDim
//    }

    private fun initResolutions() {
        val resolutionList = mCamera.parameters.supportedPreviewSizes
        highResolution = mCamera.parameters.previewSize
        mediumResolution = highResolution
        lowResolution = mediumResolution

        val resolutionItr = resolutionList.listIterator()
        while (resolutionItr.hasNext()) {
            val s = resolutionItr.next()
            if (s.width < highResolution!!.width && s.height < highResolution!!.height && mediumResolution == highResolution) {
                mediumResolution = s
            } else if (s.width < mediumResolution!!.width && s.height < mediumResolution!!.height) {
                lowResolution = s
            }
        }
        if (lowResolution == highResolution) {
            lowResolution = mediumResolution
        }
        applyQuality(quality)
    }

    private fun setResolution(resolution: Camera.Size?) {
        if (resolution == null) return
        disconnectCamera()
        mMaxHeight = resolution.height
        mMaxWidth = resolution.width
        connectCamera(width, height)
    }

    override fun setQuality(captureQuality: Int) {
        when (captureQuality) {
            ALPRCameraManager.ALPRCameraCaptureSessionPreset.ALPRCameraCaptureSessionPresetLow -> {
                this.quality = VispickCameraView.Quality.LOW
                this.setQuality = 0
            }
            ALPRCameraManager.ALPRCameraCaptureSessionPreset.ALPRCameraCaptureSessionPresetMedium -> {
                this.quality = VispickCameraView.Quality.MEDIUM
                this.setQuality = 1
            }
            ALPRCameraManager.ALPRCameraCaptureSessionPreset.ALPRCameraCaptureSessionPresetHigh, ALPRCameraManager.ALPRCameraCaptureSessionPreset.ALPRCameraCaptureSessionPresetPhoto, ALPRCameraManager.ALPRCameraCaptureSessionPreset.ALPRCameraCaptureSessionPreset480p, ALPRCameraManager.ALPRCameraCaptureSessionPreset.ALPRCameraCaptureSessionPreset720p, ALPRCameraManager.ALPRCameraCaptureSessionPreset.ALPRCameraCaptureSessionPreset1080p -> {
                this.quality = VispickCameraView.Quality.HIGH
                this.setQuality = 2
            }
        }
        applyQuality(quality)
    }

    override fun setAspect(aspect: Int) {
        Log.d("setAspect", "set aspect vispickcameraview");
        Log.d("setAspect", "aspect value: " + aspect);
        disableView()
        when (aspect) {
            ALPRCameraManager.ALPRCameraAspect.ALPRCameraAspectFill -> this.aspect = JavaCameraView.ALPRCameraAspect.ALPRCameraAspectFill
            ALPRCameraManager.ALPRCameraAspect.ALPRCameraAspectFit -> this.aspect = JavaCameraView.ALPRCameraAspect.ALPRCameraAspectFit
            ALPRCameraManager.ALPRCameraAspect.ALPRCameraAspectStretch -> this.aspect = JavaCameraView.ALPRCameraAspect.ALPRCameraAspectStretch
        }
        onResumeALPR()
    }

    private fun applyQuality(quality: Int) {
        when (quality) {
            Quality.LOW -> setResolution(lowResolution)
            Quality.MEDIUM -> setResolution(mediumResolution)
            Quality.HIGH -> setResolution(highResolution)
        }
    }

    fun onResumeALPR() {
        Log.d("onResumerALPR", "sup onResumeALPR")
        if (context == null) return
        val loaderCallback = object : BaseLoaderCallback(context) {
            override fun onManagerConnected(status: Int) {
                when (status) {
                    LoaderCallbackInterface.SUCCESS -> {
                        Log.i(TAG, "OpenCV loaded successfully")
                        if (context != null) {
                            setCvCameraViewListener(createCvCameraViewListener())
//                            setOnTouchListener(::onCanvasTouch)
                            Log.d("onResumerALPR", "sup enableView")
                            enableView()
                            this@VispickCameraView.enableView()
                        }
                    }
                    else -> {
                        super.onManagerConnected(status)
                    }
                }
            }
        }
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization")
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, context, loaderCallback)
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!")
            loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }


    fun setRotateMode(isLandscape: Boolean) {
        val context = context ?: return
        val activity = scanForActivity(context) ?: return
        activity.requestedOrientation = if (isLandscape)
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        else
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }


    //    @Override
    fun disableCameraView() {
        removeCvCameraViewListener()
        super.disableView()
        disableView()
    }

    companion object {

        private val TAG = VispickCameraView::class.java.simpleName

        fun scanForActivity(viewContext: Context?): Activity? {
            if (viewContext == null)
                return null
            else if (viewContext is Activity)
                return viewContext
            else if (viewContext is ContextWrapper)
                return scanForActivity(viewContext.baseContext)
            else if (viewContext is ThemedReactContext)
                return viewContext.currentActivity
            return null
        }

        val sessionModel: SessionModel = SessionModel()


        var rnXDim = 0.0
        var rnYDim = 0.0

        val add = "plus"
        var markerStatusMap = mutableMapOf(5 to add, 9 to "check", 15 to add, 21 to add, 23 to "", 27 to "",
        1 to add, 24 to "check", 19 to add, 35 to "")



    }

    private var lastTouch: Point? = null
    fun lastTouchPoint() = lastTouch?.let { it }
            ?: Point(mFrameWidth / 2.0, mFrameHeight / 2.0)

    /**
     * Handles screen touch events. This handles touches exclusively for the JavaCamera2View SurfaceView,
     * all other components handle touch through default mechanisms.
     */
    fun onCanvasTouch(v: View?, p1: MotionEvent): Boolean {
        Log.i("MotionEvent", "x: ${p1.x}, ${p1.y}")
        p1.point()
                .normalize()
                .let { point ->
//                    Log.i("Vispick", "Touched: $point")

                    lastTouch = point
                    sessionModel.markersSeen()
                            .firstOrNull {
                                val markerLoc = it.region.center()
                                Log.i("Vispick", "Marker: $markerLoc")
                                point.inRectangle(markerLoc, TOUCH_RECT_APOTHEM_LENGTH)
                            }?.apply {
                                selectMarker(this)
                            }
                    false
                }
        return false
    }

    fun Point.normalize(): Point {
        val touchRight = width.toFloat()
        val touchDown = height.toFloat()
        val touchX = this.x
        val touchY = this.y

        val canvasLeft = mFrameHeight
        val canvasRight = 0
        val canvasUp = -1 * heightOffset/2
        val canvasDown = mFrameWidth + heightOffset/2

        //map touchX to canvasX
        val canvasDisplacementX = (touchX/touchRight)*(canvasLeft - canvasRight)
        val canvasX = canvasLeft - canvasDisplacementX

        //map touchY to canvasY
        val canvasDisplacementY = (touchY/touchDown)*(canvasDown - canvasUp)
        var canvasY = canvasUp + canvasDisplacementY

//        Log.d("canvasY", "canvasY: " + canvasY)
//
//        canvasY = canvasDown - ((height.toFloat() - 2.16 * canvasY) / 2)
//
//        Log.d("canvasY", "normalized: " + canvasY)
        return Point(canvasY, canvasX)

    }

    fun Point.dilate(scaleX: Float, scaleY: Float, center: Point): Point {
        return Point(center.x + scaleX * (x - center.x), center.y + scaleY * (y - center.y))
    }

    /**
     * Updates the model selected tag, and updated the UI to reflect this change.
     */
    public fun selectMarker(marker: Marker) {
        sessionModel.selectTag(marker)
//        viewManager.updateSelectionView(ItemInformationRequest.get(marker.id))
    }



    /**
     * Draws all seen markers on the screen.
     */
    private fun drawMarker(marker: Marker, canvas: Mat): String {
        if (sessionModel.selected() != null && sessionModel.selected()!! == marker.id) {
            canvas.drawRegion(MARKER_OUTLINE_SIZE, MARKER_OUTLINE_THICKNESS, marker.region, SELECTED_MARKER_COLOR)
            return "yellow"
        } else {
            if (sessionModel.isPickable(marker.id)) {
                canvas.drawRegion(MARKER_OUTLINE_SIZE, MARKER_OUTLINE_THICKNESS, marker.region, PICKABLE_MARKER_COLOR)
                return "red"
            } else {
                canvas.drawRegion(MARKER_OUTLINE_SIZE, MARKER_OUTLINE_THICKNESS, marker.region, NOT_PICKABLE_MARKER_COLOR)
                return "green"
            }
        }
    }

    private fun getMarkerStatusColor(marker: Marker): String {
        if (sessionModel.selected() != null && sessionModel.selected()!! == marker.id) {
            return "yellow"
        } else {
            if (sessionModel.isPickable(marker.id)) {
                return "red"
            } else {
                return "lawngreen"
            }
        }
    }

}

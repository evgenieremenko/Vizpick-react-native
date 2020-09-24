package com.vizpickrn.utils;

import org.opencv.core.Scalar;

public class Constants {
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    /**
     * A set of ad hoc scaling factors. These are device specific.
     */
    public static final int OPENCV_X_OFFSET = 506;
    public static final int OPENCV_Y_OFFSET = -828;
    public static final double OPENCV_X_SCALE = 1.756;
    public static final double OPENCV_Y_SCALE = 1.65;

/**
 * All possible device orientations.
 */
    public static final int ORIENTATION_PORTRAIT_NORMAL = 1;
    public static final int ORIENTATION_PORTRAIT_INVERTED = 2;
    public static final int ORIENTATION_LANDSCAPE_NORMAL = 3;
    public static final int ORIENTATION_LANDSCAPE_INVERTED = 4;

/**
 * The length of the apothem of the rectangle centered at the center of a marker.
 * Touch points contained within the rectangle are considered to be touching the
 * marker.
 */
    public static final double TOUCH_RECT_APOTHEM_LENGTH = 200.0;


/**
 * Configurable variables defining the style of the marker outline
 * displayed on the screen.
 */
    public static final int MARKER_OUTLINE_SIZE = 50;
    public static final int MARKER_OUTLINE_THICKNESS = 5;
    public static final Scalar PICKABLE_MARKER_COLOR = new Scalar(0.0, 0.0, 255.0, 255.0);
    public static final Scalar NOT_PICKABLE_MARKER_COLOR = new Scalar(0.0, 255.0, 0.0, 255.0);
    public static final Scalar SELECTED_MARKER_COLOR = new Scalar(255.0, 255.0, 0.0, 255.0);


public static final int MARKER_HISTORY_BUFFER = 20;

}

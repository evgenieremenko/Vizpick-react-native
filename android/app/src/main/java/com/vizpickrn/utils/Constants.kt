package com.vizpickrn.utils

import org.opencv.core.Scalar

const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1

/**
 * A set of ad hoc scaling factors. These are device specific.
 */
const val OPENCV_X_OFFSET = 506
const val OPENCV_Y_OFFSET = -828
const val OPENCV_X_SCALE = 1.756
const val OPENCV_Y_SCALE = 1.65

/**
 * All possible device orientations.
 */
const val ORIENTATION_PORTRAIT_NORMAL = 1
const val ORIENTATION_PORTRAIT_INVERTED = 2
const val ORIENTATION_LANDSCAPE_NORMAL = 3
const val ORIENTATION_LANDSCAPE_INVERTED = 4

/**
 * The length of the apothem of the rectangle centered at the center of a marker.
 * Touch points contained within the rectangle are considered to be touching the
 * marker.
 */
const val TOUCH_RECT_APOTHEM_LENGTH = 200.0


/**
 * Configurable variables defining the style of the marker outline
 * displayed on the screen.
 */
const val MARKER_OUTLINE_SIZE = 50
const val MARKER_OUTLINE_THICKNESS = 5
val PICKABLE_MARKER_COLOR = Scalar(0.0, 0.0, 255.0, 255.0)
val NOT_PICKABLE_MARKER_COLOR = Scalar(0.0, 255.0, 0.0, 255.0)
val SELECTED_MARKER_COLOR = Scalar(255.0, 255.0, 0.0, 255.0)


const val MARKER_HISTORY_BUFFER = 20
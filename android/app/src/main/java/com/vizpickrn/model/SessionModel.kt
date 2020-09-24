package com.vizpickrn.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.vizpickrn.utils.MARKER_HISTORY_BUFFER
import com.vizpickrn.utils.TagID
import com.vizpickrn.external.BinInformationRequest
import com.vizpickrn.external.ItemInformationRequest
import com.vizpickrn.external.ItemInformation
import com.vizpickrn.utils.MARKER_HISTORY_BUFFER
import org.opencv.aruco.Marker
import java.util.*


/**
 * Handles all the state associated with binning cases to a particular bin.
 *
 * TODO: Migrate all data structures to production data structures.
 * TODO: Need much better organization.
 */
class SessionModel : ViewModel() {


    /**
     * The current bin that is selected for binning. This being null means that a bin has
     * yet to be selected.
     */
    private var selected: Int? = null

    /**
     * A history of the markers in the last X number of frames.
     */
    private var markerHistory: MutableList<List<Marker>> = mutableListOf()

    /**
     * A list of all bin markers in the store.
     */
    private val binMarkers by lazy {
        BinInformationRequest.getAllBins(5260)
    }


    /**
     * Selects the given marker, marking it in memory as the currently active marker for actions
     * or information to be displayed.
     */
    fun selectTag(marker: Marker) {
        selected = marker.id
    }

    fun selectTagId(id: Int) {
        selected = id
    }


    /**
     * Invalidates the current session, all data associated with the current bin session will be lost.
     */
    fun unselectTag() {
        selected = null
    }

    /**
     * Pushes a particular frames markers into the marker history for this object.
     */
    fun pushMarkers(markers: List<Marker>) {
        markerHistory.add(markers)
        markerHistory = markerHistory.takeLast(MARKER_HISTORY_BUFFER).toMutableList()
        if (!selectedInHistory()) {
            selected = null
        }
    }

    /**
     * Checks to see if the selected marker exists anywhere in recent history.
     */
    private fun selectedInHistory(): Boolean =
            markersSeen(MARKER_HISTORY_BUFFER).any { it.id == selected }


    /**
     * Returns the list of all markers seen in the specified number of last frames.
     */
    fun markersSeen(i: Int = 1) = markerHistory.takeLast(i).flatten().distinct()

    /**
     * Returns the currently selected marker.
     */
    fun selected() = selected


    private fun isBin(marker: Int) = binMarkers.map { it.id }.contains(marker)
    private fun isCase(marker: Int) = !isBin(marker)

    fun caseSelected() = selected != null && isCase(selected!!)


//    override fun onCleared() {
//        super.onCleared()
//        Log.d("Vispick", "View")
//    }

    fun pick() {
        selected?.let { ItemInformationRequest.pick(it) }
    }

    private val itemInfo: MutableMap<TagID, ItemInformation?> = ItemInformationRequest.getAll()

    fun isPickable(id: Int) = itemInfo.containsKey(id) && itemInfo[id]?.pickQty != 0

    fun isPickable() = selected?.let { isPickable(it) } ?: false

}
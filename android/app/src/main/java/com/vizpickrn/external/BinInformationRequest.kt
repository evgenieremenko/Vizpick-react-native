package com.vizpickrn.external

import com.vizpickrn.external.mockBins

/**
 * Handles all requests pertaining to information about bin locations.
 */
object BinInformationRequest {

    /**
     * Obtains all bin id's for all departments in the provided store.
     */
    fun getAllBins(store: Int) = mockBins.values.toList()
}
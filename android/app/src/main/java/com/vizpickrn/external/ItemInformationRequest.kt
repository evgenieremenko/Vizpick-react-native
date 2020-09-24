package com.vizpickrn.external


import com.vizpickrn.utils.TagID

/**
 * Handles all requests pertaining to case tag or case information.
 */
object ItemInformationRequest {

    fun get(id: TagID): ItemInformation? = mockItems[id]

    fun get(ids: List<TagID>): Map<TagID, ItemInformation?> = ids.map { it to mockItems[it] }.toMap()

    fun getAll(): MutableMap<TagID, ItemInformation?> = mockItems


    fun pick(id: TagID) {
        mockItems[id] = mockItems[id]?.copy(pickQty = 0)
    }

}


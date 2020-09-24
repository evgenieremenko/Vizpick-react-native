package com.vizpickrn.external

/**
 * Contains all of the available information about a case and it's case tag.
 */
data class ItemInformation(
        val img: String?,
        val description: String,
        val upc: String,
        val dept: String,
        val onHand: Int,
        val pickQty: Int,
        val bin: Int?
)


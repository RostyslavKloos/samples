package com.skyyo.samples.features.googleMap

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

class Cluster(
    lat: Double,
    lng: Double
) : ClusterItem {

    private val position: LatLng = LatLng(lat, lng)

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String? {
        return null
    }

    override fun getSnippet(): String? {
        return null
    }
}

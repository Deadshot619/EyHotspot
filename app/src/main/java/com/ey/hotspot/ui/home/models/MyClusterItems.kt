package com.ey.hotspot.ui.home.models

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlin.properties.Delegates

class MyClusterItems : ClusterItem {


    private val mPosition: LatLng
    private val mTitle: String
    private val mSnippet: String
     var rating: String? = "0"
    var mIsFavourite by Delegates.notNull<Boolean>()
    lateinit var mNavigateURL: String
    lateinit var mAddress:String

    var  mLat by Delegates.notNull<Double>()
    var mLng by Delegates.notNull<Double>()

    var mItemID:Int = 0

    var uuid: String = ""

    constructor(lat: Double, lng: Double, mItemID: Int) {
        mPosition = LatLng(lat, lng)
        mTitle = "";
        mSnippet = "";
        this.mItemID = mItemID
        mLat = lat
        mLng = lng

    }

    constructor(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String

    ) {
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
        mLat = lat
        mLng = lng
    }


    constructor(
        lat: Double,
        lng: Double,
        title: String,
        snippet: String,
        isfavourite: Boolean,
        navigateURL: String,
        itemId:Int,
        address:String,
        rating: String?,
        uuid: String
    ) {
        mPosition = LatLng(lat, lng)
        mTitle = title
        mSnippet = snippet
        mIsFavourite = isfavourite
        mNavigateURL = navigateURL
        mItemID =itemId
        mAddress=address
        mLat = lat
        mLng = lng
        this.rating = rating
        this.uuid = uuid
    }


    override fun getSnippet(): String? {
        return mSnippet;
    }


    override fun getTitle(): String? {
        return mTitle;
    }

    override fun getPosition(): LatLng {
        return mPosition
    }


    fun changeFavourite(isFavourite: Boolean){
        this.mIsFavourite = isFavourite
    }
}


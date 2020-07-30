package com.ey.hotspot.ui.home.fragment

import com.ey.hotspot.ui.home.models.WifiInfoModel

interface WifiInfoDialogClickListener {
    fun onClickRateNow(data: WifiInfoModel)
    fun onClickReport(data: WifiInfoModel)
    fun onClickAddFavourite(data: WifiInfoModel)
    fun onClickNavigate(data: WifiInfoModel)
    fun onClickShare(data: WifiInfoModel)
}
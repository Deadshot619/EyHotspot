package com.ey.hotspot.ui.favourite

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityFavouriteBinding

class FavouriteActivity : BaseActivity<ActivityFavouriteBinding,FavouriteActivityViewModel>() {


    override fun getLayoutId(): Int {

        return R.layout.activity_favourite
    }

    override fun getViewModel(): Class<FavouriteActivityViewModel> {
        return FavouriteActivityViewModel::class.java
    }

    override fun onBinding() {
    }

}
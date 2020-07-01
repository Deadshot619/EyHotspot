package com.ey.hotspot.ui.favourite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityFavouriteBinding

class FavouriteActivity : BaseActivity<ActivityFavouriteBinding,FavouriteViewModel>() {


    override fun getLayoutId(): Int {

        return R.layout.activity_favourite
    }

    override fun getViewModel(): Class<FavouriteViewModel> {
        return FavouriteViewModel::class.java
    }

    override fun onBinding() {
    }

}
package com.ey.hotspot.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityProfileBinding

class ProfileActivity : BaseActivity<ActivityProfileBinding,ProfileViewModel>(){


    override fun getLayoutId(): Int {

        return R.layout.activity_profile
    }

    override fun getViewModel(): Class<ProfileViewModel> {
        return ProfileViewModel::class.java
    }

    override fun onBinding() {
    }

}
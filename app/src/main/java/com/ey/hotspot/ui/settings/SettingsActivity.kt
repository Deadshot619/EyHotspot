package com.ey.hotspot.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity<ActivitySettingsBinding, SettingsActivityViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_settings
    }

    override fun getViewModel(): Class<SettingsActivityViewModel> {
        return SettingsActivityViewModel::class.java
    }

    override fun onBinding() {
    }

}
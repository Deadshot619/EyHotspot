package com.ey.hotspot.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityLoginSelectionBinding

class LoginSelectionActivity : BaseActivity() {


    private lateinit var binding: ActivityLoginSelectionBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_selection)
    }
}
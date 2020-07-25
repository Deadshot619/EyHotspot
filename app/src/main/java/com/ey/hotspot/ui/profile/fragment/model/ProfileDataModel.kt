package com.ey.hotspot.ui.profile.fragment.model

data class ProfileDataModel (
    var firstName: String = "",
    var lastName: String = "",
    var countryCode: Int = -1,
    var mobileNo: String = "",
    var emailId: String = "",
    var password:String ="",
    var confirmPassword:String=""

)
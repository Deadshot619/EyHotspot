package com.ey.hotspot.ui.search.submitcomplaint

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.SubmitComplaintFragmentBinding
import com.ey.hotspot.ui.search.searchlist.SearchListFragment
import com.ey.hotspot.ui.search.searchlist.model.SearchList
import kotlinx.android.synthetic.main.submit_complaint_fragment.*

class SubmitComplaintFragment :
    BaseFragment<SubmitComplaintFragmentBinding, SubmitComplaintViewModel>() {

    val languageList = arrayListOf<String>()

    var spinnerlanguages: Spinner? = null
    var textView_languages: TextView? = null


    companion object {
        fun newInstance() = SubmitComplaintFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.submit_complaint_fragment
    }

    override fun getViewModel(): Class<SubmitComplaintViewModel> {
        return SubmitComplaintViewModel::class.java
    }

    override fun onBinding() {

        spinnerlanguages = this.complaintSpinner
        languageList.add("Slow")
        languageList.add("Fast")
        languageList.add("Disconnected")
        spinnerlanguages!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                textView_languages!!.text = "Selected language: " + languageList[position]

            }

        }

        val aa = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            languageList
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerlanguages!!.setAdapter(aa)


    }


}
package com.ey.hotspot.ui.speed_test.wifi_log_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.network.response.WifispeedtestData
import com.ey.hotspot.utils.extention_functions.extractTimeFromDateTime
import com.ey.hotspot.utils.extention_functions.extractspeed
import kotlinx.android.synthetic.main.fragment_wifi_log.view.*

class WifiLogSpeedTestAdapter(
    var speeddata: List<WifispeedtestData>,
    var login_time: String
) :
    RecyclerView.Adapter<WifiLogSpeedTestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.speed_test_wifilogs_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return speeddata.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(speeddata[position], login_time)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val tvstartSpeedValue = view.tv_start_speed_value
        val tvendSpeedValue = view.tv_end_speed_value

        fun bind(wifidata: WifispeedtestData, logintime: String) {

            tvstartSpeedValue.text = logintime
            if (!wifidata?.average_speed.toString().equals("null")) {
                /*tvendSpeedValue.text =
                    "${wifidata?.average_speed.toString().extractspeed()} mbps"*/
                tvendSpeedValue.text =
                    "${wifidata?.average_speed} mbps"
            } else {
                tvendSpeedValue.text = "-"
            }


        }
    }


}
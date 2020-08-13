package com.ey.hotspot.ui.speed_test.wifi_log_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.network.response.WifispeedtestData
import com.ey.hotspot.utils.extention_functions.extractTimeFromDateTime
import kotlinx.android.synthetic.main.fragment_wifi_log.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WifiLogSpeedTestAdapter(
    var speeddata: List<WifispeedtestData>
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
        holder.bind(speeddata[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val tvstartSpeedValue = view.tv_start_speed_value
        val tvendSpeedValue = view.tv_end_speed_value

        fun bind(wifidata: WifispeedtestData) {

            tvstartSpeedValue.text = getTime(wifidata.created_at.extractTimeFromDateTime().toString())
            if (!wifidata?.average_speed.toString().equals("null")) {
                /*tvendSpeedValue.text =
                    "${wifidata?.average_speed.toString().extractspeed()} mbps"*/
                tvendSpeedValue.text =
                    "${wifidata?.average_speed} Mbps"
            } else {
                tvendSpeedValue.text = "-"
            }


        }
        private fun getTime(datestring: String?):String
        {
            var outputDateStr: String=""
            if (!datestring.equals("null")) {
                val inputFormat: DateFormat = SimpleDateFormat("hh:mm:ss", Locale.ENGLISH)
                val outputFormat: DateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
                val date: Date = inputFormat.parse(datestring)
                outputDateStr = outputFormat.format(date)
            }
            else
            {
                outputDateStr="-"
            }
            return outputDateStr;
        }
    }


}
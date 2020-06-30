package com.ey.hotspot.utils

import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import fr.bmartel.speedtest.inter.ISpeedTestListener
import fr.bmartel.speedtest.model.SpeedTestError

object SpeedTestUtils {
    fun calculateSpeed(
        onCompletedReport: (report: SpeedTestReport?) -> Unit,
        onProgressReport: (report: SpeedTestReport?) -> Unit,
        onErrorReport: (msg: String?) -> Unit
    ): SpeedTestSocket {
        return SpeedTestSocket().apply {
            addSpeedTestListener(object : ISpeedTestListener {
                override fun onCompletion(report: SpeedTestReport?) {
                    onCompletedReport(report)
                }

                override fun onProgress(percent: Float, report: SpeedTestReport?) {
                    onProgressReport(report)
                }

                override fun onError(speedTestError: SpeedTestError?, errorMessage: String?) {
                    onErrorReport(errorMessage)
                }
            })
        }
    }
}
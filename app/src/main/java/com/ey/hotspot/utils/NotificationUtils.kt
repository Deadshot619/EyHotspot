package com.ey.hotspot.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ey.hotspot.R


/*
*   This file contains extension function of Notification Manager, which will help
*   sending notification
*/


private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

lateinit var notificationManager: NotificationManager
val CHANNEL_ID = "serviceChannel"
val channel_name = "Wifi Check Channel"

val wifi_notification_key = "wifi_notification_key"



/**
 * Builds and delivers a notification.
 *
 * @param messageBody, notification text.
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(
    messageTitle: String = "",
    messageBody : String = "",
    channelId: String,
    applicationContext: Context
){


    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        channelId
    )
        .setContentTitle(messageTitle)
        .setContentText(messageBody)


    notify(NOTIFICATION_ID, builder.build())
}

/**
 * Builds and delivers a notification.
 *
 * @param messageBody, notification text.
 * @param context, activity context.
 */
fun getNotification(
    applicationContext: Context,
    messageTitle: String = "",
    messageBody: String = "",
    channelId: String
): Notification{


    // Build the notification
    return NotificationCompat.Builder(
        applicationContext,
        channelId
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(messageTitle)
        .setContentText(messageBody)
        .build()
}

/**
 * Create channel id for Android O & above
 */
fun createNotificationChannel(context: Context, channelId: String, channelName: String) {

    /*
     * If the device is O & above, then create a channel
     * else create normal notification manager.
     */

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(false)
//            notificationChannel.description = "Time for breakfast"


        //Create Notification Manager
        notificationManager = context.getSystemService(
            NotificationManager::class.java
        )
        //Add channel to Notification Manager
        notificationManager.createNotificationChannel(notificationChannel)

    } else {

        notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
    }
}

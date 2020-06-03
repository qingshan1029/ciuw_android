package com.ciuwapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.media.RingtoneManager
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import com.ciuwapp.R
import com.ciuwapp.activities.CalendarActivity
import com.ciuwapp.activities.HomeActivity
import com.ciuwapp.activities.MessageActivity
import com.ciuwapp.prefs.PrefsManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import me.leolin.shortcutbadger.ShortcutBadger
import org.json.JSONObject


class FirebaseMessageService : FirebaseMessagingService() {

    val TAG = String::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")


        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            // Compose and show notification
            if (!remoteMessage.data.isNullOrEmpty()) {
                try {
                    val jsonObject = JSONObject(remoteMessage.data.toString())
                    val jsonString = jsonObject.get("data").toString()
                    val jsonData = JSONObject(jsonString)
                    val title = jsonData.get("title").toString()
                    val msg = jsonData.get("message").toString()
                    val post_id = jsonData.get("payload").toString()
                    launchNotification(title, msg, post_id)
                }
                catch (e: Exception){

                }
            }

        }
    }
    // NOTE: String not String?
    override fun onNewToken(token: String){

    }

    private fun launchNotification(title:String?, messageBody: String?, post_id: String?) {

        lateinit var intent: Intent
        val postId = post_id?.toInt()

        var messageBudges = PrefsManager.newInstance(this).getMessageBadges()
        var eventBudges = PrefsManager.newInstance(this).getEventBadges()

        if(title?.toUpperCase()!!.contains("MESSAGE")){
            intent = Intent(this, MessageActivity::class.java)
            if(title?.toUpperCase().contains("CREATED")) {
                messageBudges++
                PrefsManager.newInstance(this).setMessageBadges(messageBudges)
            }
        }
        else if(title?.toUpperCase().contains("EVENT")) {
            intent = Intent(this, CalendarActivity::class.java)
            if(title?.toUpperCase().contains("CREATED")) {
                eventBudges++
                PrefsManager.newInstance(this).setEventBadges(eventBudges)
            }
        }
        else
            intent = Intent(this, HomeActivity::class.java)

        if(title?.toUpperCase().contains("CREATED"))
            ShortcutBadger.applyCount(this, messageBudges+eventBudges) //for 1.1.4+

//        intent.putExtra(HomeActivity.EXTRA_ID, postId)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val titleBold = SpannableString(title)
        titleBold.setSpan(StyleSpan(Typeface.BOLD), 0 ,title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val spanned = HtmlCompat.fromHtml(messageBody!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
/*
        val contentView = RemoteViews(packageName, R.layout.notification_layout)
        contentView.setImageViewResource(R.id.thumbnailImageView, R.mipmap.app_logo)
        contentView.setTextViewText(R.id.titleTextView, title)
        contentView.setTextViewText(R.id.contentTextView, spanned)
*/
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_app)
//            .setLargeIcon(
//                BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo))
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentTitle(titleBold)
            .setContentText(spanned)
//            .setNumber(120)
//            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//            .setCustomBigContentView(contentView)
//            .setCustomContentView(contentView)
            .setContentIntent(pendingIntent)
        // Since android Oreo notification channel is needed.
        // https://developer.android.com/training/notify-user/build-notification#Priority
        val notification = notificationBuilder.build()
        notificationManager.notify(100, notification)


    }

}
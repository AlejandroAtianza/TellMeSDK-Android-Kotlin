package com.atianza.tellmesdk

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import java.net.HttpURLConnection
import java.net.URL

object TellMe {
    private const val TAG = "TellMe"
    private var currentToken: String? = null

    init {
        println("TellMeSDK - initialization")
    }

    public fun configure(activity: Activity, context: Context) {
        firebaseConfig(activity, context)
    }

    fun getStoredToken(context: Context): String? {
        val retrievedToken = SharedPreferencesManager.getSomeStringValue(context)
        //Log.d("TAG", "XXX - Registered Token: $retrievedToken")
        return retrievedToken
    }

    private fun saveToken(context: Context, newToken: String) {
        SharedPreferencesManager.clearSharedPreferences(context)
        SharedPreferencesManager.setSomeStringValue(context, newToken)
        //Log.d("TAG", "XXX - Registered Token: $newToken")
    }

    private fun firebaseConfig(activity: Activity, context: Context) {
        if (getStoredToken(context) == null) {
            //Log.d("TAG", "XXX - 1st Time")
            //saveToken(context, currentToken!!)

            if (checkGooglePlayServices(context)) {
                // [START retrieve_current_token]
                FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            //Log.w(TAG, getString(R.string.token_error), task.exception)
                            return@OnCompleteListener
                        }

                        // Get new Instance ID token
                        var token = task.result?.token
                        currentToken = token
                        saveToken(context, token!!)
                        registerToken()

                        // Log and toast
                        //val msg = getString(R.string.token_prefix, token)
                        Log.d(TAG, "Token: $token")
                        //Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                    })
                // [END retrieve_current_token]
            } else {
                //You won't be able to send notifications to this device
                Log.w(TAG, "Device doesn't have google play services")
            }


        } else {
            //Log.d("TAG", "XXX - Not 1st Time")
            //getStoredToken(context)
        }

        val bundle = activity.intent.extras
        if (bundle != null) { //bundle must contain all info sent in "data" field of the notification
            //text_view_notification.text = bundle.getString("text")
            val bundle = bundle.getString("text")
            Log.w(TAG, "Message bundle: $bundle")
        }
    }

    val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            //text_view_notification.text = intent.extras?.getString("message")
            val message = intent.extras?.getString("message")
            Log.w(TAG,"MessageReceiver: $message")
        }
    }

    private fun checkGooglePlayServices(context: Context): Boolean {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        return if (status != ConnectionResult.SUCCESS) {
            Log.e(TAG, "Error")
            // ask user to update google play services.
            false
        } else {
            Log.i(TAG, "Google play services updated")
            true
        }
    }

    fun registerToken() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())

        val url = URL("https://testing.proxy.beeceptor.com/app/device/register")
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET
            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")
        }
    }

    fun registerNotification() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())

        val url = URL("https://testing.proxy.beeceptor.com/app/device/notification")
        with(url.openConnection() as HttpURLConnection) {
            requestMethod = "GET"  // optional default is GET
            println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")
        }
    }

}
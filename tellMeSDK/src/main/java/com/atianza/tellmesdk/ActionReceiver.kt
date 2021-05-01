package com.atianza.tellmesdk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ActionReceiver  : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("TAG", "XXX - $intent")

        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();
        val action = intent.getStringExtra("action")
        if (action == "action1") {
            performAction1()
        } else if (action == "action2") {
            performAction2()
        }
        //This is used to close the notification tray
        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(it)
    }

    fun performAction1() {}
    fun performAction2() {}
}
package edu.washington.aaronioh.arewethereyet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("Receiver", "Started alarm receiver")
        val message = intent!!.getStringExtra("message")
        val phoneNum = intent!!.getStringExtra("phoneNum")
        val formattedNum = "(" + phoneNum.substring(0, 3) + ")" + phoneNum.substring(3, 6) + "-" + phoneNum.substring(6)
        SmsManager.getDefault().sendTextMessage(phoneNum, null, message, null, null)
        Log.i("Receiver", "Toast made")
    }
}
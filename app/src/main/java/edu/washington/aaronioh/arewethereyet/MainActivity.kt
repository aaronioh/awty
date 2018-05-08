package edu.washington.aaronioh.arewethereyet

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val message = findViewById<EditText>(R.id.editMessage) as EditText
        val phoneNum = findViewById<EditText>(R.id.editPhoneNum) as EditText
        val nag = findViewById<EditText>(R.id.editNag) as EditText
        val button = findViewById<Button>(R.id.buttonToggle) as Button

        button.setOnClickListener {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, AlarmReceiver::class.java)

            if (button.text == "Start") {
                if (message.text.isNotEmpty() && phoneNum.length() == 10 &&
                        nag.text.isNotEmpty() &&
                        nag.text.toString().toInt() > 0) {
                    button.text = "Stop"
                    Toast.makeText(this, "Alarm Started", Toast.LENGTH_SHORT).show()
                    intent.putExtra("message", message.text.toString())
                    intent.putExtra("phoneNum", phoneNum.text.toString())
                    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    val interval = nag.text.toString().toLong() * 1000 * 60
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent)
                } else {
                    if (message.text.isEmpty()) {
                        Toast.makeText(this, "Message is empty", Toast.LENGTH_SHORT).show()
                    } else if (phoneNum.length() != 10) {
                        Toast.makeText(this, "Invalid Phone Number", Toast.LENGTH_SHORT).show()
                    } else if (nag.text.toString().toIntOrNull() != null ||
                            nag.text.toString().toInt() > 0) {
                        Toast.makeText(this, "Nag Interval must be an integer > 0", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "All fields must be valid", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (button.text == "Stop") {
                button.text = "Start"
                Toast.makeText(this, "Alarm Stopped", Toast.LENGTH_SHORT).show()
                val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
                alarmManager.cancel(pendingIntent)
            }
        }
    }

    class AlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i("Receiver", "Started alarm receiver")
            val message = intent!!.getStringExtra("message")
            val phoneNum = intent!!.getStringExtra("phoneNum")
            val formattedNum = "(" + phoneNum.substring(0, 3) + ")" + phoneNum.substring(3, 6) + "-" + phoneNum.substring(6)
            Toast.makeText(context, formattedNum + ":" + message, Toast.LENGTH_SHORT).show()
            Log.i("Receiver", "Toast made")
        }
    }
}

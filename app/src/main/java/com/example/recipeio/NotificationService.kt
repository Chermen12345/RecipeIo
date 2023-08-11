package com.example.recipeio

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeio.model.User
import com.example.recipeio.utils.Consts
import com.example.recipeio.view.activities.HomeActivity
import com.example.recipeio.view.activities.MainActivity
import com.example.recipeio.view.adapters.NotAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class NotificationService(): Service() {
    val context: Context = MainActivity()
    private val listOfNots = arrayListOf<User>()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.d("tagg","hello")
        getNotifications()
    }
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    private fun getNotifications(){
        Consts.REF.child("users/${Consts.AUTH.currentUser!!.uid}/notifications").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listOfNots.clear()


                    for (ds in snapshot.children){
                        val value = ds.getValue(User::class.java)
                        value?.let {user ->
                            listOfNots.add(0,value)
                        }

                    }
                    if (listOfNots.isNotEmpty()){
                        showNotification(listOfNots[0])
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }
    private fun showNotification(user: User){
        val intent = Intent(context, HomeActivity::class.java)
        val pendingIntentToApp = PendingIntent.getActivity(context,1,intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, HomeActivity.CHANNEL_ID)
            .setContentTitle("Someone followed you")
            .setSmallIcon(R.drawable.recipe)
            .setContentText("${user.username} now following you")
            .setContentIntent(pendingIntentToApp)
            .build()
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED){
            val manager = NotificationManagerCompat.from(context)
            manager!!.notify(
                NOT_ID,notification!!
            )
        }
    }


    companion object{
        const val NOT_ID = 2
    }
}
package com.example.howlstagram_f16.navigation.util

import android.util.Log
import com.example.howlstagram_f16.navigation.model.PushDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class FcmPush {

    var JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    var url = "https://fcm.googleapis.com/fcm/send"
    var serverKey = "AIzaSyDJkXmpDemGhlKOGMSNZ_yO4FQXsNkZct8"
    var gson : Gson? = null
    var okHttpClient : OkHttpClient? = null
    companion object{
        var instance = FcmPush()
    }

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }
    fun sendMessage(destinationUid : String, title : String, message : String){
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if(task.isSuccessful){
                var token = task.result

                var pushDTO = PushDTO()
                pushDTO.to = token
                pushDTO.notification.title = title
                pushDTO.notification.body = message

                var body = gson?.toJson(pushDTO)?.toRequestBody(JSON)
                var request = body?.let {
                    Request.Builder()
                        .addHeader("Content-Type","application/json")
                        .addHeader("Authorization", "key=$serverKey")
                        .url(url)
                        .post(it)
                        .build()
                }

                if (request != null) {
                    okHttpClient?.newCall(request)?.enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {

                        }

                        override fun onResponse(call: Call, response: Response) {
                            println(response.body?.string())
                        }

                    })
                }
            }
        }
    }
}
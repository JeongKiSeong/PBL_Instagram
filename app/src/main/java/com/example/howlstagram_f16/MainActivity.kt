package com.example.howlstagram_f16

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.howlstagram_f16.navigation.*
import com.example.howlstagram_f16.navigation.util.FcmPush
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 권한 요청
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )

        bottom_navigation.setOnItemSelectedListener {
            setToolbarDefault()
            when (it.itemId) {
                R.id.action_home -> {
                    val detailViewFragment = DetailViewFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_content, detailViewFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_search -> {
                    val gridFragment = GridFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, gridFragment)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_add_photo -> {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        startActivity(Intent(this, AddPhotoActivity::class.java))
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.action_favorite_alarm -> {
                    val alarmFragment = AlarmFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content, alarmFragment)
                        .commit()
                    return@setOnItemSelectedListener true
                }
                R.id.action_account -> {
                    val userFragment = UserFragment()
                    val bundle = Bundle()
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    bundle.putString("destinationUid", uid)
                    userFragment.arguments = bundle

                    supportFragmentManager.beginTransaction().replace(R.id.main_content, userFragment)
                        .commit()
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        //Set default screen
        bottom_navigation.selectedItemId = R.id.action_home
    }

    override fun onStop() {
        super.onStop()

        FcmPush.instance.sendMessage("rG78toed3lMw2aTa7JMlkPlcbD92","test", "test")
    }

    private fun setToolbarDefault() {
        toolbar_username.visibility = View.GONE
        toolbar_btn_back.visibility = View.GONE
        toolbar_title_image.visibility = View.VISIBLE

    }
}
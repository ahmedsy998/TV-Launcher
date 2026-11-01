package com.minimal.tvlauncher

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<Button>(R.id.btnNativeSettings).setOnClickListener {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }

        findViewById<Button>(R.id.btnInstallApk).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "application/vnd.android.package-archive"
            }
            startActivity(Intent.createChooser(intent, "اختر حزمة APK لتثبيتها"))
        }
    }
}

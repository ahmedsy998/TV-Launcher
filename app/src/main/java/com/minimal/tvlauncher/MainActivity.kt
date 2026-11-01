package com.minimal.tvlauncher

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvPrimary: RecyclerView
    private lateinit var rvAll: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageView>(R.id.btnAndroidSettings).setOnClickListener {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }

        findViewById<ImageView>(R.id.btnCustomSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        rvPrimary = findViewById(R.id.rvPrimaryApps)
        rvAll = findViewById(R.id.rvAllApps)

        rvPrimary.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvAll.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        val activities = pm.queryIntentActivities(intent, 0)

        val primaryOrderedPackages = listOf(
            "com.google.android.youtube",
            "com.android.chrome",
            "com.android.vending",
            "com.google.android.documentsui",
            "com.android.documentsui"
        )

        val primaryList = mutableListOf<AppModel>()
        val secondaryList = mutableListOf<AppModel>()

        for (info in activities) {
            val pkg = info.activityInfo.packageName
            if (pkg == packageName) continue

            val app = AppModel(
                title = info.loadLabel(pm).toString(),
                packageName = pkg,
                icon = info.loadIcon(pm)
            )

            if (primaryOrderedPackages.contains(pkg)) {
                primaryList.add(app)
            } else {
                secondaryList.add(app)
            }
        }

        val sortedPrimary = primaryList.sortedBy { primaryOrderedPackages.indexOf(it.packageName) }

        rvPrimary.adapter = AppsAdapter(sortedPrimary) { view, pkg -> launchAppAnimated(view, pkg) }
        rvAll.adapter = AppsAdapter(secondaryList) { view, pkg -> launchAppAnimated(view, pkg) }
    }

    private fun launchAppAnimated(view: View, packageName: String) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.18f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.18f, 1.0f)
        val animSet = AnimatorSet().apply {
            duration = 150
            interpolator = OvershootInterpolator()
            playTogether(scaleX, scaleY)
        }
        animSet.start()

        view.postDelayed({
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                startActivity(launchIntent)
            }
        }, 140)
    }
}

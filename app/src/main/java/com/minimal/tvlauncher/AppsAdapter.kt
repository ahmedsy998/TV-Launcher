package com.minimal.tvlauncher

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppsAdapter(
    private val apps: List<AppModel>,
    private val onItemClick: (View, String) -> Unit
) : RecyclerView.Adapter<AppsAdapter.AppViewHolder>() {

    class AppViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val container: View = view.findViewById(R.id.cardContainer)
        val icon: ImageView = view.findViewById(R.id.appIcon)
        val title: TextView = view.findViewById(R.id.appName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_card, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = apps[position]
        holder.title.text = app.title
        holder.icon.setImageDrawable(app.icon)

        holder.container.setOnFocusChangeListener { view, hasFocus ->
            val targetScale = if (hasFocus) 1.1f else 1.0f
            val scaleX = ObjectAnimator.ofFloat(view, "scaleX", targetScale).setDuration(120)
            val scaleY = ObjectAnimator.ofFloat(view, "scaleY", targetScale).setDuration(120)
            AnimatorSet().apply {
                playTogether(scaleX, scaleY)
                start()
            }
        }

        holder.container.setOnClickListener {
            onItemClick(it, app.packageName)
        }
    }

    override fun getItemCount(): Int = apps.size
}

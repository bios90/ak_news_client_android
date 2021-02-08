package com.dimfcompany.aknewsclient.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dimfcompany.aknewsclient.R
import com.dimfcompany.aknewsclient.base.BaseActivity
import com.dimfcompany.aknewsclient.base.extensions.getColorMy
import com.dimfcompany.aknewsclient.base.extensions.runActionWithDelay
import com.dimfcompany.aknewsclient.logic.SharedPrefsManager
import com.dimfcompany.aknewsclient.ui.act_first.ActFirst
import com.dimfcompany.aknewsclient.ui.act_main.ActMain

class ActSplash : BaseActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        setNavStatus()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_splash)

        val img_splash: ImageView = findViewById(R.id.img_splash)
        Glide.with(img_splash).load(R.drawable.img_splash_new).into(img_splash)

        runActionWithDelay(lifecycleScope, 3000,
            {
                val intent: Intent
                if (SharedPrefsManager.pref_current_user.get().value != null)
                {
                    intent = Intent(this, ActMain::class.java)
                }
                else
                {
                    intent = Intent(this, ActFirst::class.java)
//                    intent = Intent(this, ActMain::class.java)
                }

                startActivity(intent)
                finish()
            })
    }

    fun setNavStatus()
    {
        is_full_screen = true
        is_below_nav_bar = true
        color_status_bar = getColorMy(R.color.transparent)
        color_nav_bar = getColorMy(R.color.transparent)
        is_light_status_bar = true
        is_light_nav_bar = true
    }
}
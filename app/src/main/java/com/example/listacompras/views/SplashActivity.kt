package com.example.listacompras.views

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.listacompras.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        hideStatusBar()

        Handler(Looper.getMainLooper()).postDelayed({
                  val intent = Intent(this, LoginActivity::class.java);
                  startActivity(intent);
              },2500)
    }

    fun hideStatusBar()
    {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
        actionBar?.hide();
    }
}
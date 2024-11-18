package com.example.listacompras.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.listacompras.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(binding.root);

        hideStatusBar();

        binding.shopListId.setOnClickListener {
            var i : Intent = Intent(this, ShoppingListActivity::class.java);
            startActivity(i);
        }

        binding.toDoListId.setOnClickListener {
            var i : Intent = Intent(this, ToDoListActivity::class.java);
            startActivity(i);

        }

        binding.postLayout.setOnClickListener {
            var i : Intent = Intent(this, PostLayoutActivity::class.java);
            startActivity(i);
        }

    }

    fun hideStatusBar()
    {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
        actionBar?.hide();
    }
}
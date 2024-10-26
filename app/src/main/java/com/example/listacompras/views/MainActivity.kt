package com.example.listacompras.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.listacompras.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(binding.root);

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
}
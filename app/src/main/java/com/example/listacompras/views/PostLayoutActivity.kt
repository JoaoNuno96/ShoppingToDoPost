package com.example.listacompras.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listacompras.adapter.PostListAdatper
import com.example.listacompras.databinding.ActivityPostLayoutBinding
import com.example.listacompras.model.Post


class PostLayoutActivity : AppCompatActivity() {


    public var listaPost : ArrayList<Post> = ArrayList<Post>();

    private val binding by lazy{
        ActivityPostLayoutBinding.inflate(layoutInflater);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(binding.root);

        binding.registerPostButton.setOnClickListener {

            var user = binding.userName.text.toString();
            var email = binding.userEmail.text.toString();
            var titulo = binding.userTitle.text.toString();
            var text = binding.postText.text.toString();

            var post = Post(user,email,titulo,text);

            listaPost.add(post);

            //HORIZONTAL
            binding.postRecycleView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
            binding.postRecycleView.adapter = PostListAdatper(listaPost);


            binding.userName.setText("");
            binding.userEmail.setText("");
            binding.userTitle.setText("");
            binding.postText.setText("");
        }

    }
}
package com.example.listacompras.views

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listacompras.adapter.PostListAdatper
import com.example.listacompras.databinding.ActivityPostLayoutBinding
import com.example.listacompras.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PostLayoutActivity : AppCompatActivity() {


    public var listaPost : ArrayList<Post> = ArrayList<Post>();
    public var recover : Map<String,Any>? = null;
    public var count = 1;

    private val binding by lazy{
        ActivityPostLayoutBinding.inflate(layoutInflater);
    }

    private val auth by lazy{
        FirebaseAuth.getInstance();
    }

    private val db by lazy{
        FirebaseFirestore.getInstance();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(binding.root);
        hideStatusBar();
        loadData();

        binding.registerPostButton.setOnClickListener {

            var user = binding.userName.text.toString();
            var email = binding.userEmail.text.toString();
            var titulo = binding.userTitle.text.toString();
            var text = binding.postText.text.toString();

            addPost(user,email,titulo,text);

            //HORIZONTAL
            binding.postRecycleView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
            binding.postRecycleView.adapter = PostListAdatper(listaPost);


            binding.userName.setText("");
            binding.userEmail.setText("");
            binding.userTitle.setText("");
            binding.postText.setText("");
        }

    }

    fun loadData()
    {
        val userId = auth.currentUser?.uid;

        db.collection(userId.toString()).document("listaPost").get()
            .addOnSuccessListener {

                recover = it.getData();
                if(recover == null)
                {
                    Toast.makeText(applicationContext, "Nenhuns items da Base de Dados ", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    recover?.forEach { entry ->
                        Toast.makeText(applicationContext, "Dados a carregar de base de dados...", Toast.LENGTH_SHORT).show()

                        var values = entry.value.toString().split("#").toTypedArray();

                        addPostFromDataBase(values[0].toString(), values[1].toString(),values[2].toString(),values[3].toString());
                    }
                }
            }


        Handler().postDelayed({
            binding.postRecycleView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
            binding.postRecycleView.adapter = PostListAdatper(listaPost);

        },1500)
    }

    fun addPost(userParam : String, emailParam : String, tituloParam : String, textParam : String)
    {

        var userId = auth.currentUser?.uid.toString();

        //Adicionar Lista
        var post = Post(count,userParam,emailParam,tituloParam,textParam);
        listaPost.add(post);
        count++;

        //Adicionar Database
        var postInfo = "${post.name}#${post.email}#${post.titulo}#${post.texto}";

        var data = mapOf(
            post.id.toString() to postInfo
        );

        if(listaPost.size == 1)
        {
            db.collection(userId).document("listaPost").set(data);
        }
        else
        {
            db.collection(userId).document("listaPost").update(data);
        }

        binding.postRecycleView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        binding.postRecycleView.adapter = PostListAdatper(listaPost);

    }

    fun addPostFromDataBase(userParam : String, emailParam : String, tituloParam : String, textParam : String)
    {
        var post = Post(count,userParam,emailParam,tituloParam,textParam);
        listaPost.add(post);
        count++;
    }

    fun hideStatusBar()
    {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
        actionBar?.hide();
    }
}
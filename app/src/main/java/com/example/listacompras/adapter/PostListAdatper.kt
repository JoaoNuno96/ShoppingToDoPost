package com.example.listacompras.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listacompras.R
import com.example.listacompras.model.Post

class PostListAdatper(val listaPost : ArrayList<Post>) : RecyclerView.Adapter<PostListAdatper.PostViewHolder>() {

    class PostViewHolder(ItemView : View) : RecyclerView.ViewHolder(ItemView){
        val title : TextView = itemView.findViewById(R.id.post_title);
        val user : TextView = itemView.findViewById(R.id.dados_user);
        val text : TextView = itemView.findViewById(R.id.postText);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_post_list,parent,false);

        return PostViewHolder(view);
    }

    override fun getItemCount(): Int {
        return listaPost.size;
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val post = listaPost[position];

        holder.title.setText(post.titulo);
        holder.text.setText(post.texto);
        holder.user.setText("${post.name} - ${post.email}");

    }
}
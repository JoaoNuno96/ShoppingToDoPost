package com.example.listacompras.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listacompras.R
import com.example.listacompras.model.ShoppingItem

class ShoppingListAdapter(val shopplist : ArrayList<ShoppingItem>) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {

    class ShoppingViewHolder(ItemView : View) : RecyclerView.ViewHolder(ItemView){
        val textView : TextView = itemView.findViewById(R.id.text_modelo);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_shopping_list,parent,false);

        return ShoppingViewHolder(view);
    }

    override fun getItemCount(): Int {
        return shopplist.size;
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val item = shopplist[position];

        holder.textView.setText(item.toString());
    }
}
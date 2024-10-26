package com.example.listacompras.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listacompras.adapter.ShoppingListAdapter
import com.example.listacompras.data.ListaMock
import com.example.listacompras.databinding.ActivityShoppingListBinding
import com.example.listacompras.model.ShoppingItem

class ShoppingListActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityShoppingListBinding.inflate(layoutInflater);
    }
    public val lista = ArrayList<ShoppingItem>();
    public val listaShared : Map<Int,String> = mapOf();
    public var listSize = lista.size;
    public var flagVariable = false;
    public var count = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(binding.root);

        val mock = ListaMock();

        binding.recycleView.layoutManager = LinearLayoutManager(this);
        binding.recycleView.adapter = ShoppingListAdapter(lista);

        val sharedPreferences = this.getSharedPreferences("lista", Context.MODE_PRIVATE);
        val valor = sharedPreferences.getString("","").toString();

        if(flagVariable)
        {
            verificarShared(valor);
        }


        binding.botaoAdicionar.setOnClickListener {

            val adicionarItem = binding.textoAdicionar.text.toString();

            adicionarItems(adicionarItem,sharedPreferences);

            binding.textoAdicionar.setText("");
        }

        binding.botaoRemover.setOnClickListener {

            val removerIndex = binding.textoRemover.text.toString().toInt();

            removerItem(removerIndex);

            binding.textoRemover.setText("");
        }
    }

    fun adicionarItems(item : String, sp : SharedPreferences) {

        //ADICIONAR LISTA
        val shop_item = ShoppingItem(count,item)
        lista.add(shop_item);
        count++;

        flagVariable = true;
        //ADICIONAR ADAPTER LIST VIEW
//        val arrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, lista);
//        binding.listaDeItems.adapter = arrayAdapter;
        binding.recycleView.adapter = ShoppingListAdapter(lista);

        //ADICIONAR SHARED (EDITOR)
        val editor : SharedPreferences.Editor = sp.edit();
        editor.putString("${shop_item.id}","${shop_item.nome}");
        editor.apply();
    }


    fun removerItem(index : Int) {

        var item = lista.get(index-1);
        lista.remove(item);

        verificar();

//        val arrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, lista);
//        binding.listaDeItems.adapter = arrayAdapter;
        binding.recycleView.adapter = ShoppingListAdapter(lista);
    }

    fun verificar(){

        for(i in 0..lista.size-1){
            lista.get(i).id = lista.indexOf(lista.get(i)).toString().toInt() +1;
        }

//        val arrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, lista);
//        binding.listaDeItems.adapter = arrayAdapter;
        binding.recycleView.adapter = ShoppingListAdapter(lista);
    }

    fun verificarShared(valor : String){

        valor.toString()

        //ADICIONAR LISTA
        val shop_item = ShoppingItem(count,valor)
        lista.add(shop_item);
        count++;

        //ADICIONAR ADAPTER LIST VIEW
//        val arrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, lista);
//        binding.listaDeItems.adapter = arrayAdapter;
        binding.recycleView.adapter = ShoppingListAdapter(lista);
    }

}
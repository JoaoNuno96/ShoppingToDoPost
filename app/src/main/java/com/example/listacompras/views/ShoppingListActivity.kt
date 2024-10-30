package com.example.listacompras.views

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listacompras.adapter.ShoppingListAdapter
import com.example.listacompras.databinding.ActivityShoppingListBinding
import com.example.listacompras.model.ShoppingItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ShoppingListActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityShoppingListBinding.inflate(layoutInflater);
    }

    private val auth by lazy{
        FirebaseAuth.getInstance();
    }

    private val db by lazy{
        FirebaseFirestore.getInstance();
    }

    public var lista = ArrayList<ShoppingItem>();
    public var listaExemplo : ArrayList<String> = ArrayList<String>();

    public val shoppingItem : ShoppingItem? = null;
    public var flagVariable = false;
    public var recover : Map<String,Any>? = null;
    public var count = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(binding.root);

        loadData();

        binding.botaoAdicionar.setOnClickListener {

            val adicionarItem = binding.textoAdicionar.text.toString();

            val userId = auth.currentUser?.uid;

            adicionarItems(adicionarItem,userId.toString());

            binding.textoAdicionar.setText("");
        }

        binding.botaoRemover.setOnClickListener {

            val removerIndex = binding.textoRemover.text.toString().toInt();

            removerItem(removerIndex);

            binding.textoRemover.setText("");
        }
    }

    private fun adicionarItems(item : String, userId : String) {

        //ADICIONAR LISTA
        val shop_item = ShoppingItem(count,item)
        lista.add(shop_item);
        count++;

        //MANDA BASE DE DADOS
        var data = mapOf(
            shop_item.id.toString() to shop_item.nome
        );

        db.collection(userId).document("listaTarefas").update(data);

        binding.recycleView.layoutManager = LinearLayoutManager(this);
        binding.recycleView.adapter = ShoppingListAdapter(lista);

    }

    private fun adicionarItemsRecuperadosDeDB(item : String, userId : String)
    {
        //ADICIONAR LISTA
        val shop_item = ShoppingItem(count,item)
        lista.add(shop_item);
        count++;


        //MANDA BASE DE DADOS
        var data = mapOf(
            shop_item.id.toString() to shop_item.nome
        );

        db.collection(userId).document("listaTarefas").update(data);

    }

    fun removerItem(index : Int) {

        //REMOVER DA LISTA
        var item = lista.get(index-1);
        lista.remove(item);

        //REMOVER DB
        itemRemoveFromDataBase(index.toString());

        //REORDENAR
        verificar();

        binding.recycleView.layoutManager = LinearLayoutManager(this);
        binding.recycleView.adapter = ShoppingListAdapter(lista);
    }

    //REMOVER DADOS PELA BASE DE DADOS CONSOANTE O ID DO FIELD
    fun itemRemoveFromDataBase(userData : String)
    {
        val userId = auth.currentUser?.uid;

        val documentRef = db.collection(userId.toString()).document("listaTarefas");

        var removesDatas = hashMapOf<String,Any>(
            userData to FieldValue.delete(),
            )

        documentRef.update(removesDatas).addOnCompleteListener {
            Toast.makeText(applicationContext, "Remove Success", Toast.LENGTH_SHORT).show()
        }

    }

    fun loadData()
    {
        val userId = auth.currentUser?.uid;

        db.collection(userId.toString()).document("listaTarefas").get()
            .addOnSuccessListener {

                recover = it.getData();
//                Thread.sleep(4000);

                recover?.forEach { entry ->

                    adicionarItemsRecuperadosDeDB(entry.value.toString(),userId.toString());
                }

           }

        Toast.makeText(applicationContext, "Dados a carregar de base de dados...", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            binding.recycleView.layoutManager = LinearLayoutManager(this);
            binding.recycleView.adapter = ShoppingListAdapter(lista);

        },3000)

    }

    fun verificar(){

        for(i in 0..lista.size-1){
            lista.get(i).id = lista.indexOf(lista.get(i)).toString().toInt() +1;
        }

        binding.recycleView.layoutManager = LinearLayoutManager(this);
        binding.recycleView.adapter = ShoppingListAdapter(lista);
    }

}
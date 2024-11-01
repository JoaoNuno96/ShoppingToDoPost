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
    public var recover : Map<String,Any>? = null;
    public var count = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(binding.root);

        val userId = auth.currentUser?.uid;
        loadData();

        binding.carregarDb.setOnClickListener {

            val uId = auth.currentUser?.uid.toString();

            binding.exemplo.text = uId;
            db.collection(uId).document("joao");

        }

        binding.botaoAdicionar.setOnClickListener {

            val adicionarItem = binding.textoAdicionar.text.toString();

            adicionarItems(adicionarItem,userId.toString());

            binding.textoAdicionar.setText("");
        }

        binding.botaoRemover.setOnClickListener {

            val removerIndex = binding.textoRemover.text.toString().toInt();

            removerItem(removerIndex,userId.toString());

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

        if(lista.size == 1)
        {
            db.collection(userId).document("listaTarefas").set(data);
        }
        else
        {
            db.collection(userId).document("listaTarefas").update(data);
        }

        binding.recycleView.layoutManager = LinearLayoutManager(this);
        binding.recycleView.adapter = ShoppingListAdapter(lista);

    }

    private fun adicionarItemsRecuperadosDeDB(item : String, userId : String)
    {
        //ADICIONAR LISTA
        val shop_item = ShoppingItem(count,item)
        lista.add(shop_item);
        count++;

    }

    fun removerItem(index : Int,userId: String) {

        //REMOVER DA LISTA
        var item = lista.get(index-1);
        lista.remove(item);

        //REMOVER DB
        itemRemoveFromDataBase(index.toString());

        //REORDENAR
        verificar(userId);

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

        },1500)

    }

    //DA UM SET NO DOCUMENTO DA LISTA DE TAREFAS DE UMA MUTABLEMAP LISTA REAJUSTADA
    fun verificar(userId : String)
    {
        for(i in 0..lista.size-1)
        {
            lista.get(i).id = lista.indexOf(lista.get(i)).toString().toInt() +1;
        }

        var listaItensBaseDadosReorganizados = mutableMapOf<String, Any>()

        for(item in lista)
        {
            listaItensBaseDadosReorganizados.put(item.id.toString(),item.nome);
        }

        db.collection(userId).document("listaTarefas").set(listaItensBaseDadosReorganizados);


        binding.recycleView.layoutManager = LinearLayoutManager(this);
        binding.recycleView.adapter = ShoppingListAdapter(lista);
    }

}
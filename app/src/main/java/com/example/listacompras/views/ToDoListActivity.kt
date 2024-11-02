package com.example.listacompras.views

import android.R
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.listacompras.databinding.ActivityToDoListBinding
import com.example.listacompras.model.Tarefa
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ToDoListActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityToDoListBinding.inflate(layoutInflater);
    }

    private val auth by lazy{
        FirebaseAuth.getInstance();
    }

    private val db by lazy{
        FirebaseFirestore.getInstance();
    }

    public var bateriaRegistada = "";
    public var listaTarefa = ArrayList<Tarefa>();
    public var recover : Map<String,Any>? = null;
    public var count = 1;

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState);
        setContentView(binding.root);

        loadData();

        //ESCONDER CONTEUDO
        binding.botaoDesaparecer.visibility = View.GONE;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            window.insetsController?.hide(WindowInsets.Type.statusBars());
        }
        else
        {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        //APRESENTAR CONTEUDO
        binding.botaoAparecer.setOnClickListener {
            binding.linearSubir.animate().translationY(0F).setDuration(
                        resources.getInteger(R.integer.config_mediumAnimTime)
                            .toLong()
                    )
            binding.botaoAparecer.visibility = View.GONE;
            binding.botaoDesaparecer.visibility = View.VISIBLE;
        }

        binding.botaoDesaparecer.setOnClickListener {
            binding.linearSubir.animate().translationY(500F).setDuration(
                resources.getInteger(R.integer.config_mediumAnimTime)
                    .toLong()
            )
            binding.botaoAparecer.visibility = View.VISIBLE;
            binding.botaoDesaparecer.visibility = View.GONE;
        }


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        verificarNivelBateria();


        //_____LISTA TAREFAS_____//

        binding.adicionarTarefa.setOnClickListener {

            var nova = binding.novaTarefa.text.toString();
            addTarefa(nova);
            binding.novaTarefa.setText("");


        }

        binding.removerTarefa.setOnClickListener {

            var remover = binding.tarefaParaRemover.text.toString().toInt();

            removeTarefa(remover);

            binding.tarefaParaRemover.setText("");
        }

    }

    fun loadData()
    {
        val userId = auth.currentUser?.uid;

        db.collection(userId.toString()).document("listaTarefas").get()
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
                        adicionarItemsRecuperadosDeDB(entry.value.toString());
                    }
                }
            }


        Handler().postDelayed({

            adapterParaListas();

        },1500)

    }

    fun verificarNivelBateria(){

        val bateria: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    var nivel: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

                    var valor = nivel.toString().toInt();

                    if(valor <= 5)
                    {
                        visibilidadeNumSó(0);
                        binding.nivelBateria.text = nivel.toString() + "%";
                    }
                    else if(valor >= 6 && valor <= 15)
                    {
                        visibilidadeNumSó(1);
                        binding.nivelBateria.text = nivel.toString() + "%";
                    }
                    else if(valor >= 16 && valor <= 25)
                    {
                        visibilidadeNumSó(2);
                        binding.nivelBateria.text = nivel.toString() + "%";
                    }
                    else if(valor >= 26 && valor <= 38)
                    {
                        visibilidadeNumSó(3);
                        binding.nivelBateria.text = nivel.toString() + "%";
                    }
                    else if(valor >= 39 && valor <= 50)
                    {
                        visibilidadeNumSó(4);
                        binding.nivelBateria.text = nivel.toString() + "%";
                    }
                    else if(valor >= 51 && valor <= 64)
                    {
                        visibilidadeNumSó(5);
                        binding.nivelBateria.text = nivel.toString() + "%";
                    }
                    else if(valor >= 65 && valor <= 78)
                    {
                        visibilidadeNumSó(6);
                        binding.nivelBateria.text = nivel.toString() + "%";
                    }
                    else if(valor >= 79 && valor <= 100)
                    {
                        visibilidadeNumSó(7);
                        binding.nivelBateria.text = nivel.toString() + "%";
                    }


                }
            }
        }

        registerReceiver(bateria, IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    fun addTarefa(item : String){

        val userId = auth.currentUser?.uid.toString();

        //Adicionar ArrayList
        val task = Tarefa(count,item);
        listaTarefa.add(task);
        count++;

        //Adicionar banco de dados
        var data = mapOf(
            task.id.toString() to task.nome
        );

        if(listaTarefa.size == 1)
        {
            db.collection(userId).document("listaTarefas").set(data);
        }
        else
        {
            db.collection(userId).document("listaTarefas").update(data);
        }

        adapterParaListas();
    }

    private fun adicionarItemsRecuperadosDeDB(item : String)
    {
        //ADICIONAR LISTA
        val task = Tarefa(count,item);
        listaTarefa.add(task);
        count++;

    }

    fun removeTarefa(index : Int){

        val userId = auth.currentUser?.uid;

        //REMOVER LISTA
        listaTarefa.remove(listaTarefa.get(index-1));

        //REMOVER DB
        itemRemoveFromDataBase(index.toString());

        //REORDENAR
        verificar(userId.toString());

    }

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

    fun visibilidadeNumSó(valor : Int){

        if(valor == 0)
        {
            binding.bateria0.visibility = View.VISIBLE;
            binding.bateria1.visibility = View.GONE;
            binding.bateria2.visibility = View.GONE;
            binding.bateria3.visibility = View.GONE;
            binding.bateria4.visibility = View.GONE;
            binding.bateria5.visibility = View.GONE;
            binding.bateria6.visibility = View.GONE;
            binding.bateria7.visibility = View.GONE;
        }
        else if(valor == 1)
        {
            binding.bateria0.visibility = View.GONE;
            binding.bateria1.visibility = View.VISIBLE;
            binding.bateria2.visibility = View.GONE;
            binding.bateria3.visibility = View.GONE;
            binding.bateria4.visibility = View.GONE;
            binding.bateria5.visibility = View.GONE;
            binding.bateria6.visibility = View.GONE;
            binding.bateria7.visibility = View.GONE;
        }
        else if(valor == 2)
        {
            binding.bateria0.visibility = View.GONE;
            binding.bateria1.visibility = View.GONE;
            binding.bateria2.visibility = View.VISIBLE;
            binding.bateria3.visibility = View.GONE;
            binding.bateria4.visibility = View.GONE;
            binding.bateria5.visibility = View.GONE;
            binding.bateria6.visibility = View.GONE;
            binding.bateria7.visibility = View.GONE;
        }
        else if(valor == 3)
        {
            binding.bateria0.visibility = View.GONE;
            binding.bateria1.visibility = View.GONE;
            binding.bateria2.visibility = View.GONE;
            binding.bateria3.visibility = View.VISIBLE;
            binding.bateria4.visibility = View.GONE;
            binding.bateria5.visibility = View.GONE;
            binding.bateria6.visibility = View.GONE;
            binding.bateria7.visibility = View.GONE;
        }
        else if(valor == 4)
        {
            binding.bateria0.visibility = View.GONE;
            binding.bateria1.visibility = View.GONE;
            binding.bateria2.visibility = View.GONE;
            binding.bateria3.visibility = View.GONE;
            binding.bateria4.visibility = View.VISIBLE;
            binding.bateria5.visibility = View.GONE;
            binding.bateria6.visibility = View.GONE;
            binding.bateria7.visibility = View.GONE;
        }
        else if(valor == 5)
        {
            binding.bateria0.visibility = View.GONE;
            binding.bateria1.visibility = View.GONE;
            binding.bateria2.visibility = View.GONE;
            binding.bateria3.visibility = View.GONE;
            binding.bateria4.visibility = View.GONE;
            binding.bateria5.visibility = View.VISIBLE;
            binding.bateria6.visibility = View.GONE;
            binding.bateria7.visibility = View.GONE;
        }
        else if(valor == 6)
        {
            binding.bateria0.visibility = View.GONE;
            binding.bateria1.visibility = View.GONE;
            binding.bateria2.visibility = View.GONE;
            binding.bateria3.visibility = View.GONE;
            binding.bateria4.visibility = View.GONE;
            binding.bateria5.visibility = View.GONE;
            binding.bateria6.visibility = View.VISIBLE;
            binding.bateria7.visibility = View.GONE;
        }
        else if(valor == 7)
        {
            binding.bateria0.visibility = View.GONE;
            binding.bateria1.visibility = View.GONE;
            binding.bateria2.visibility = View.GONE;
            binding.bateria3.visibility = View.GONE;
            binding.bateria4.visibility = View.GONE;
            binding.bateria5.visibility = View.GONE;
            binding.bateria6.visibility = View.GONE;
            binding.bateria7.visibility = View.VISIBLE;
        }
    }

    fun adapterParaListas(){
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_list_item_1, listaTarefa);
        binding.listaView.adapter = arrayAdapter;
    }

    fun verificar(userId : String){

        for(i in 0..listaTarefa.size-1)
        {
            listaTarefa.get(i).id = listaTarefa.indexOf(listaTarefa.get(i)).toString().toInt() +1;
        }

        var listaItensBaseDadosReorganizados = mutableMapOf<String, Any>()

        for(item in listaTarefa)
        {
            listaItensBaseDadosReorganizados.put(item.id.toString(),item.nome);
        }

        db.collection(userId).document("listaTarefas").set(listaItensBaseDadosReorganizados);


        adapterParaListas();
    }



}
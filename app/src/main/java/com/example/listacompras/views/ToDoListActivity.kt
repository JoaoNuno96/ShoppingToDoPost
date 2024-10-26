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
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.listacompras.databinding.ActivityToDoListBinding
import com.example.listacompras.model.Tarefa

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ToDoListActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityToDoListBinding.inflate(layoutInflater);
    }

    public var bateriaRegistada = "";
    public var listaTarefa = ArrayList<Tarefa>();
    public var count = 1;

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState);
        setContentView(binding.root);

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

        listaTarefa.add(Tarefa(count,item));
        count++;

        adapterParaListas();
    }

    fun removeTarefa(idex : Int){

        listaTarefa.remove(listaTarefa.get(idex-1));

        verificar();
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

    fun verificar(){

        for(i in 0..listaTarefa.size-1){
            listaTarefa.get(i).id = listaTarefa.indexOf(listaTarefa.get(i)).toString().toInt() +1;
        }

        adapterParaListas();
    }



}
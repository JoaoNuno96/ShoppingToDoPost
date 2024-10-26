package com.example.listacompras.model

class Tarefa(var index : Int, var name : String) {

    public var id = index;
    public var nome = name;

    override fun toString(): String {
        return "${id} - ${nome}"
    }
}
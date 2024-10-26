package com.example.listacompras.model

class ShoppingItem(var idparam : Int,var name : String) {

    public var id = idparam;
    public var nome = name;

    override fun toString(): String {
        return "${id} - ${nome}";
    }

}
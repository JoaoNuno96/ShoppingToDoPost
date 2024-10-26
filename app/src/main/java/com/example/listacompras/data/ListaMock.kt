package com.example.listacompras.data

import com.example.listacompras.model.ShoppingItem

class ListaMock {

    var listaShop = ArrayList<ShoppingItem>();

    init{

            listaShop.add(ShoppingItem(1,"Nenhum item para apresentar"));
    }
}
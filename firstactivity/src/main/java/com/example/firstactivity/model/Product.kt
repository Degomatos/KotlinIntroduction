package com.example.firstactivity.model

import java.text.NumberFormat
import java.util.Locale
import javax.swing.text.NumberFormatter

data class Product(
    val id: Int,
    var name: String,
    var price: Double
){
    override fun toString(): String {
        val precoFormatado = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR")).format(price)
        return "Id do produto: $id, Nome: $name, Preço: R$ $precoFormatado"
    }
}
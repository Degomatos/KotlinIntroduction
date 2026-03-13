package com.example.firstactivity.repository

import com.example.firstactivity.model.Product

class ProductRepository {
    private val products = mutableListOf<Product>()

    fun save(product: Product) {
        products.add(product)
    }

    fun findAll() = products

    fun findById(id: Int) =
        products.find { it.id == id }

    fun delete(product: Product) {
        products.remove(product)
    }
}

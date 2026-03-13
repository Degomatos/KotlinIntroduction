package com.example.firstactivity.service

import com.example.firstactivity.model.Product
import com.example.firstactivity.repository.ProductRepository

class ProductService {
    private val products = ProductRepository()
    private var nextId = 1

    fun addProduct(name: String?, price: Double?) {
        val validName = name ?: "Produto não cadastrado"
        val validPrice = price ?: 0.0

        if (validName.isBlank()) {
            println("Erro: Nome do produto não pode estar vazio.")
            return
        }
        if (validPrice < 0) {
            println("Erro: Preço não pode ser negativo.")
            return
        }

        val product = Product(nextId++, validName, validPrice)
        products.save(product)
        println("Produto cadastrado com sucesso: ${product.toString()}")
    }

    fun listProducts() {
        if (products.findAll().isEmpty()) {
            println("Nenhum produto cadastrado.")
        } else {
            println("--- Lista de Produtos ---")
            products.findAll().forEach { println(it.toString()) }
        }
    }

    fun findProductById(id: Int): Product? {
        return products.findById(id)
    }

    fun updateProduct(id: Int, newName: String?, newPrice: Double?) {
        val product = findProductById(id)
        if (product != null) {
            product.name = newName ?: product.name
            product.price = newPrice ?: product.price
            println("Produto atualizado: $product")
        } else {
            println("Produto com ID $id não encontrado.")
        }
    }

    fun removeProduct(id: Int) {
        val product = findProductById(id)
        if (product != null) {
            products.delete(product)
            println("Produto removido com sucesso.")
        } else {
            println("Produto com ID $id não encontrado.")
        }
    }
}

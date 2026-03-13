package com.example.firstactivity

import com.example.firstactivity.repository.ProductRepository
import com.example.firstactivity.service.ProductService
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val manager = ProductService()
    var option: Int

    do {
        println("\n--- MENU ---")
        println("1. Cadastrar")
        println("2. Listar")
        println("3. Pesquisar")
        println("4. Alterar")
        println("5. Remover")
        println("6. Finalizar")
        print("Escolha uma opção: ")

        option = try {
            scanner.nextInt()
        } catch (e: Exception) {
            scanner.nextLine()
            -1
        }

        when (option) {
            1 -> {
                scanner.nextLine()
                print("Nome do produto: ")
                val name = scanner.nextLine()
                print("Preço do produto: ")
                val price = try {
                    scanner.nextDouble()
                } catch (e: Exception) {
                    scanner.nextLine()
                    -1.0
                }
                if (price > 0) {
                    manager.addProduct(name, price)
                } else {
                    println("Erro: preço inválido. O preço deve ser um numero, positivo e usar virgula.")
                }

            }

            2 -> manager.listProducts()
            3 -> {
                print("ID do produto para pesquisar: ")
                val id = try {
                    scanner.nextInt()
                } catch (e: Exception) {
                    -1
                }
                val product = manager.findProductById(id)
                if (product != null) {
                    println("Encontrado: ${product.toString()}")
                } else {
                    println("Produto não encontrado.")
                }
            }

            4 -> {
                manager.listProducts()
                print("ID do produto para alterar: ")
                val id = try {
                    scanner.nextInt()
                } catch (e: Exception) {
                    -1
                }
                val product = manager.findProductById(id)
                if (product == null) {
                    println("Não existe produto para o id digitado, tente novamente")
                    scanner.nextLine()
                    continue
                }
                scanner.nextLine()
                print("Novo nome (deixe vazio para manter): ")
                val name = scanner.nextLine().takeIf { it.isNotBlank() }
                print("Novo preço (ou -1 para manter): ")
                val priceInput = try {
                    scanner.nextDouble()
                } catch (e: Exception) {
                    -1.0
                }
                val price = if (priceInput >= 0) priceInput else null
                manager.updateProduct(id, name, price)
            }

            5 -> {
                print("ID do produto para remover: ")
                val id = try {
                    scanner.nextInt()
                } catch (e: Exception) {
                    -1
                }
                manager.removeProduct(id)
            }

            6 -> println("Finalizando o programa...")
            else -> println("Opção inválida!")
        }
    } while (option != 6)
}

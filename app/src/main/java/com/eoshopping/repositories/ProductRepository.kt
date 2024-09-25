package com.eoshopping.repositories

import Product
import com.eoshopping.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepository {


    val firestore = FirebaseFirestore.getInstance()
    suspend fun getProducts(): List<Product> {
        return try {
            val snapshot = firestore.collection("products").get().await()
            snapshot.toObjects(Product::class.java)
        } catch (e: Exception) {
            emptyList() // Handle the exception appropriately
        }
    }
}
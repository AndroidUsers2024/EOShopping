package com.eoshopping.repositories
import com.eoshopping.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    suspend fun getUser(id: String): User {
        return try {

            val document = firestore.collection("Users").document(id).get().await()
            if (document.exists()) {
                User(document.getString("name") ?: "", document.getString("email") ?: "",document.getString("password") ?: "",document.getString("mobile") ?: "",document.getString("age") ?: "",document.getString("imageUri") ?: "", )
            } else {
                User("","","","","","")
            }
//            document.toObject(User::class.java)!!.copy(user = document.id)
        }catch (e: Exception){
            User("","","","","","")
        }
    }

}
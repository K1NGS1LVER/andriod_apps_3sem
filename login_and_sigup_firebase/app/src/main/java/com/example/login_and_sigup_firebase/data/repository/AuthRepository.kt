package com.example.login_and_sigup_firebase.data.repository

import com.example.login_and_sigup_firebase.data.model.User
import com.example.login_and_sigup_firebase.utils.Constants.USERS_COLLECTION
import com.example.login_and_sigup_firebase.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    val currentUser = firebaseAuth.currentUser

    suspend fun login(email: String, password: String): Resource<Boolean> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login failed")
        }
    }

    suspend fun signUp(name: String, email: String, password: String): Resource<Boolean> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Resource.Error("User ID not found")
            
            val user = User(uid = uid, fullName = name, email = email)
            firestore.collection(USERS_COLLECTION).document(uid).set(user).await()
            
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Signup failed")
        }
    }

    suspend fun resetPassword(email: String): Resource<Boolean> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to send reset email")
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun getUserDetails(uid: String): Flow<Resource<User>> = callbackFlow {
        trySend(Resource.Loading)
        val subscription = firestore.collection(USERS_COLLECTION).document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Failed to fetch user"))
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val user = snapshot.toObject(User::class.java)
                    if (user != null) {
                        trySend(Resource.Success(user))
                    }
                }
            }
        awaitClose { subscription.remove() }
    }
}

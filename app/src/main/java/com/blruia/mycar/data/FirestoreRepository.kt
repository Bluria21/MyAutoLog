package com.blruia.mycar.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

    class FirestoreRepository @Inject constructor() {
    
        private val db = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()
    
        private fun getUserIdOrNull(): String? {
            return auth.currentUser?.uid
        }
    
        // ---------- Машины ----------
    
        suspend fun uploadCar(car: CarInfo) {
            val uid = getUserIdOrNull()
            if (uid == null) {
                Log.w("FirestoreRepository", "uploadCar: user not signed in — skipping upload")
                return
            }
            try {
                val carRef = db.collection("users")
                    .document(uid)
                    .collection("cars")
                    .document(car.id.toString())
    
                val carMap = mapOf(
                    "nickname" to car.nickname,
                    "brand" to car.brand,
                    "mileageCar" to car.mileageCar,
                    "yearCar" to car.yearCar,
                    "imageUri" to car.imageUri
                )
    
                carRef.set(carMap).await()
            } catch (e: Exception) {
                Log.e("FirestoreRepository", "uploadCar failed: ${e.message}", e)
                // можно перекинуть ошибку дальше или просто логировать
            }
        }
    
        suspend fun deleteCar(carId: Int) {
            val uid = getUserIdOrNull() ?: run {
                Log.w("FirestoreRepository", "deleteCar: user not signed in — skipping")
                return
            }
            try {
                val carRef = db.collection("users")
                    .document(uid)
                    .collection("cars")
                    .document(carId.toString())
    
                carRef.delete().await()
            } catch (e: Exception) {
                Log.e("FirestoreRepository", "deleteCar failed: ${e.message}", e)
            }
        }
    
        suspend fun downloadCarsAsync(): List<CarInfo> {
            val uid = getUserIdOrNull() ?: run {
                Log.w("FirestoreRepository", "downloadCarsAsync: user not signed in — returning empty list")
                return emptyList()
            }
            return try {
                val snapshot = db.collection("users")
                    .document(uid)
                    .collection("cars")
                    .get()
                    .await()
    
                snapshot.documents.map { doc ->
                    CarInfo(
                        id = doc.id.toIntOrNull() ?: 0,
                        nickname = doc.getString("nickname") ?: "",
                        brand = doc.getString("brand") ?: "",
                        mileageCar = doc.getString("mileageCar") ?: "",
                        yearCar = doc.getString("yearCar") ?: "",
                        imageUri = doc.getString("imageUri")
                    )
                }
            } catch (e: FirebaseFirestoreException) {
                Log.e("FirestoreRepository", "downloadCarsAsync firestore error: ${e.message}", e)
                emptyList()
            } catch (e: Exception) {
                Log.e("FirestoreRepository", "downloadCarsAsync error: ${e.message}", e)
                emptyList()
            }
        }
    
        // ---------- Записи ----------
    
        suspend fun uploadEntry(entry: Entry) {
            val uid = getUserIdOrNull()
            if (uid == null) {
                Log.w("FirestoreRepository", "uploadEntry: user not signed in — skipping upload")
                return
            }
            try {
                val entryRef = db.collection("users")
                    .document(uid)
                    .collection("cars")
                    .document(entry.carId.toString())
                    .collection("entries")
                    .document(entry.id.toString())
    
                val entryMap = mapOf(
                    "partKey" to entry.partKey,
                    "date" to entry.date,
                    "mileageHistory" to entry.mileageHistory,
                    "price" to entry.price,
                    "isResourceKey" to entry.isResourceKey
                )
    
                entryRef.set(entryMap).await()
            } catch (e: Exception) {
                Log.e("FirestoreRepository", "uploadEntry failed: ${e.message}", e)
            }
        }
    
        suspend fun deleteEntry(carId: Int, entryId: Int) {
            val uid = getUserIdOrNull() ?: run {
                Log.w("FirestoreRepository", "deleteEntry: user not signed in — skipping")
                return
            }
            try {
                val entryRef = db.collection("users")
                    .document(uid)
                    .collection("cars")
                    .document(carId.toString())
                    .collection("entries")
                    .document(entryId.toString())
    
                entryRef.delete().await()
            } catch (e: Exception) {
                Log.e("FirestoreRepository", "deleteEntry failed: ${e.message}", e)
            }
        }
    
        suspend fun downloadEntriesAsync(carId: Int): List<Entry> {
            val uid = getUserIdOrNull() ?: run {
                Log.w("FirestoreRepository", "downloadEntriesAsync: user not signed in — returning empty list")
                return emptyList()
            }
            return try {
                val snapshot = db.collection("users")
                    .document(uid)
                    .collection("cars")
                    .document(carId.toString())
                    .collection("entries")
                    .get()
                    .await()
    
                snapshot.documents.map { doc ->
                    Entry(
                        id = doc.id.toIntOrNull() ?: 0,
                        carId = carId,
                        partKey = doc.getString("partKey") ?: "",
                        date = doc.getString("date") ?: "",
                        mileageHistory = doc.getString("mileageHistory") ?: "",
                        price = doc.getString("price") ?: "",
                        isResourceKey = doc.getBoolean("isResourceKey") ?: true
                    )
                }
            } catch (e: FirebaseFirestoreException) {
                Log.e("FirestoreRepository", "downloadEntriesAsync firestore error: ${e.message}", e)
                emptyList()
            } catch (e: Exception) {
                Log.e("FirestoreRepository", "downloadEntriesAsync error: ${e.message}", e)
                emptyList()
            }
        }
    }
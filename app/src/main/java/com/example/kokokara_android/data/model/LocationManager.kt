package com.example.kokokara_android.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

data class LatLng(val latitude: Double, val longitude: Double)

class LocationManager(context: Context) {
    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): LatLng {
        val cts = CancellationTokenSource()
        Log.d("kokokara", "getCurrentLocation: 開始")
        return suspendCancellableCoroutine { cont ->
            fusedClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                .addOnSuccessListener { location ->
                    Log.d("kokokara", "getCurrentLocation: onSuccess location=$location")
                    if (location != null) {
                        cont.resume(LatLng(location.latitude, location.longitude))
                    } else {
                        Log.d("kokokara", "getCurrentLocation: location が null")
                        cont.resumeWithException(Exception("位置情報を取得できませんでした"))
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("kokokara", "getCurrentLocation: onFailure e=$e")
                    cont.resumeWithException(e)
                }
            cont.invokeOnCancellation { cts.cancel() }
        }
    }
}
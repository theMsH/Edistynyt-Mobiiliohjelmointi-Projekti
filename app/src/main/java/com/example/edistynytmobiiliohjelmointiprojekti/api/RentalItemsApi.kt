package com.example.edistynytmobiiliohjelmointiprojekti.api

import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemsRes
import retrofit2.http.GET
import retrofit2.http.Path


private val retrofit = createClient()

val rentalItemsService: RentalItemsApi = retrofit.create(RentalItemsApi::class.java)

interface RentalItemsApi {
    // GET::http://localhost:8000/api/v1/category/{categoryId}/items
    @GET("category/{categoryId}/items")
    suspend fun getRentalItems(@Path("categoryId") categoryId: Int) : RentalItemsRes
}

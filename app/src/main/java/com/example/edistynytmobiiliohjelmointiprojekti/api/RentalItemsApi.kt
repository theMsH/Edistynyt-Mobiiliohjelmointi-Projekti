package com.example.edistynytmobiiliohjelmointiprojekti.api

import com.example.edistynytmobiiliohjelmointiprojekti.model.RentItemReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItem
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemPostReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemsByCategory
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemsRes
import com.example.edistynytmobiiliohjelmointiprojekti.model.UpdateRentalItemNameReq
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


val rentalItemsService: RentalItemsApi = retrofit.create(RentalItemsApi::class.java)

interface RentalItemsApi {
    // GET::http://localhost:8000/api/v1/category/{categoryId}/items
    @GET("category/{categoryId}/items")
    suspend fun getRentalItems(@Path("categoryId") categoryId: Int) : RentalItemsRes

    @GET("rentalitem/{rentalItemId}")
    suspend fun getRentalItem(@Path("rentalItemId") rentalItemId: Int) : RentalItem

    @Headers("Content-Type: application/json")
    @PUT("rentalitem/{rentalItemId}")
    suspend fun updateRentalItemName(
        @Path("rentalItemId") rentalItemId: Int,
        @Body updateRentalItemNameReq: UpdateRentalItemNameReq
    ) : RentalItem

    @Headers("Content-Type: application/json")
    @POST("category/{categoryId}/items")
    suspend fun postRentalItem(
        @Path("categoryId") categoryId: Int,
        @Body rentalItemPostReq: RentalItemPostReq
    ): RentalItemsByCategory

    @Headers("Content-Type: application/json")
    @POST("rentalitem/{rental_item_id}/rent")
    suspend fun rentItem(
        @Path("rental_item_id") rentalItemId: Int,
        @Body rentItemReq: RentItemReq
    )

}


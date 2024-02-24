package com.example.edistynytmobiiliohjelmointiprojekti.api

import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoriesRes
import retrofit2.http.GET


private val retrofit = createClient()

val categoriesService = retrofit.create(CategoriesApi::class.java)

interface CategoriesApi {
    // GET::http://localhost:8000/api/v1/category/
    @GET("category/")
    suspend fun getCategories() : CategoriesRes

}

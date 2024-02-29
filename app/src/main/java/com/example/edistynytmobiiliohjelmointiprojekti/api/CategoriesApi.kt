package com.example.edistynytmobiiliohjelmointiprojekti.api

import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoriesRes
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryRes
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


private val retrofit = createClient()

val categoriesService = retrofit.create(CategoriesApi::class.java)

interface CategoriesApi {
    // GET::http://localhost:8000/api/v1/category/
    @GET("category/")
    suspend fun getCategories() : CategoriesRes

    @POST("category/")
    suspend fun postCategory(@Body categoryReq: CategoryReq) : CategoryRes

    @DELETE("category/{categoryId}")
    suspend fun deleteCategoryById(@Path("categoryId") categoryId: Int)


}

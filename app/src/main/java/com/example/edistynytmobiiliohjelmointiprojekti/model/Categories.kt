package com.example.edistynytmobiiliohjelmointiprojekti.model

import com.google.gson.annotations.SerializedName


data class CategoriesState(
    val list: List<CategoryItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

/* GET 200:
{
    "categories": [
    {
        "category_id": 1,
        "category_name": "Kategoria 1"
    }
    ]
}
*/

data class CategoryItem(
    @SerializedName("category_id")
    val categoryId: Int = 0,
    @SerializedName("category_name")
    val categoryName: String = ""
)

data class CategoriesRes(
    val categories: List<CategoryItem> = emptyList()
)

/* POST 200:
{
    "category_name" : "Kategoria 1"
}
*/

data class CategoryState(
    val categoryName: String = "",
    val loading: Boolean = false,
    val error: String? = null
)

data class CategoryReq(
    @SerializedName("category_name")
    val categoryName: String = ""
)

data class CategoryRes(
    val category : CategoryItem = CategoryItem()
)

data class PostCategoryRes(
    @SerializedName("category_name")
    val categoryName: String = "",
    @SerializedName("category_id")
    val categoryId: Int = 0
)

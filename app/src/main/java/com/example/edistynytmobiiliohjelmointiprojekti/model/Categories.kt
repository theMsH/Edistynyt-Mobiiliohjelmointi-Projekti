package com.example.edistynytmobiiliohjelmointiprojekti.model

data class CategoriesState(
    val list: List<CategoryItem> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

data class CategoryItem(
    val categoryId: Int = 0,
    val categoryName: String = ""
)

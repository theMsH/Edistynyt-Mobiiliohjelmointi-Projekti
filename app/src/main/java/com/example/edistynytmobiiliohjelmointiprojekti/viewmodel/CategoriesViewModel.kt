package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoriesState
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    private val _categoriesState = mutableStateOf(CategoriesState())
    val categoriesState: State<CategoriesState> = _categoriesState

    init {
        getCategories()
    }

    private suspend fun fakeLoading() {
        delay(1000)
    }

    private fun getCategories() {
        viewModelScope.launch {
            _categoriesState.value =_categoriesState.value.copy(loading = true)
            fakeLoading()
            _categoriesState.value =_categoriesState.value.copy(
                list = listOf<CategoryItem>(
                    CategoryItem(categoryId = 1, categoryName = "Kategoria 1"),
                    CategoryItem(categoryId = 2, categoryName = "Kategoria 2"),
                    CategoryItem(categoryId = 3, categoryName = "Kategoria 3"),
                ),
                loading = false
            )
        }
    }

    fun deleteCategory(category: CategoryItem) {
        // Filteröidään kategorialistaan kaikki kategoriat, joiden id on eri kuin valittu id.
        val categories =_categoriesState.value.list.filter {
            // Jos palautuu false, se filteröityy pois
            it.categoryId != category.categoryId
        }

        // Määritellään se uudeksi listaksi
        _categoriesState.value =_categoriesState.value.copy(list = categories)
    }

}
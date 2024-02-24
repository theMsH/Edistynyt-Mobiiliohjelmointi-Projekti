package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.categoriesService
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoriesState
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryItem
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    private val _categoriesState = mutableStateOf(CategoriesState())
    val categoriesState: State<CategoriesState> = _categoriesState

    private val _categoryState = mutableStateOf(CategoryState())
    val categoryState: State<CategoryState> = _categoryState

    val showDeleteDialog = mutableStateOf(false)
    val showCreateCategoryDialog = mutableStateOf(false)
    val selectedCategoryItem = mutableStateOf(CategoryItem())


    init {
        getCategories()
    }

    private suspend fun fakeLoading() {
        delay(1000)
    }

    private fun getCategories() {
        viewModelScope.launch {
            try {
                _categoriesState.value = _categoriesState.value.copy(loading = true)

                val categoriesRes = categoriesService.getCategories()
                _categoriesState.value = _categoriesState.value.copy(list = categoriesRes.categories)
            }
            catch (e: Exception) {
                _categoriesState.value = _categoriesState.value.copy(error = e.toString())

            }
            finally {
                _categoriesState.value = _categoriesState.value.copy(loading = false)
            }
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                _categoriesState.value = _categoriesState.value.copy(loading = true)
                categoriesService.deleteCategory(categoryId)
            }
            catch (e: Exception) {
                _categoriesState.value = _categoriesState.value.copy(error = e.toString())
            }
            finally {
                _categoriesState.value = _categoriesState.value.copy(loading = false)
                getCategories()
            }
        }
        /*
        // Filteröidään kategorialistaan kaikki kategoriat, joiden id on eri kuin valittu id.
        val categories =_categoriesState.value.list.filter {
            // Jos palautuu false, se filteröityy pois
            it.categoryId != category.categoryId
        }

        // Määritellään se uudeksi listaksi
        _categoriesState.value =_categoriesState.value.copy(list = categories)
        */
    }

    fun setCategoryName(newCategoryName: String) {
        _categoryState.value = _categoryState.value.copy(categoryName = newCategoryName)
    }

    fun postCategory() {
        viewModelScope.launch {
            try {
                _categoryState.value = _categoryState.value.copy(loading = true)
                categoriesService.postCategory(CategoryReq(_categoryState.value.categoryName))
            }
            catch (e: Exception) {
                _categoriesState.value = _categoriesState.value.copy(error = e.toString())
            }
            finally {
                _categoriesState.value = _categoriesState.value.copy(loading = false)
                getCategories()
            }
        }
    }

    fun resetError() {
        _categoriesState.value = _categoriesState.value.copy(error = null)
    }


}
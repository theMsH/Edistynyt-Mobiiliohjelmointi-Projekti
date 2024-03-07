package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.categoriesService
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryState
import kotlinx.coroutines.launch

class EditCategoryViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    // Haetaan välitetystä routesta id. Se tunnistaa routen stringistä {id} muuttujana.
    // Sieltä voi palautua myös null, joten "elvis operatorilla" ( ?: ) asetetaan se 0.
    val id = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0

    private val _categoryState = mutableStateOf(CategoryState())
    val categoryState: State<CategoryState> = _categoryState

    // Used for static TopAppBar title
    var categoryTitle = ""
    // Used for checking duplicate categoryNames
    var categoryNamesList: List<String> = emptyList()


    init {
        // Init when id is found to avoid 422 API call in CreateNewCategoryDialog()
        if (id != 0) {
            getCategoryById()
        }
        getCategoryNamesList()
    }

    fun setCategoryName(newCategoryName: String) {
        _categoryState.value = _categoryState.value.copy(categoryName = newCategoryName)
    }

    fun updateCategoryById(goToCategoriesScreen: () -> Unit) {
        viewModelScope.launch {
            try {
                _categoryState.value = _categoryState.value.copy(loading = true)

                categoriesService.updateCategoryById(
                    id,
                    CategoryReq(_categoryState.value.categoryName)
                )
                goToCategoriesScreen()
            }
            catch (e: Exception) {
                Log.d("error getCategoryById()", "$e")
                _categoryState.value = _categoryState.value.copy(error = e.toString())
            }
            finally {
                _categoryState.value = _categoryState.value.copy(loading = false)
            }
        }
    }

    private fun getCategoryById() {
        viewModelScope.launch {
            try {
                _categoryState.value = _categoryState.value.copy(loading = true)

                val response = categoriesService.getCategoryById(id)
                _categoryState.value =
                    _categoryState.value.copy(categoryName = response.category.categoryName)

                categoryTitle = _categoryState.value.categoryName
            }
            catch (e: Exception) {
                Log.d("error getCategoryById()", "$e")
                _categoryState.value = _categoryState.value.copy(error = e.toString())
            }
            finally {
                _categoryState.value = _categoryState.value.copy(loading = false)
            }
        }
    }


    private fun getCategoryNamesList() {
        viewModelScope.launch {
            try {
                val categoriesRes = categoriesService.getCategories()

                for (category in categoriesRes.categories) {
                    if (category.categoryName != categoryTitle) {
                        categoryNamesList += category.categoryName
                    }
                }
            }
            catch (e: Exception) {
                Log.d("error getAvailableNames()", "$e")
            }
        }
    }

}
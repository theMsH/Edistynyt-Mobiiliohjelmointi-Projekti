package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.categoriesService
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryItem
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryState
import kotlinx.coroutines.launch

class EditCategoryViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _categoryState = mutableStateOf(CategoryState())
    val categoryState: State<CategoryState> = _categoryState

    private val _nonValidNamesList = savedStateHandle.get<String>("nonValidNamesList") ?: ""
    private val _categoryId = savedStateHandle.get<String>("categoryId")?.toIntOrNull() ?: 0


    init {
        getCategoryById()
        initNonValidNamesList()
    }

    // Used for checking duplicate categoryNames
    private fun initNonValidNamesList() {
        val nonValidNamesString = _nonValidNamesList.removeSurrounding("[","]")
        val nonValidNamesList = nonValidNamesString.split(", ")
        _categoryState.value = _categoryState.value.copy(nonValidNamesList = nonValidNamesList)
    }

    fun setCategoryName(newCategoryName: String) {
        _categoryState.value = _categoryState.value.copy(
            categoryItem = CategoryItem(
                _categoryId,
                newCategoryName
            )
        )
    }

    fun setDone(done: Boolean) {
        _categoryState.value = _categoryState.value.copy(done = done)
    }

    private fun getCategoryById() {
        viewModelScope.launch {
            try {
                _categoryState.value = _categoryState.value.copy(loading = true)

                val response = categoriesService.getCategoryById(_categoryId)
                _categoryState.value = _categoryState.value.copy(
                        categoryItem = CategoryItem(
                            response.category.categoryId,
                            response.category.categoryName
                        )
                    )
                _categoryState.value = _categoryState.value.copy(categoryTitle = response.category.categoryName)
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

    fun updateCategoryById() {
        viewModelScope.launch {
            try {
                _categoryState.value = _categoryState.value.copy(loading = true)

                categoriesService.updateCategoryById(
                    _categoryId,
                    CategoryReq(_categoryState.value.categoryItem.categoryName)
                )
                setDone(true)
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

}
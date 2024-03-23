package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edistynytmobiiliohjelmointiprojekti.api.categoriesService
import com.example.edistynytmobiiliohjelmointiprojekti.api.rentalItemsService
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoriesState
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryItem
import com.example.edistynytmobiiliohjelmointiprojekti.model.CategoryReq
import com.example.edistynytmobiiliohjelmointiprojekti.model.CreateState
import com.example.edistynytmobiiliohjelmointiprojekti.model.DeleteState
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemsState
import kotlinx.coroutines.launch
import java.util.Locale

class CategoriesViewModel : ViewModel() {
    private val _categoriesState = mutableStateOf(CategoriesState())
    val categoriesState: State<CategoriesState> = _categoriesState

    private val _deleteState = mutableStateOf(DeleteState())
    val deleteState: State<DeleteState> = _deleteState

    private val _createState = mutableStateOf(CreateState())
    val createState: State<CreateState> = _createState

    val showUnauthorizedDialog = mutableStateOf(false)


    init {
        getCategories()
    }

    fun setShowDeleteDialog(show: Boolean) {
        _deleteState.value = _deleteState.value.copy(showDialog = show)
    }

    fun setShowCreateDialog(show: Boolean) {
        _createState.value = _createState.value.copy(showDialog = show)
    }

    fun setSelectedCategory(categoryItem: CategoryItem) {
        _deleteState.value = _deleteState.value.copy(
            selectedId = categoryItem.categoryId,
            selectedName = categoryItem.categoryName
        )
    }

    fun setDeleteDone(done: Boolean) {
        _deleteState.value = _deleteState.value.copy(done = done)
    }

    fun clearError() {
        _categoriesState.value = _categoriesState.value.copy(error = null)
    }

    // Get list of names used for categories. Used in creating new category.
    fun getNonValidNamesList() : List<String> {
        var notValidNamesList: Array<String> = emptyArray()

        for (category in _categoriesState.value.list) {
            notValidNamesList += category.categoryName
        }

        return notValidNamesList.toList()
    }

   fun getCategories() {
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
        Log.d("deleteCategory", "CategoryId: $categoryId")

        val rentalItemsState = mutableStateOf(RentalItemsState())

        viewModelScope.launch {
            // Check contents of category by given id
            try {
                _deleteState.value = _deleteState.value.copy(loading = true)

                // Get rentalItems of category.
                val response = rentalItemsService.getRentalItems(categoryId)
                rentalItemsState.value = rentalItemsState.value.copy(list = response.rentalItems)

                // If no items found, continue to post.
                if (rentalItemsState.value.list.isEmpty()) {
                    Log.d("DeleteCategory()", "category is empty")
                    categoriesService.deleteCategoryById(categoryId)

                    // Filter categories into categoryList that has different categoryId than selected category.
                    val categories = _categoriesState.value.list.filter {
                        // false = filter out
                        it.categoryId != categoryId
                    }

                    // Update list
                    _categoriesState.value = _categoriesState.value.copy(list = categories)

                    Log.d("DeleteCategory()", "category deleted")
                    _deleteState.value = _deleteState.value.copy(done = true, success = true)
                }
                else {
                    Log.d("DeleteCategory()", "category is not empty")
                    _deleteState.value = _deleteState.value.copy(done = true, success = false)
                }
            }
            catch (e: Exception) {
                Log.d("error DeleteCategory()","$e")
                _categoriesState.value = _categoriesState.value.copy(error = e.toString())
            }
            finally {
                _deleteState.value = _deleteState.value.copy(loading = false)
            }
        }
    }

    fun postCategory(categoryName: String) {
        viewModelScope.launch {
            try {
                _createState.value = _createState.value.copy(loading = true)
                val response = categoriesService.postCategory(CategoryReq(categoryName))

                // Create new item from response
                val newCategoryItem = CategoryItem(
                    response.categoryId,
                    response.categoryName
                )

                // Add into categoriesState list
                val categories = (_categoriesState.value.list + newCategoryItem)
                    // Order list so it doesn't pop around randomly
                    .sortedBy { categoryItem ->
                        categoryItem.categoryName.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                            else it.toString()
                        }
                    }

                // Update list
                _categoriesState.value = _categoriesState.value.copy(list = categories)
            }
            catch (e: Exception) {
                Log.d("error PostCategory()", "$e")
                _createState.value = _createState.value.copy(error = e.toString())
            }
            finally {
                _createState.value = _createState.value.copy(loading = false)
            }
        }
    }

}
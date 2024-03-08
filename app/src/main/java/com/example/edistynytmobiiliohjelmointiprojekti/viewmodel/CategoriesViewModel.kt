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
import com.example.edistynytmobiiliohjelmointiprojekti.model.RentalItemsState
import kotlinx.coroutines.launch
import java.util.Locale

class CategoriesViewModel : ViewModel() {
    private val _categoriesState = mutableStateOf(CategoriesState())
    val categoriesState: State<CategoriesState> = _categoriesState

    val showDeleteDialog = mutableStateOf(false)
    val showCreateNewDialog = mutableStateOf(false)
    val showUnauthorizedDialog = mutableStateOf(false)
    val selectedCategoryItem = mutableStateOf(CategoryItem())


    init {
        getCategories()
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

    fun deleteCategory(categoryId: Int, deleteToast: (Boolean) -> Unit) {
        Log.d("deleteCategory", "CategoryId: $categoryId")

        val rentalItemsState = mutableStateOf(RentalItemsState())

        viewModelScope.launch {
            // Check contents of category by given id
            try {
                val response = rentalItemsService.getRentalItems(categoryId)
                rentalItemsState.value = rentalItemsState.value.copy(list = response.rentalItems)
            }
            catch (e: Exception) {
                Log.d("error getRentalItems in DeleteCategory()","$e")
            }
            finally {
                // Try delete category if empty
                if (rentalItemsState.value.list.isEmpty()) {
                    try {
                        categoriesService.deleteCategoryById(categoryId)

                        // Filteröidään kategorialistaan kaikki kategoriat, joiden id on eri kuin valittu id.
                        val categories =_categoriesState.value.list.filter {
                            // Jos palautuu false, se filteröityy pois
                            it.categoryId != categoryId
                        }
                        // Päivitetään lista
                        _categoriesState.value =_categoriesState.value.copy(list = categories)

                        Log.d("DeleteCategory()", "category deleted")
                        deleteToast(true)
                    }
                    catch (e: Exception) {
                        Log.d("error DeleteCategory()", "$e")
                    }
                }
                else {
                    Log.d("DeleteCategory()", "category has items inside")
                    deleteToast(false)
                }
            }
        }
    }

    fun postCategory(categoryName: String) {
        viewModelScope.launch {
            try {
                val response = categoriesService.postCategory(CategoryReq(categoryName))

                // Option 1: muokataan lista, jolloin vältytään ylimääräiseltä api kutsulta
                // Option 2: pyyhitään alla oleva ja kutsutaan simppelisti getCategories()

                // Luodaan repsonsesta uusi categoryitem
                val newCategoryItem = CategoryItem(
                    response.categoryId,
                    response.categoryName
                )

                // Lisätään luotu categoria categoriesstaten listaan.
                val categories = (_categoriesState.value.list + newCategoryItem)
                    // Järjestetään lista, jottei se ole aina eri järjestyksessä
                    .sortedBy { categoryItem ->
                        categoryItem.categoryName.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.ROOT)
                            else it.toString()
                        }
                    }

                // Päivitetään lista
                _categoriesState.value = _categoriesState.value.copy(list = categories)
            }
            catch (e: Exception) {
                Log.d("error PostCategory()", "$e")
            }
        }
    }

    // Get list of names used for categories. Used in creating new category.
    fun getNonValidNamesList(categories: List<CategoryItem>) : List<String> {
        var notValidNamesList: Array<String> = emptyArray()

        for (category in categories) {
            notValidNamesList += category.categoryName
        }

        return notValidNamesList.toList()
    }

}
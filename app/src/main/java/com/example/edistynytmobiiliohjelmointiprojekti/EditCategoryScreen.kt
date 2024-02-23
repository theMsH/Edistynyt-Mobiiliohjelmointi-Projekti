package com.example.edistynytmobiiliohjelmointiprojekti

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.edistynytmobiiliohjelmointiprojekti.viewmodel.EditCategoryViewModel

@Composable
fun EditCategoryScreen() {
    val editCategoryVm : EditCategoryViewModel = viewModel()
    
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Editing category by id: ${editCategoryVm.id}")
    }
}
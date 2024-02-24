package com.example.edistynytmobiiliohjelmointiprojekti.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.edistynytmobiiliohjelmointiprojekti.model.SelectedState

class NavigationdrawerViewModel : ViewModel() {
    private val _selected = mutableStateOf(SelectedState())
    val selected: State<SelectedState> = _selected

    init {
        setSelected("loginScreen")
    }

    fun setSelected(newSelected: String){
        _selected.value = _selected.value.copy(selected = newSelected)
    }
}
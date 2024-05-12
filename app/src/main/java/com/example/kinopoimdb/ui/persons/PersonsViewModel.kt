package com.example.kinopoimdb.ui.persons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Persons fragment.\nIt is not implemented yet"
    }
    val text: LiveData<String> = _text
}
package com.myjar.jarassignment.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState{
    object Loading : UiState()
    data class Success(val data: List<ComputerItem>) : UiState()
    data class Error(val message: String) : UiState()
}

//TODO ADD UI STATE FOR BETTER

class JarViewModel : ViewModel() {

    companion object{
        private const val TAG = "JarViewModel"
    }

    private val _listStringData = MutableStateFlow<List<ComputerItem>>(emptyList())
    val listStringData: StateFlow<List<ComputerItem>>
        get() = _listStringData

    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())

    fun fetchData() {
        viewModelScope.launch {
         try {
             repository.fetchResults().collect { item->
                 Log.d(TAG, "$item")
                 _listStringData.value = item
             }
         }catch (e: Exception){
             Log.e(TAG, "Error fetching data", e)
             e.printStackTrace()
             }
         }
        }
    }

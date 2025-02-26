package com.example.fisioapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fisioapp.data.repository.NoticiasRepository
import com.example.fisioapp.domain.models.NoticiasModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoticiasViewModel : ViewModel() {
    private val repository = NoticiasRepository()

    private val _listadoNoticias = MutableLiveData<List<NoticiasModel>>()
    val listadoNoticias: LiveData<List<NoticiasModel>> = _listadoNoticias

    // Para lanzar una función suspend hay que usar viewModelScope (Corrutina)
    fun traerNoticiasFisio(){
        viewModelScope.launch(Dispatchers.IO) {
            val datos = repository.getNoticiasFisio()
            _listadoNoticias.postValue(datos)
        }

    }

}
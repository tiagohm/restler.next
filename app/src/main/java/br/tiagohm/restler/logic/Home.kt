package br.tiagohm.restler.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    var method by mutableStateOf("GET")
    var protocol by mutableStateOf(ProtocolType.HTTP)
    var url by mutableStateOf("")
    var requestBodyEnabled by mutableStateOf(false)
    var requestBodyType by mutableStateOf(RequestBodyType.TEXT)
    var requestQueryEnabled by mutableStateOf(true)
    var requestHeaderEnabled by mutableStateOf(true)
    var requestAuthEnabled by mutableStateOf(false)
    var requestAuthType by mutableStateOf(RequestAuthType.BASIC)

    var customMethod = "GET"
        set(value) {
            field = value
            method = field
        }
}

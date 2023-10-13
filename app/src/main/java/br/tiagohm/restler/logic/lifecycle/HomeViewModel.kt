package br.tiagohm.restler.logic.lifecycle

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.tiagohm.restler.logic.enumeration.RequestAuthType
import br.tiagohm.restler.logic.enumeration.RequestBodyType
import br.tiagohm.restler.logic.enumeration.RequestProtocolType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext val appContext: Context,
) : ViewModel() {

    var method by mutableStateOf("GET")
    var protocol by mutableStateOf(RequestProtocolType.HTTP)
    var url by mutableStateOf("")
    var requestBodyEnabled by mutableStateOf(false)
    var requestBodyType by mutableStateOf(RequestBodyType.NONE)
    var requestQueryEnabled by mutableStateOf(true)
    var requestHeaderEnabled by mutableStateOf(true)
    var requestAuthEnabled by mutableStateOf(false)
    var requestAuthType by mutableStateOf(RequestAuthType.NONE)
    var requestBodyFile by mutableStateOf<Uri?>(null)

    var responseStatus by mutableIntStateOf(0)
    var responseTime by mutableIntStateOf(0)
    var responseLength by mutableIntStateOf(0)

    var customMethod = "GET"
        set(value) {
            field = value
            method = field
        }

    fun clear() {
        method = "GET"
        protocol = RequestProtocolType.HTTP
        url = ""
        requestBodyEnabled = false
        requestBodyType = RequestBodyType.NONE
        requestQueryEnabled = true
        requestHeaderEnabled = true
        requestAuthEnabled = false
        requestAuthType = RequestAuthType.NONE
        responseStatus = 0
        responseTime = 0
        responseLength = 0
    }
}

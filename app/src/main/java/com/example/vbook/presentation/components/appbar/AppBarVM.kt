package com.example.vbook.presentation.components.appbar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppBarVM @Inject constructor() : ViewModel() {
    private val _currentType: MutableStateFlow<Type> = MutableStateFlow(Type.Default)
    val currentType = _currentType.asStateFlow()

    //Search block
    private val _searchText: MutableStateFlow<String> =
        MutableStateFlow(value = "")
    val searchTextState = _searchText.asStateFlow()

    val isSearchBarOpened: MutableStateFlow<Boolean> =
        MutableStateFlow(value = false)

    fun updateSearchBarText(newValue: String) {
        _searchText.value = newValue
    }

    var onSearch: (String) -> Unit = {}
        private set
    var onClose: () -> Unit = {}
        private set

    fun setSearchBarCallBacks(
        onSearchCallback: (String) -> Unit,
        onCloseCallback: () -> Unit
    ) {
        onSearch = onSearchCallback
        onClose = onCloseCallback
    }

    //Search block end
    fun setType(type: Type) {
        _currentType.value = type
    }
    fun clearCallBacks() {
        isSearchBarOpened.value = false
        onSearch = {}
        onClose = {}
    }

    enum class Type() {
        Default, Search
    }
}

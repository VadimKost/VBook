/*
 * Created by vadim on 03.04.25, 17:45
 * Copyright (c) 2025 . All rights reserved.
 * Last modified 03.04.25, 17:45
 *
 */

package com.example.vbook.domain.shared

import com.example.vbook.domain.shared.ResourceState.Error
import com.example.vbook.domain.shared.ResourceState.Loading
import com.example.vbook.domain.shared.ResourceState.Success
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class ResourceState<out T> {
    data class Success<T>(var data: T) : ResourceState<T>()
    data object Loading : ResourceState<Nothing>()
//    data object Empty : ResourceState<Nothing>()
    class Error(val message: String) : ResourceState<Nothing>()

    inline fun onSuccess(action: (T) -> Unit): ResourceState<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (String) -> Unit): ResourceState<T> {
        if (this is Error) action(message)
        return this
    }

    inline fun onLoading(action: () -> Unit): ResourceState<T> {
        if (this is Loading) action()
        return this
    }

    fun getOrNull(): T? {
        return if (this is Success) data else null
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <reified T> ResourceState<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is Success<T>)
    }

    return this is Success<T>
}

@OptIn(ExperimentalContracts::class)
fun <T> ResourceState<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is Error)
    }

    return this is Error
}
@OptIn(ExperimentalContracts::class)
fun <T> ResourceState<T>.isLoading(): Boolean {
    contract {
        returns(true) implies (this@isLoading is Loading)
    }

    return this is Loading
}
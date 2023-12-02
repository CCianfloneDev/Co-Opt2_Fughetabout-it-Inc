package com.example.coopt2_fughetabout_it_inc.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData

/**
 * Converts the notes into an observable type.
 *
 * @param T The type of data in the LiveData.
 * @param initial The initial value of the LiveData.
 * @return The state as a Composable.
 */
@Composable
fun <T> LiveData<T>.observeAsState(initial: T): T {
    val liveData = this
    val state = remember { mutableStateOf(initial) }

    DisposableEffect(liveData) {
        val observer = androidx.lifecycle.Observer<T> { value ->
            state.value = value
        }
        liveData.observeForever(observer)

        onDispose {
            liveData.removeObserver(observer)
        }
    }

    return state.value
}
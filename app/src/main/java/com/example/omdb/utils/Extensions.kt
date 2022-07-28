package com.example.omdb.utils

import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.omdb.data.result.Result
import com.example.omdb.data.result.error.NetworkError
import com.example.omdb.widgit.selectable.SelectableView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Response


fun <T> Response<T>.isSuccessFul(): Boolean {
    return isSuccessful and (body() != null) and (code() in 200 until 300)
}

fun <T> Response<T>.asResult(): Result<T> {
    return if (isSuccessFul()) {
        Result.success(body()!!)
    } else {
        val error = NetworkError.get(this)
        Result.fail(error)
    }
}

fun <T : Any> safeApiCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    block: suspend () -> Result<T>,
): Flow<Result<T>> {
    return flow {
        emit(block())
    }.onStart {
        emit(Result.loading())
    }.catch { cause ->
        emit(Result.fail(cause))
    }.flowOn(dispatcher)
}

fun Fragment.launch(
    state: Lifecycle.State = Lifecycle.State.STARTED, block: suspend CoroutineScope.() -> Unit
): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(state) {
            block()
        }
    }
}

fun RadioButton.set(value: Boolean) {
    isSelected = value
    isChecked = value
}

fun List<SelectableView>.setupSelection(defaultIndex: Int? = null, cb: (Int) -> Unit): () -> Unit {
    var lastItem: SelectableView? = null

    fun select(selectableView: SelectableView) {
        lastItem?.select(false)
        selectableView.select(true)
        lastItem = selectableView
    }

    for (i in indices) {
        val s = this[i]
        s.setOnClickListener {
            select(s)
            cb(i)
        }
    }

    defaultIndex?.let {
        select(this[it])
        cb(it)
    }

    fun reset() {
        lastItem?.select(false)
        lastItem = null
        cb(-1)
    }

    return ::reset
}

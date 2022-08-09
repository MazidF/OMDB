package com.example.omdb.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.view.children
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.omdb.R
import com.example.omdb.data.local.datastore.setting.Theme
import com.example.omdb.data.result.Result
import com.example.omdb.data.result.error.NetworkError
import com.example.omdb.widgit.selectable.SelectableView
import com.google.android.material.appbar.AppBarLayout
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

fun AppCompatActivity.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch {
        block()
    }
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

fun AlertDialog.hideBackground() = apply {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}

fun getAttributeResourceId(context: Context, attribute: Int): Int {
    val value = TypedValue()
    context.theme.resolveAttribute(attribute, value, true)
    return value.resourceId
}

fun ImageView.loadImage(input: Any, cb: ((Boolean) -> Unit)? = null) {

    val request = Glide.with(this)
        .load(input)
        .centerCrop()
        .placeholder(R.drawable.loading_animation)
        .fitCenter()
        .error(R.drawable.ic_empty_movie)
//        .error(getAttributeResourceId(context, R.attr.errorIconDrawable))
        .diskCacheStrategy(DiskCacheStrategy.ALL) // caching for offline mode

    cb?.let {
        request.addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                cb(false)
                return false // false --> glide continue it's work
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                cb(true)
                return false
            }
        })
    }

    request.into(this)
}

fun Context.isLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun Fragment.onBack() {
    (requireActivity() as? AppCompatActivity)?.onBackPressed()
}

fun Fragment.changeColorOfStatusBar(color: Int) {
    requireActivity().window.statusBarColor = color
}

fun Fragment.setupFullScreen() {
    requireActivity().window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    )
}

fun Fragment.restoreFullScreen() {
    requireActivity().window.clearFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    )
}

fun AppBarLayout.setPadding(padding: Int) {
    this.children.forEach {
        it.updateLayoutParams {
            height += padding
        }
    }
}

fun Context.sharedPreferences(): SharedPreferences {
    return getSharedPreferences(packageName, MODE_PRIVATE)
}

fun SharedPreferences.saveTheme(theme: Theme) {
    edit {
        putInt(THEME_KEY, theme.mode)
    }
}

fun SharedPreferences.getThemeMode(): Int {
    return getInt(THEME_KEY, Theme.AUTO.mode)
}

fun setupTheme(mode: Int) {
    if (AppCompatDelegate.getDefaultNightMode() != mode) {
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}

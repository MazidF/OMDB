package com.example.omdb.widgit.selectable

import android.view.View

interface SelectableView : Selectable {
    fun getView(): View
    fun setOnClickListener(l: View.OnClickListener?)
}
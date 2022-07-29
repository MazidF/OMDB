package com.example.omdb.widgit.item

import android.content.Context
import android.graphics.Color.WHITE
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.omdb.R
import com.example.omdb.databinding.ItemViewBinding

// Compound View
class ItemView(
    context: Context,
    attrs: AttributeSet,
) : LinearLayout(context, attrs) {
    private val binding: ItemViewBinding

    init {
        val view = inflate(context, R.layout.item_view, this)
        binding = ItemViewBinding.bind(view)

        val attribute = context.obtainStyledAttributes(attrs, R.styleable.ItemView)

        val tint = attribute.getColor(R.styleable.ItemView_itemTint, WHITE)

        val text = attribute.getString(R.styleable.ItemView_itemText) ?: ""
        setupText(text, tint)

        val icon = attribute.getResourceId(R.styleable.ItemView_itemIcon, 0)
        setIcon(icon, tint)

        attribute.recycle() // attribute.use{} wont be work
    }

    private fun setupText(text: String, tint: Int) {
        setText(text)
        setTextColor(tint)
    }

    private fun setTextColor(tint: Int) {
        binding.title.apply {
            setTextColor(tint)
        }
    }

    fun setText(text: String) {
        binding.title.apply {
            this.text = text
        }
    }

    private fun setIcon(icon: Int, tint: Int) {
        binding.icon.apply {
            setImageResource(icon)
            setColorFilter(tint, android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }
}
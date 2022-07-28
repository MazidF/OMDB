package com.example.omdb.widgit.selectable

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.omdb.R
import com.example.omdb.databinding.SettingSelectableItemBinding
import com.example.omdb.utils.set
import com.example.omdb.utils.setupSelection

class SelectableViewGroup(
    context: Context,
    attrs: AttributeSet,
) : LinearLayout(context, attrs) {
    private var divider: Drawable? = null
    private var selectedIndex: Int = -1
    private var icons: Array<Int>? = null
    private lateinit var reset: () -> Unit
    private lateinit var titles: Array<String>
    private lateinit var items: List<SelectableView>

    init {
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet) {
        val attribute = context.obtainStyledAttributes(attrs, R.styleable.SelectableViewGroup)

        try {
            divider = attribute.getDrawable(R.styleable.SelectableViewGroup_divider)

            selectedIndex = attribute.getInt(R.styleable.SelectableViewGroup_selectedIndex, -1)

            val titlesId = attribute.getResourceId(R.styleable.SelectableViewGroup_itemsTitle, 0)
            titles = titlesId.takeUnless { it == 0 }?.let {
                context.resources.getStringArray(it)
            } ?: return

            val iconsId = attribute.getResourceId(R.styleable.SelectableViewGroup_itemsIcon, 0)
            val array = context.resources.obtainTypedArray(iconsId)
            icons = Array(array.length()) {
                array.getResourceId(it, 0)
            }
            array.recycle()

        } finally {
            attribute.recycle()

            setupView()
            setupChildren()
        }
    }

    private fun setupView() {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_HORIZONTAL
    }

    private fun setupChildren() {
        if (titles.isEmpty()) return


        val firstItem = createChild(titles[0], icons?.get(0)).also { view ->
            addView(view.getView())
        }
        items = listOf(firstItem) + List(titles.size - 1) {
            val index = it + 1
            addDivider()
            createChild(titles[index], icons?.get(index)).also { view ->
                addView(view.getView())
            }
        }
        reset = items.setupSelection(selectedIndex.takeIf { it != -1 }) {
            selectionChangeListener?.invoke(it)
        }
    }

    private fun addDivider() {
        divider?.let {
            addView(ImageView(context).apply {
                setImageDrawable(divider)
            })
        }
    }

    fun select(index: Int) {
        reset()
        for (i in items.indices) {
            items[i].select(i == index)
        }
        if (index != -1) {
            items[index].getView().callOnClick()
        }
    }

    fun reset() {
        reset.invoke()
    }

    private fun createChild(title: String, icon: Int?): SelectableView {
        val binding = SettingSelectableItemBinding.inflate(
            LayoutInflater.from(context), this, false
        ).apply {
            this.title.text = title
            if (icon != null) {
                this.icon.setImageResource(icon)
            } else {
                this.icon.isVisible = false
            }
        }

        return object : SelectableView {
            override fun getView(): View {
                return binding.root
            }

            override fun setOnClickListener(l: OnClickListener?) {
                binding.apply {
                    root.setOnClickListener(l)
                    check.setOnClickListener(l)
                }
            }

            override fun select(isSelected: Boolean) {
                binding.check.set(isSelected)
            }

            override fun changeSelectionState(isSelecting: Boolean) {}
        }
    }

    private var selectionChangeListener: ((Int) -> Unit)? = null
    fun setONSelectionChangeListener(block: ((Int) -> Unit)?) {
        selectionChangeListener = block
    }
}
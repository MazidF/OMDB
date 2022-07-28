package com.example.omdb.ui.fragment.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.omdb.data.model.entity.Movie
import com.example.omdb.databinding.SmallMovieItemBinding
import com.example.omdb.utils.bindable.Bindable
import com.example.omdb.utils.bindable.BindableFactory
import com.example.omdb.utils.loadImage

class SmallMovieItemFactory : BindableFactory<Movie>() {

    override fun inflate(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean
    ): Bindable<Movie> {
        val binding = SmallMovieItemBinding.inflate(layoutInflater, parent, attachToParent)

        return object : Bindable<Movie> {

            // it is possible to use data_binding
            // for better performance we won't
            // but data_binding makes it very easy (at least for me)
            override fun bind(item: Movie) = with(binding) {
                image.loadImage(item.poster)
                title.text = item.title
            }

            override fun getView(): View {
                return binding.root
            }

            override fun setOnClickListener(l: View.OnClickListener?) {
                binding.base.setOnClickListener(l)
            }
        }
    }

}
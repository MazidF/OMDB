package com.example.omdb.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.omdb.R
import com.example.omdb.databinding.FragmentWithLottieBinding

abstract class FragmentWithLottie(
    private val layoutResourceId: Int,
) : Fragment(R.layout.fragment_with_lottie) {
    private var _binding: FragmentWithLottieBinding? = null
    private val binding: FragmentWithLottieBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWithLottieBinding.bind(view)

        initView()
    }

    private fun initView() = with(binding) {
        val child = View.inflate(requireContext(), layoutResourceId, null)
        root.addView(child)
        onViewCreated(child)
    }

    protected abstract fun onViewCreated(view: View)

    protected fun stopAnimation() {
        binding.lottie.apply {
            pauseAnimation()
            cancelAnimation()
            isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
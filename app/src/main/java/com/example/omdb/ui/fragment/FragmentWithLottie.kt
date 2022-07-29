package com.example.omdb.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.omdb.R
import com.example.omdb.databinding.FragmentWithLottieBinding
import com.example.omdb.utils.getAttributeResourceId

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

    private fun startAnimation(resourceId: Int) {
        binding.lottie.apply {
            pauseAnimation()
            cancelAnimation()
            setAnimation(resourceId)
            play()
        }
    }

    protected fun startAnimation() = with(binding) {
        startAnimation(getAttributeResourceId(lottie.context, R.attr.loading_animation))
        hideRetry()
    }

    protected fun stopAnimation() = with(binding) {
        hideRetry()
        lottie.apply {
            pauseAnimation()
            animate().alpha(0f).withEndAction {
                isVisible = false
                alpha = 1f
            }.duration = 300
        }
    }

    private fun hideRetry() {
       binding.retry.isVisible = false
    }

    protected fun showError(onRetry: () -> Unit) = with(binding) {
        retry.apply {
            setOnClickListener {
                startAnimation()
                onRetry()
            }
            isVisible = true
        }
        startAnimation(getAttributeResourceId(root.context, R.attr.failed_animation))
    }

    private fun play() = binding.lottie.apply {
        playAnimation()
        isVisible = true
    }

    override fun onStop() {
        super.onStop()
        binding.lottie.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.omdb.ui.fragment

import android.view.View
import com.example.omdb.R
import com.example.omdb.databinding.FragmentRefreshableBinding

abstract class RefreshableFragment(
    private val layoutResourceId: Int,
) : FragmentWithLottie(R.layout.fragment_refreshable) {
    private var _binding: FragmentRefreshableBinding? = null
    private val binding: FragmentRefreshableBinding get() = _binding!!

    protected abstract fun refresh()

    protected abstract fun onChildViewCreated(view: View)

    override fun onViewCreated(view: View) {
        _binding = FragmentRefreshableBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        val child = View.inflate(requireContext(), layoutResourceId, null)
        root.addView(child)
        onChildViewCreated(child)
    }

    private fun observe() = with(binding) {
        root.setOnRefreshListener {
            refresh()
        }
    }

    protected fun disableRefresher() {
        binding.root.isEnabled = false
    }

    protected fun enableRefresher() {
        binding.root.isEnabled = true
    }

    protected fun stopRefreshing() {
        binding.root.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

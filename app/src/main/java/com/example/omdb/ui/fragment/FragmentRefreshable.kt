package com.example.omdb.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.omdb.R
import com.example.omdb.databinding.FragmentRefreshableBinding

abstract class RefreshableFragment(
    private val layoutResourceId: Int,
) : Fragment(R.layout.fragment_refreshable) {
    private var _binding: FragmentRefreshableBinding? = null
    private val binding: FragmentRefreshableBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRefreshableBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        val child = View.inflate(requireContext(), layoutResourceId, null)
        root.addView(child)
        onViewCreated(child)
    }

    protected abstract fun onViewCreated(view: View)

    private fun observe() = with(binding) {
        root.setOnRefreshListener {
            refresh()
        }
    }

    protected abstract fun refresh()

    protected fun stopRefreshing() {
        binding.root.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



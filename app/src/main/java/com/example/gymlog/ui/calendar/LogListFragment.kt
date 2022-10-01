package com.example.gymlog.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.gymlog.R
import com.example.gymlog.base.BaseFragment
import com.example.gymlog.databinding.FragmentLoglistBinding

class LogListFragment : BaseFragment() {

    private lateinit var binding:FragmentLoglistBinding

    override val _viewModel: LogListViewModel by viewModels {
        LogListViewModel.Factory(
            requireActivity().application
        )
    }
    private lateinit var adapter:LogListItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loglist, container, false)
        binding.lifecycleOwner= viewLifecycleOwner
        adapter = LogListItemAdapter {
            Navigation.findNavController(binding.root)
                .navigate(LogListFragmentDirections.actionLogListFragmentToLogPreviewFragment(it))
        }


        _viewModel.logs.observe(viewLifecycleOwner, Observer{logs -> adapter.setData(logs)})
        binding.rvLogList.adapter = adapter
        return binding.root
    }


}
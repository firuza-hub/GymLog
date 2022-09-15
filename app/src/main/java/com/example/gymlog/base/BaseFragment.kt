package com.example.gymlog.base

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment: Fragment() {

    abstract val _viewModel: BaseViewModel
    override fun onStart() {
        super.onStart()
        _viewModel.validationError.observe(this) {
            Snackbar.make(this.requireView(), it, Snackbar.LENGTH_SHORT) .show()
        }
    }

}
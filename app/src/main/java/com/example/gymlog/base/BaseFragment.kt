package com.example.gymlog.base

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.gymlog.utils.toMessage
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment: Fragment() {

    abstract val _viewModel: BaseViewModel
    override fun onStart() {
        super.onStart()
        Log.i("MIAO","START")
        _viewModel.validationError.observe(this) {
            if(it.size > 0) {
                val text = it.toMessage()
                Log.i("MIAO",text)
                Snackbar.make(this.requireView(), text, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}
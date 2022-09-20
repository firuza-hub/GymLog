package com.example.gymlog.ui.createLog

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gymlog.R
import com.example.gymlog.base.BaseFragment
import com.example.gymlog.databinding.FragmentCreateLogBinding
import com.example.gymlog.ui.auth.signIn.SignInViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class CreateLogFragment : BaseFragment() {

    override val _viewModel: CreateLogViewModel by viewModels {
        CreateLogViewModel.Factory(
            requireActivity().application
        )
    }

    private val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm aa")

    private lateinit var binding: FragmentCreateLogBinding
    private val myCalendar = Calendar.getInstance()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_log, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel

        binding.btnCloseInputBox.setOnTouchListener { v, _ ->
            v.performClick()
            if (binding.etWorkoutType.editText?.text?.isNotEmpty() == true) {
                binding.tvWorkoutTypeLabel.visibility = View.INVISIBLE
                binding.tvWorkoutTypeInput.text = binding.etWorkoutType.editText?.text.toString()
            } else {
                binding.tvWorkoutTypeLabel.visibility = View.VISIBLE
                binding.tvWorkoutTypeInput.text = ""
            }

            true
        }

        binding.btnCloseInputBoxNotes.setOnTouchListener { v, event ->
            v.performClick()
            if (binding.etNotes.editText?.text?.isNotEmpty() == true) {
                binding.tvNotesLabel.visibility = View.INVISIBLE
                binding.tvNotesInput.text = binding.etNotes.editText?.text.toString()
            } else {
                binding.tvNotesLabel.visibility = View.VISIBLE
                binding.tvNotesInput.text = ""
            }

            true
        }

        binding.btnCloseInputBoxWorkoutSubtype.setOnTouchListener { v, event ->
            v.performClick()
            if (binding.etWorkoutSubType.editText?.text?.isNotEmpty() == true) {
                binding.tvWorkoutSubTypeLabel.visibility = View.INVISIBLE
                binding.tvWorkoutSubTypeInput.text =
                    binding.etWorkoutSubType.editText?.text.toString()
            } else {
                binding.tvWorkoutSubTypeLabel.visibility = View.VISIBLE
                binding.tvWorkoutSubTypeInput.text = ""
            }

            true
        }

        binding.ivSelectLogDate.setOnClickListener {
           openDatePicker()
        }


        binding.btnSaveLog.setOnClickListener {
            _viewModel.workoutType = binding.etWorkoutType.editText?.text.toString()
            _viewModel.workoutSubtype = binding.etWorkoutSubType.editText?.text.toString()
            _viewModel.notes = binding.etNotes.editText?.text.toString()
            _viewModel._date.value = binding.tvLogDate.text.toString()
            _viewModel.saveLog()
        }
        return binding.root
    }

    private fun openTimePicker() {
        val timePicker = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
            myCalendar.set(Calendar.HOUR, hour)
            myCalendar.set(Calendar.MINUTE, minute)
            _viewModel._date.value = formatter.format(myCalendar.time)

        }
        TimePickerDialog(requireContext(),timePicker, myCalendar.get(Calendar.HOUR),  myCalendar.get(Calendar.MINUTE),false).show()
    }

    private fun openDatePicker() {

        Log.i("CREATE_LOG_FRAGMENT",_viewModel.date.value.toString())

        Log.i("CREATE_LOG_FRAGMENT", formatter.format(Date()))

        val datePicker = DatePickerDialog.OnDateSetListener{_, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            openTimePicker()
        }
         DatePickerDialog(requireContext(),datePicker,  myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
    }

}
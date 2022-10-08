package com.example.gymlog.ui.createLog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.gymlog.BuildConfig
import com.example.gymlog.R
import com.example.gymlog.base.BaseFragment
import com.example.gymlog.databinding.FragmentCreateLogBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CreateLogFragment : BaseFragment() {

    override val _viewModel: CreateLogViewModel by viewModels {
        CreateLogViewModel.Factory(
            requireActivity().application
        )
    }

    private val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm aa")
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraActivityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var binding: FragmentCreateLogBinding
    private val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                setPic()
            }
        }

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission() // Permission Type
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(
                    requireContext(),
                    "Permission granted now you can read the storage",
                    Toast.LENGTH_LONG
                ).show()
                dispatchTakePictureIntent()
            } else {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Please give access to camera usage to share your CHEF skills",
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.settings) {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }.show()
            }
        }
    }

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

        binding.btnCamera.setOnClickListener {
            requestCameraPermissionAndOpenCamera()
        }

        binding.btnSaveLog.setOnClickListener {
            _viewModel.workoutType = binding.etWorkoutType.editText?.text.toString()
            _viewModel.workoutSubtype = binding.etWorkoutSubType.editText?.text.toString()
            _viewModel.notes = binding.etNotes.editText?.text.toString()
            _viewModel._date.value = binding.tvLogDate.text.toString()
            _viewModel.saveLog { redirectToCalendar() }
        }

        binding.btnRemovePicture.setOnClickListener {
            _viewModel.bitMap = null
            _viewModel.currentPhotoName.value = ""
            _viewModel.currentPhotoPath.value = ""

            binding.btnCamera.setImageResource(R.drawable.ic_camera)
        }
        _viewModel.currentPhotoPath.observe(viewLifecycleOwner) {
            binding.btnRemovePicture.isClickable = !it.isNullOrEmpty()
        }

        _viewModel.showLoader.observe(
            viewLifecycleOwner
        ) { showLoader ->
            if (showLoader) binding.pbSaveLoader.visibility = View.VISIBLE
            else binding.pbSaveLoader.visibility = View.INVISIBLE
        }
        return binding.root
    }

    private fun redirectToCalendar() {
        Navigation.findNavController(binding.root)
            .navigate(CreateLogFragmentDirections.actionCreateLogFragmentToCalendarFragment())
    }

    private fun openTimePicker() {
        val timePicker = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            myCalendar.set(Calendar.HOUR, hour)
            myCalendar.set(Calendar.MINUTE, minute)
            _viewModel._date.value = formatter.format(myCalendar.time)

        }
        TimePickerDialog(
            requireContext(),
            timePicker,
            myCalendar.get(Calendar.HOUR),
            myCalendar.get(Calendar.MINUTE),
            false
        ).show()
    }

    private fun openDatePicker() {

        Log.i("CREATE_LOG_FRAGMENT", _viewModel.date.value.toString())

        Log.i("CREATE_LOG_FRAGMENT", formatter.format(Date()))

        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            openTimePicker()
        }
        DatePickerDialog(
            requireContext(),
            datePicker,
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun requestCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                dispatchTakePictureIntent()// You can use the API that requires the permission.
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {}
            else -> {
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }


    private fun dispatchTakePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Log.i("MYCAMERA", ex.message.toString())
                    null
                }
                // Continue only if the File was successfully created
                Log.i("MYCAMERA", "file created successfully")
                Log.i("MYCAMERA", ((photoFile?.name) ?: "nofile"))
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.gymlog.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    Log.i("MYCAMERA", "PUT EXTRA PHOTO URI: $photoURI")

                }
            }
        }
        cameraActivityResultLauncher.launch(intent)

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = formatter.format(Date())
        val storageDir: File =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            _viewModel.currentPhotoPath.value = absolutePath
            _viewModel.currentPhotoName.value = name
        }
    }


    private fun setPic() {
        // Get the dimensions of the View
        val targetW: Int = binding.btnCamera.width
        val targetH: Int = binding.btnCamera.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(_viewModel.currentPhotoPath.value, bmOptions)?.also { bitmap ->
            binding.btnCamera.setImageBitmap(bitmap)
            _viewModel.bitMap = bitmap
        }
    }
}
package com.siddharthchordia.travelassistant.ui.trips.dialog

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.siddharthchordia.travelassistant.databinding.FragmentCityInputDelogBinding
import java.util.Calendar
import java.util.Date

class CityInputDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentCityInputDelogBinding

    interface CityInputListener {
        fun onCityInput(cityName: String, startDate: Date, endDate: Date): Boolean
    }

    var cityInputListener: CityInputListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityInputDelogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.datePickerStartDate.minDate = (System.currentTimeMillis() - 1000)
        binding.datePickerEndDate.minDate = (System.currentTimeMillis() - 1000)


        binding.buttonOk.setOnClickListener {
            val cityName = binding.editTextCityName.text.toString()
            val startDay = binding.datePickerStartDate.dayOfMonth
            val startMonth = binding.datePickerStartDate.month
            val startYear = binding.datePickerStartDate.year

            val endDay = binding.datePickerEndDate.dayOfMonth
            val endMonth = binding.datePickerEndDate.month
            val endYear = binding.datePickerEndDate.year

            val calendar = Calendar.getInstance()

            calendar.set(startYear, startMonth, startDay)
            val startDate = calendar.time

            calendar.set(endYear, endMonth, endDay)
            val endDate = calendar.time

            if(cityInputListener?.onCityInput(cityName, startDate, endDate) == true) {
                dismiss()
            }

        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
    }
}
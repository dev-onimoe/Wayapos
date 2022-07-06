package com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.SignupFragmentTwoBinding
import com.wayapaychat.wayapay.framework.network.model.APICreateAccount
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFullScreenActivity
import com.wayapaychat.wayapay.presentation.screens.auth.login.LoginActivity
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.three.SignUpFragmentThree
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.helper.getPhoneNumber
import com.wayapaychat.wayapay.presentation.utils.helper.validateEmail
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragmentTwo :
    BaseActivity(), ISelectedDate {
    private var selectedGender = ""
    private var gender = arrayOf<String>()

    @Inject
    lateinit var cache: Cache
    lateinit var binding: SignupFragmentTwoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.signup_fragment_two)
        initView()
    }

    private fun initView() {
        gender = arrayOf(
            getString(R.string.select_one),
            getString(R.string.gender_male),
            getString(
                R.string.gender_female
            )
        )
        val arrayAdapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, gender)
        binding.genderSpinner.apply {
            adapter = arrayAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedGender = gender[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
        listeners()
    }

    private fun listeners() {
        with(binding) {
            nextbtn.setOnClickListener {
                validateFields { requestBody ->
                    cache.putObject("AccountDetails", requestBody)
                    startActivity(Intent(applicationContext, SignUpFragmentThree::class.java))
                }
            }
            loginNow.setOnClickListener {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
            dateOfBirthField.setOnClickListener {
                showDatePickerDialog()
            }
        }
    }

    private fun validateFields(callBack: (APICreateAccount) -> Unit) {

        with(binding) {

            if (binding.lastNameField.text.isEmpty() || binding.lastNameField.text.toString()
                    .contains("[0-9]".toRegex()) || binding.lastNameField.text.toString()
                    .contains("[^A-Za-z0-9 ]".toRegex())
            ) {
                showError(getString(R.string.last_name_is_required))
                return
            }
            if (binding.firstNameField.text.isEmpty() || binding.firstNameField.text.toString()
                    .contains("[0-9]".toRegex()) || binding.firstNameField.text.toString()
                    .contains("[^A-Za-z0-9 ]".toRegex())
            ) {
                showError("First name is required")
                return
            }

            if (binding.personalEmailField.text.isEmpty() || validateEmail(binding.personalEmailField.text.toString()).not()) {
                showError("Invalid Email address")
                return
            }

            if (binding.phoneNumberField.text.toString().isEmpty()) {
                showError("Business Phone number is Required")
                return
            }

            if (binding.phoneNumberField.text.toString()
                    .isEmpty() || binding.phoneNumberField.text.toString().length < 10
                || binding.phoneNumberField.text.toString().length > 10
            ) {
                showError("Invalid mobile number")
                return
            }
            if (binding.officeAddressField.text.toString().isEmpty()) {
                showError("office address is Required")
                return
            }
            if (binding.stateField.text.toString().isEmpty()
            ) {
                showError("state is Required")
                return
            }

            if (binding.cityField.text.toString().isEmpty() /*|| binding.cityField.text.toString()
                    .contains("[0-9]".toRegex()) || binding.cityField.text.toString()
                    .contains("[^A-Za-z0-9 ]".toRegex())*/
            ) {
                showError("city is Required")
                return
            }

            if (selectedGender == getString(R.string.select_one)) {
                showError("Please select a gender")
                return
            }
            if (binding.dateOfBirthField.text.toString() == "") {
                showError("date of birth is required")
                return
            }


            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val newdatee: Date = formatter.parse(binding.dateOfBirthField.text.toString()) as Date
            val formatFrom = SimpleDateFormat("yyyy-MM-dd")
            val myd = formatFrom.format(newdatee)
            Toast.makeText(applicationContext, myd, Toast.LENGTH_SHORT).show()



            val requestBody =
                cache.getObject("AccountDetails", APICreateAccount::class.java) as APICreateAccount
            requestBody.surname = lastNameField.text.toString()
            requestBody.firstName = firstNameField.text.toString()
            requestBody.email = personalEmailField.text.toString()
            requestBody.phoneNumber = getPhoneNumber(phoneNumberField.text.toString())
            requestBody.officeAddress = officeAddressField.text.toString()
            requestBody.state = stateField.text.toString()
            requestBody.city = cityField.text.toString()
            requestBody.gender = selectedGender.toUpperCase()
            requestBody.dateOfBirth = myd
            callBack(requestBody)
        }
    }

    private fun showDatePickerDialog() {
        val newFragment: DialogFragment =
            DatePickerFragment(this)
        newFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onSelectedDate(string: String) {

        val newdate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd/MM/yyyy") //or use getDateInstance()
        val formatedDate = formatter.format(newdate)


        val todayDate: Date = formatter.parse(formatedDate) as Date

        val selectedDate: Date = formatter.parse(string) as Date

        when {
            selectedDate.after(todayDate) -> {
                Toast.makeText(this, "DOB cannot be after today's date ", Toast.LENGTH_SHORT).show()
            }

            selectedDate.before(todayDate) -> {

               /* val newdatee: Date = formatter.parse(string) as Date
                val formatFrom = SimpleDateFormat("yyyy-MM-dd")

                val myd = formatFrom.format(newdatee)

                Toast.makeText(applicationContext, formatedDate, Toast.LENGTH_SHORT).show()*/

                binding.dateOfBirthField.text = string
            }
            selectedDate == todayDate -> {
                Toast.makeText(this, "DOB cannot be equal to today's date", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

interface ISelectedDate {
    fun onSelectedDate(string: String)
    fun onCancel(dialog: DialogInterface) {}
}

class DatePickerFragment(private val iSelectedDate: ISelectedDate) :
    DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    lateinit var date: String

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        iSelectedDate.onCancel(dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    /**
     * @param view the picker associated with the dialog
     * @param year the selected year
     * @param month the selected month (0-11 for compatibility with
     * [Calendar.MONTH])
     * @param dayOfMonth the selected day of the month (1-31, depending on
     * month)
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val finalMonth = if (month.toString().length >= 2) month else "0${month + 1}"
        val finalDay = if (dayOfMonth.toString().length >= 2) dayOfMonth else "0$dayOfMonth"
        date = "$finalDay/$finalMonth/$year"
        iSelectedDate.onSelectedDate(date)
    }
}

package com.wayapaychat.wayapay.presentation.utils.helper

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.wayapaychat.wayapay.R
import java.util.regex.Matcher
import java.util.regex.Pattern

fun validatePhoneNumber(phoneNumber: String, context: Context): Boolean {
    var phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.getInstance(context)
    if (phoneNumber.length >= phoneNumberUtil.getExampleNumber("NG").nationalNumber.toString().length) {
        try {
            val enteredPhoneNumber = phoneNumberUtil.parse(phoneNumber, "NG")
            Log.d("Phone validator", enteredPhoneNumber.countryCode.toString())
            Log.d("Phone validator", phoneNumberUtil.isValidNumber(enteredPhoneNumber).toString())

            return phoneNumberUtil.isValidNumber(enteredPhoneNumber)
        } catch (e: NumberParseException) {
            System.err.println("NumberParseException was thrown: $e")
        }
    } else {
        Log.d(TAG, "validatePhoneNumber: Hello")
    }

    return false
}

fun validateEmail(email: String): Boolean {
    val pt = Pattern.compile(
        "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)" +
                "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{3,3}$"
    )
    val matcher: Matcher = pt.matcher(email)
    return matcher.matches()
}

fun getPhoneNumber(phoneNumber: String): String {

    return if (phoneNumber.startsWith("234"))
        phoneNumber
    else {
        phoneNumber.let {
            val new = if (it.startsWith("0")) it.takeLast(10) else it
            "234$new"
        }
    }
}

class PasswordUtil() : TextWatcher {

    enum class StrengthLevel(val strength: String) {
        WEAK("WEAK"),
        MEDIUM("MEDIUM"),
        STRONG("STRONG"),
        BULLET_PROOF("BULLET PROOF")
    }

    var lowerCase = MutableLiveData(false)
    var upperCase = MutableLiveData(false)
    var digit = MutableLiveData(false)
    var specialCharacter = MutableLiveData(false)
    var strengthColor = MutableLiveData(R.color.deep_orange)
    var strengthLevel = MutableLiveData<StrengthLevel>()

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable?) {
        s?.let {
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s != null) {
            lowerCase.value = s.hasLowerCase()
            upperCase.value = s.hasUpperCase()
            digit.value = s.hasDigit()
            specialCharacter.value = s.hasSpecialCharacters()
            calculateStrength(s)
        }
    }

    private fun calculateStrength(s: CharSequence) {

        if (s.length in 1..3) {
            strengthLevel.value = StrengthLevel.WEAK
            strengthColor.value = R.color.deep_orange
        }
        if (s.length in 4..5) {
            if (lowerCase.value!! || upperCase.value!! || digit.value!! || specialCharacter.value!!) {
                strengthLevel.value = StrengthLevel.MEDIUM
                strengthColor.value = R.color.deep_orange
            }
        }
        if (s.length in 4..5) {
            if (lowerCase.value!! || upperCase.value!! || digit.value!! || specialCharacter.value!!) {
                if (lowerCase.value!! && upperCase.value!!) {
                    strengthLevel.value = StrengthLevel.STRONG
                    strengthColor.value = R.color.deep_orange
                }
            }
        }
        if (s.length >= 6) {
            if (lowerCase.value!! || upperCase.value!! || digit.value!! || specialCharacter.value!!) {
                if (lowerCase.value!! && upperCase.value!!) {
                    strengthLevel.value = StrengthLevel.STRONG
                    strengthColor.value = R.color.deep_orange
                } else {
                    strengthLevel.value = StrengthLevel.STRONG
                    strengthColor.value = R.color.deep_orange
                }
            }
        }
        if (s.length >= 6) {
            if (lowerCase.value!! && upperCase.value!! && digit.value!! && specialCharacter.value!!) {
                strengthLevel.value = StrengthLevel.BULLET_PROOF
                strengthColor.value = R.color.deep_orange
            }
        }
    }

    private fun CharSequence.hasLowerCase(): Boolean {
        return Pattern.compile("[a-z]").matcher(this).find()
    }

    private fun CharSequence.hasUpperCase(): Boolean {
        return Pattern.compile("[A-Z]").matcher(this).find()
    }

    private fun CharSequence.hasSpecialCharacters(): Boolean {
        return Pattern.compile("[!`~@#$%^&*()_+={}|\\[\\].?><:;,-]").matcher(this).find()
    }

    private fun CharSequence.hasDigit(): Boolean {
        return Pattern.compile("[0-9]").matcher(this).find()
    }

    private fun CharSequence.hasSequence(): Boolean {
        return this.contains("123")
    }
}

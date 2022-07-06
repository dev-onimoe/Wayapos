package com.wayapaychat.wayapay.presentation.screens.settings.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.wayapaychat.wayapay.R
import com.wayapaychat.wayapay.databinding.ProfileActivityBinding
import com.wayapaychat.wayapay.databinding.ProfileFragmentBinding
import com.wayapaychat.wayapay.framework.network.model.APILoginResponse
import com.wayapaychat.wayapay.framework.network.model.APIUpdateProfileRequest
import com.wayapaychat.wayapay.framework.network.model.AccountNumberData
import com.wayapaychat.wayapay.framework.network.model.LoginData
import com.wayapaychat.wayapay.framework.network.model.ProfileData
import com.wayapaychat.wayapay.framework.state_machine.StateMachine
import com.wayapaychat.wayapay.presentation.core.BaseActivity
import com.wayapaychat.wayapay.presentation.core.BaseFragment
import com.wayapaychat.wayapay.presentation.screens.MainActivity
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.DatePickerFragment
import com.wayapaychat.wayapay.presentation.screens.auth.sign_up.two.ISelectedDate
import com.wayapaychat.wayapay.presentation.utils.PathUtil
import com.wayapaychat.wayapay.presentation.utils.cache.Cache
import com.wayapaychat.wayapay.presentation.utils.cache.CacheConstants
import com.wayapaychat.wayapay.presentation.utils.ext.views.TAG
import com.wayapaychat.wayapay.presentation.utils.ext.views.showAlertDialogMessage
import com.wayapaychat.wayapay.presentation.utils.helper.validateEmail
import com.wayapaychat.wayapay.presentation.utils.helper.validatePhoneNumber
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.Arrays
import javax.inject.Inject

const val REQUEST_CODE_IMAGE = 100

@AndroidEntryPoint
class ProfileActivity :
    BaseActivity(),
    ISelectedDate {
    val viewmodel: ProfileViewModel by viewModels()
    lateinit var binding: ProfileActivityBinding
    private var imageFilePath: String? = ""
    private val REQUEST_CODE_IMAGE = 100
    private val REQUEST_CODE_PERMISSIONS = 101

    private val KEY_PERMISSIONS_REQUEST_COUNT = "KEY_PERMISSIONS_REQUEST_COUNT"
    private val MAX_NUMBER_REQUEST_PERMISSIONS = 2
    private val permissions = Arrays.asList(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var permissionRequestCount: Int = 0

    @Inject
    lateinit var cache: Cache
    private var gender = arrayOf<String>()
    private var selectedGender = ""
    private lateinit var loginResponse: APILoginResponse
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.profile_activity)
        savedInstanceState?.let {
            permissionRequestCount = it.getInt(KEY_PERMISSIONS_REQUEST_COUNT, 0)
        }
        loginResponse =
            cache.getObject(
                CacheConstants.Keys.USER_DETAILS,
                APILoginResponse::class.java
            ) as APILoginResponse
        initView()
        populateView(loginResponse.data)
        requestPermissionsIfNecessary()
    }

    private fun initView() {
        setUpSpinner()
        listeners()
        observers()
    }

    /**
     * Request permissions twice - if the user denies twice then show a toast about how to update
     * the permission for storage. Also disable the button if we don't have access to pictures on
     * the device.
     */
    private fun requestPermissionsIfNecessary() {
        if (!checkAllPermissions()) {
            if (permissionRequestCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                permissionRequestCount += 1
                ActivityCompat.requestPermissions(
                    this,
                    permissions.toTypedArray(),
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.set_permissions_in_settings,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /** Permission Checking  */
    private fun checkAllPermissions(): Boolean {
        var hasPermissions = true
        for (permission in permissions) {
            hasPermissions = hasPermissions and isPermissionGranted(permission)
        }
        return hasPermissions
    }

    private fun isPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(applicationContext, permission) ==
                PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            requestPermissionsIfNecessary() // no-op if permissions are granted already.
        }
    }

    private fun setUpSpinner() {
        gender = arrayOf(
            getString(R.string.select_one),
            getString(R.string.gender_male),
            getString(
                R.string.gender_female
            )
        )
        val arrayAdapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, gender)
        binding.genderField.apply {
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
    }

    private fun observers() {
        with(viewmodel) {
            uploadProfileImageObserver.observe(this@ProfileActivity) {
                when (it) {
                    is StateMachine.Loading -> {
                        loading(true, binding.profileProgressBar)
                    }

                    is StateMachine.Error -> {
                        loading(false, binding.profileProgressBar)
                        showAlertDialogMessage(
                            message = it.error.localizedMessage!!,
                            positiveBottomText = "Retry"
                        )
                    }

                    is StateMachine.Success -> {
                        loading(false, binding.profileProgressBar)
                        loading(true, binding.progressBar)
                        updateProfileInfo()
                    }

                    is StateMachine.TimeOut -> {
                        loading(false, binding.profileProgressBar)
                        showAlertDialogMessage(
                            message = getString(R.string.timeout_request),
                            positiveBottomText = "Retry"
                        )
                    }
                    is StateMachine.GenericError -> {
                        loading(false, binding.profileProgressBar)
                        showAlertDialogMessage(
                            message = it.error?.message!!,
                            positiveBottomAction = {
                            },
                            positiveBottomText = "Ok"
                        )
                    }
                }
            }
            updateProfileObserver.observe(this@ProfileActivity) {
                when (it) {
                    is StateMachine.Loading -> {
                        loading(true, binding.progressBar)
                    }

                    is StateMachine.Error -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = it.error.localizedMessage!!,
                            positiveBottomText = "Retry"
                        )
                    }
                    is StateMachine.Success -> {
                        loading(false, binding.progressBar)
                        // fetchAndSaveProfile()
                        gotoDashBoard()
                    }

                    is StateMachine.TimeOut -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = getString(R.string.timeout_request),
                            positiveBottomText = "Retry"
                        )
                    }
                    is StateMachine.GenericError -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = it.error?.message!!,
                            positiveBottomAction = {
                            },
                            positiveBottomText = "Ok"
                        )
                    }
                }
            }
            profileObserver.observe(this@ProfileActivity) {
                when (it) {
                    is StateMachine.Loading -> {
                        loading(true, binding.progressBar)
                    }
                    is StateMachine.Error -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = it.error.localizedMessage!!,
                            positiveBottomText = "Retry"
                        )
                    }
                    is StateMachine.Success -> {
                        loading(false, binding.progressBar)
                        val profileData = it.data.data
                        val accountDetails = cache.getObject(
                            CacheConstants.Keys.ACCOUNT_NUMBER,
                            AccountNumberData::class.java
                        ) as AccountNumberData
                        profileData?.accountName = "${accountDetails.accountName}"
                        profileData?.accountNumber = "${accountDetails.accountNumber}"
                        profileData?.banKName = "${accountDetails.banKName}"
                        profileData?.bankCode = "${accountDetails.bankCode}"
                        profileData?.userId = loginResponse.data?.user?.id
                        profileData?.referalCode = loginResponse.data?.user?.referenceCode
                        profileData?.isEmailVerified = loginResponse.data?.user?.isEmailVerified
                        profileData?.isPhoneVerified = loginResponse.data?.user?.isPhoneVerified
                        viewmodel.setStateEvent(
                            ProfileStateEvent.UpdateLocalProfile,
                            loginResponse.data?.user?.id!!,
                            profileData
                        )
                    }

                    is StateMachine.TimeOut -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = getString(R.string.timeout_request),
                            positiveBottomText = "Retry"
                        )
                    }
                    is StateMachine.GenericError -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = it.error?.message!!,
                            positiveBottomAction = {
                            },
                            positiveBottomText = "Ok"
                        )
                    }
                }
            }
            updateLocalProfileProfileObserver.observe(this@ProfileActivity) {
                when (it) {
                    is StateMachine.Loading -> {
                        loading(true, binding.progressBar)
                    }
                    is StateMachine.Error -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = it.error.localizedMessage!!,
                            positiveBottomText = "Retry"
                        )
                    }
                    is StateMachine.Success -> {
                        loading(false, binding.progressBar)
                        //cache.remove(CacheConstants.Keys.USER_DETAILS)
                        //gotoDashBoard()
                    }
                }
            }
        }
    }

    private fun fetchAndSaveProfile() {
        viewmodel.setStateEvent(ProfileStateEvent.FetchProfile, loginResponse.data?.user?.id!!)
    }

    private fun populateView(data: LoginData?) {
         val accountDetails = cache.getObject(
             CacheConstants.Keys.ACCOUNT_NUMBER,
             AccountNumberData::class.java
         ) as? AccountNumberData
        data?.let {
            with(binding) {
                data.user?.let {
                    accountNumberTxt.text = "Acct Number: ${accountDetails?.accountNumber.toString()}"
                    firstNameField.setText(it.firstName)
                    lastNameField.setText(it.lastName)
                    middleNameField.setText(it.middleName)
                    dateOfBirthField.setText(it.dateOfBirth.toString())
                    if (it.gender?.toLowerCase() == "male")
                        genderField.setSelection(1)
                    else
                        genderField.setSelection(2)
                    emailField.setText(it.email)
                    mobileNumberField.setText(it.phoneNumber)
                    districtState.setText(it.state)
                    address.setText(it.address)
                    Glide.with(this@ProfileActivity).load(it.profileImage).into(profileImage)
                }
            }
        }
    }

    private fun gotoDashBoard() {
        val intent = Intent(applicationContext, MainActivity::class.java)

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_IMAGE) {
                val imageUri: Uri? = data?.clipData?.let {
                    it.getItemAt(0).uri
                } ?: data?.data

                if (imageUri == null) {
                    Log.e(TAG, "Invalid input image Uri.")
                    return
                }
                imageFilePath = PathUtil.getRealPath(applicationContext, imageUri)
                Log.d(TAG, "onActivityResult: $imageFilePath")
                Glide.with(applicationContext).load(imageUri).placeholder(R.drawable.place_holder)
                    .into(binding.profileImage)
            }
        }
    }

    private fun listeners() {
        with(binding) {
            skipButton.setOnClickListener {
                gotoDashBoard()
            }
            saveBtn.setOnClickListener {
                validateFields {
                    if (imageFilePath.isNullOrEmpty()){
                        updateProfileInfo()
                    }else{
                        uploadProfileImage()
                    }
                }
            }
            dateOfBirthField.setOnClickListener {
                DatePickerFragment(this@ProfileActivity).show(
                    supportFragmentManager,
                    "DatePickerFragment"
                )
            }
            profileImage.setOnClickListener {

                val chooseIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(chooseIntent, REQUEST_CODE_IMAGE)
            }
        }
    }

    private fun uploadProfileImage() {
        val file = File(imageFilePath)
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestBody)



        //val (_, data) = loginResponse
        Log.d(TAG, "uploadProfileImage: $body")
        viewmodel.setStateEvent(
            ProfileStateEvent.UpdateProfileImage,
            loginResponse.data?.user?.id!!,
            body
        )
    }

    private fun updateProfileInfo() {
        val body = APIUpdateProfileRequest(
            firstName = binding.firstNameField.text.toString(),
            middleName = binding.middleNameField.text.toString(),
            surname = binding.lastNameField.text.toString(),
            dateOfBirth = binding.dateOfBirthField.text.toString(),
            gender = selectedGender,
            email = binding.emailField.text.toString(),
            phoneNumber = binding.mobileNumberField.text.toString(),
            state = binding.districtState.text.toString(),
            district = binding.districtState.text.toString(),
            city = binding.districtState.text.toString(),
            address = binding.address.text.toString(),
            organisationName = binding.firstNameField.text.toString(),
            businessType = "computer",
            organisationType = "solo"
        )
        //val (_, data) = loginResponse
        viewmodel.setStateEvent(
            ProfileStateEvent.UpdateProfile,
            loginResponse.data?.user?.id!!,
            body
        )
        //gotoDashBoard()
    }

    private fun validateFields(callBack: () -> Unit) {
        with(binding) {
            if (firstNameField.text.toString().isEmpty()) {
                showError("first name is required")
                return
            }

            if (lastNameField.text.toString().isEmpty()) {
                showError("last name is required")
                return
            }

            if (dateOfBirthField.text.isEmpty()) {
                showError("Date of birth is required")
                return
            }

            if (emailField.text.toString().isEmpty()) {
                showError("email is required")
                return
            }
            if (validateEmail(emailField.text.toString()).not()) {
                showError("invalid email")
                return
            }

            if (mobileNumberField.text.toString().isEmpty()) {
                showError("Phone number is required")
                return
            }

            if (districtState.text.toString().isEmpty()) {
                showError("set a state")
                return
            }

            if (address.text.toString().isEmpty()) {
                showError("set an address")
                return
            }
            if (validatePhoneNumber(mobileNumberField.text.toString(), applicationContext).not()) {
                showError("invalid phone number")
                return
            }
        }
        callBack()
    }

    override fun onSelectedDate(string: String) {
        binding.dateOfBirthField.setText(string)
    }
}

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<ProfileFragmentBinding>(R.layout.profile_fragment),
    ISelectedDate {
    val viewmodel: ProfileViewModel by viewModels()

    @Inject
    lateinit var cache: Cache
    private var gender = arrayOf<String>()
    private var selectedGender = ""
    private var userId: Int = 0

    override fun init() {
        super.init()
        initView()
        userId = cache.getInt(CacheConstants.Keys.USER_ID)
        viewmodel.setStateEvent(
            ProfileStateEvent.FetchProfileFromDB,
            userId
        )

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
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, gender)
        binding.genderField.apply {
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
        observers()
    }

    private fun observers() {
        viewmodel.profileObserver.observe(
            this
        ) {

            when (it) {
                is StateMachine.Loading -> {
                    loading(true, binding.progressBar)
                }

                is StateMachine.Error -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = it.error.localizedMessage!!,
                        positiveBottomText = "Retry"
                    )
                }

                is StateMachine.Success -> {
                    loading(false, binding.progressBar)
                    populateView(it.data.data)
                }

                is StateMachine.TimeOut -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = getString(R.string.timeout_request),
                        positiveBottomText = "Retry"
                    )
                }
                is StateMachine.GenericError -> {
                    loading(false, binding.progressBar)
                    showAlertDialogMessage(
                        message = it.error?.message!!,
                        positiveBottomAction = {
                        },
                        positiveBottomText = "Ok"
                    )
                }
            }
        }
    }

    private fun populateView(data: ProfileData?) {
        data?.let {
            with(binding) {
                firstNameField.setText(data.firstName)
                lastNameField.setText(data.surname)
                dateOfBirthField.text = data.dateOfBirth
                if (data.gender?.toLowerCase() == "male")
                    genderField.setSelection(0)
                else
                    genderField.setSelection(1)
                emailField.setText(data.email)
                mobileNumberField.setText(data.phoneNumber)
                //districtState.setText(data.otherDetails?.organizationState)
                //address.setText(data.otherDetails?.organizationAddress)
            }
        }
    }

    private fun listeners() {
        with(binding) {
            cancelButton.setOnClickListener {
                Navigation.findNavController(requireView()).popBackStack()
            }
            saveBtn.setOnClickListener {

                validateFields {

                    val body = APIUpdateProfileRequest(
                        firstName = binding.firstNameField.text.toString(),
                        middleName = binding.middleNameField.text.toString(),
                        surname = binding.lastNameField.text.toString(),
                        dateOfBirth = binding.dateOfBirthField.text.toString(),
                        gender = selectedGender,
                        email = binding.emailField.text.toString(),
                        phoneNumber = binding.mobileNumberField.text.toString(),
                        state = binding.districtState.text.toString(),
                        district = binding.districtState.text.toString(),
                        city = binding.districtState.text.toString(),
                        address = binding.address.text.toString(),
                        organisationName = binding.firstNameField.text.toString(),
                        businessType = "computer",
                        organisationType = "solo"
                    )

                    viewmodel.setStateEvent(
                        ProfileStateEvent.UpdateProfile,
                        userId,
                        body
                    )
                }
            }
            dateOfBirthField.setOnClickListener {
            }
        }
    }

    private fun validateFields(callBack: () -> Unit) {
        with(binding) {
            if (firstNameField.text.toString().isEmpty()) {
                showError("first name is required")
                return
            }

            if (lastNameField.text.toString().isEmpty()) {
                showError("last name is required")
                return
            }

            if (dateOfBirthField.text.isEmpty()) {
                showError("date of birth is required")
                return
            }

            if (emailField.text.toString().isEmpty()) {
                showError("email is required")
                return
            }
            if (validateEmail(emailField.text.toString()).not()) {
                showError("invalid email")
                return
            }

            if (mobileNumberField.text.toString().isEmpty()) {
                showError("Phone number is required")
                return
            }
            if (validatePhoneNumber(mobileNumberField.text.toString(), requireContext()).not()) {
                showError("invalid phone number")
                return
            }
        }
        callBack()
    }

    override fun onSelectedDate(string: String) {
        binding.dateOfBirthField.text = string
    }
}

@AndroidEntryPoint
class ProfileActivity2 :
    BaseActivity(),
    ISelectedDate {

    lateinit var binding: ProfileFragmentBinding
    val viewmodel: ProfileViewModel by viewModels()

    @Inject
    lateinit var cache: Cache
    private var gender = arrayOf<String>()
    private var selectedGender = ""
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.profile_fragment)
        initView()
        val loginResponse: APILoginResponse? =
            cache.getObject(
                CacheConstants.Keys.USER_DETAILS,
                APILoginResponse::class.java
            ) as? APILoginResponse
        //userId = 181
        Log.d("MMM", " ${loginResponse?.data?.toString()}")
        viewmodel.setStateEvent(
            ProfileStateEvent.FetchProfileFromDB,
            userId
        )
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
        binding.genderField.apply {
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
        observers()
    }

    private fun observers() {
        viewmodel.profileObserver.observe(
            this,
            {

                when (it) {
                    is StateMachine.Loading -> {
                        loading(true, binding.progressBar)
                    }

                    is StateMachine.Error -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = it.error.localizedMessage!!,
                            positiveBottomText = "Retry"
                        )
                    }

                    is StateMachine.Success -> {
                        loading(false, binding.progressBar)
                        populateView(it.data.data)
                    }

                    is StateMachine.TimeOut -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = getString(R.string.timeout_request),
                            positiveBottomText = "Retry"
                        )
                    }
                    is StateMachine.GenericError -> {
                        loading(false, binding.progressBar)
                        showAlertDialogMessage(
                            message = it.error?.message!!,
                            positiveBottomAction = {
                            },
                            positiveBottomText = "Ok"
                        )
                    }
                }
            }
        )
    }

    private fun populateView(data: ProfileData?) {
        val accountDetails = cache.getObject(
            CacheConstants.Keys.ACCOUNT_NUMBER,
            AccountNumberData::class.java
        ) as AccountNumberData

        data?.let {
            with(binding) {
                firstNameField.setText(data.firstName)
                lastNameField.setText(data.surname)
                dateOfBirthField.text = data.dateOfBirth
                if (data.gender?.toLowerCase() == "male")
                    genderField.setSelection(0)
                else
                    genderField.setSelection(1)
                emailField.setText(data.email)
                mobileNumberField.setText(data.phoneNumber)
               // districtState.setText(data.otherDetails?.organizationState)
               // address.setText(data.otherDetails?.organizationAddress)
                accountNumberTxt.setText("Acct Number:  ${accountDetails.accountNumber}")
            }
        }
    }

    private fun listeners() {
        with(binding) {
            cancelButton.setOnClickListener {
                finish()
            }
            saveBtn.setOnClickListener {

                validateFields {

                    val body = APIUpdateProfileRequest(
                        firstName = binding.firstNameField.text.toString(),
                        middleName = binding.middleNameField.text.toString(),
                        surname = binding.lastNameField.text.toString(),
                        dateOfBirth = binding.dateOfBirthField.text.toString(),
                        gender = selectedGender,
                        email = binding.emailField.text.toString(),
                        phoneNumber = binding.mobileNumberField.text.toString(),
                        state = binding.districtState.text.toString(),
                        district = binding.districtState.text.toString(),
                        city = binding.districtState.text.toString(),
                        address = binding.address.text.toString(),
                        organisationName = binding.firstNameField.text.toString(),
                        businessType = "computer",
                        organisationType = "solo"
                    )

                    viewmodel.setStateEvent(
                        ProfileStateEvent.UpdateProfile,
                        userId,
                        body
                    )
                }

            }

            dateOfBirthField.setOnClickListener {
            }
        }
    }

    private fun validateFields(callBack: () -> Unit) {
        with(binding) {
            if (firstNameField.text.toString().isEmpty()) {
                showError("first name is required")
                return
            }

            if (lastNameField.text.toString().isEmpty()) {
                showError("last name is required")
                return
            }

            if (dateOfBirthField.text.isEmpty()) {
                showError("last name is required")
                return
            }

            if (emailField.text.toString().isEmpty()) {
                showError("email is required")
                return
            }
            if (validateEmail(emailField.text.toString()).not()) {
                showError("invalid email")
                return
            }

            if (mobileNumberField.text.toString().isEmpty()) {
                showError("Phone number is required")
                return
            }
            if (validatePhoneNumber(mobileNumberField.text.toString(), applicationContext).not()) {
                showError("invalid phone number")
                return
            }
        }
        callBack()
    }

    override fun onSelectedDate(string: String) {
        binding.dateOfBirthField.text = string
    }
}

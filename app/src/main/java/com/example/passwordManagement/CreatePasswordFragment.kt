package com.example.passwordManagement

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.passwordManagement.database.PasswordDatabase
import com.example.passwordManagement.entities.Password
import com.example.passwordManagement.util.PasswordBottomFragment
import kotlinx.android.synthetic.main.fragment_create_password.*
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class CreatePasswordFragment : BaseFragment(),EasyPermissions.PermissionCallbacks,EasyPermissions.RationaleCallbacks{

    var selectedColor = "#171C26"
    var currentDate:String? = null
    private var REQUEST_CODE_IMAGE = 456
    private var selectedImagePath = ""
    private var webLink = ""
    private var passwordID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        passwordID = requireArguments().getInt("passwordID",-1)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_password, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreatePasswordFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)

        if (passwordID != -1){

            launch {
                context?.let {
                    var password = PasswordDatabase.getDatabase(it).PasswordDao().getSpecificPassword(passwordID)
                    colorView.setBackgroundColor(Color.parseColor(password.color))
                    txtService.setText(password.service)
                    usernameEmail.setText(decrypt(password.usernameEmail))
                    txtPassword.setText(decrypt(password.passwordText))

                    if (password.webLink != ""){
                        webLink = password.webLink!!
                        tvWebLink.text = password.webLink
                        layoutWebUrl.visibility = View.VISIBLE
                        boxWebLink.setText(password.webLink)
                        imgUrlDelete.visibility = View.VISIBLE
                    }else{
                        imgUrlDelete.visibility = View.GONE
                        layoutWebUrl.visibility = View.GONE
                    }
                }
            }
        }
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")

        currentDate = sdf.format(Date())
        colorView.setBackgroundColor(Color.parseColor(selectedColor))

        DateTime.text = currentDate

        imgDone.setOnClickListener {
            if (passwordID != -1){
                updatePassword()
            }else{
                savePassword()
            }
        }

        imgBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        imgSetting.setOnClickListener{
            var passwordBottomFragment = PasswordBottomFragment.newInstance(passwordID)
            passwordBottomFragment.show(requireActivity().supportFragmentManager,"Password Bottom Fragment")
        }

        btnOk.setOnClickListener {
            if (boxWebLink.text.toString().trim().isNotEmpty()){
                checkWebUrl()
            }else{
                Toast.makeText(requireContext(),"Url is Required",Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            if (passwordID != -1){
                tvWebLink.visibility = View.VISIBLE
                layoutWebUrl.visibility = View.GONE
            }else{
                layoutWebUrl.visibility = View.GONE
            }

        }

        imgUrlDelete.setOnClickListener {
            webLink = ""
            tvWebLink.visibility = View.GONE
            imgUrlDelete.visibility = View.GONE
            layoutWebUrl.visibility = View.GONE
        }

        tvWebLink.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW,Uri.parse(boxWebLink.text.toString()))
            startActivity(intent)
        }

    }

    private fun updatePassword(){

        if (txtService.text.isNullOrEmpty()){
            Toast.makeText(context,"Service is Required",Toast.LENGTH_SHORT).show()
        }
        else if (usernameEmail.text.isNullOrEmpty()){

            Toast.makeText(context,"Username or Email is Required",Toast.LENGTH_SHORT).show()
        }

        else if (txtPassword.text.isNullOrEmpty()){

            Toast.makeText(context,"Password is Required",Toast.LENGTH_SHORT).show()
        }
        else {

            launch {
                context?.let {
                    var password = PasswordDatabase.getDatabase(it).PasswordDao().getSpecificPassword(passwordID)

                    password.service = txtService.text.toString()
                    password.usernameEmail = encrypt(usernameEmail.text.toString())
                    password.passwordText = encrypt(txtPassword.text.toString())
                    password.dateTime = currentDate
                    password.color = selectedColor
                    password.webLink = webLink

                    PasswordDatabase.getDatabase(it).PasswordDao().updatePassword(password)
                    txtService.setText("")
                    usernameEmail.setText("")
                    txtPassword.setText("")
                    colorView.setBackgroundColor(Color.parseColor(password.color))
                    layoutImage.visibility = View.GONE
                    tvWebLink.visibility = View.GONE
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun savePassword(){
        if (txtService.text.isNullOrEmpty()){
            Toast.makeText(context,"Service is Required",Toast.LENGTH_SHORT).show()
        }
        else if (usernameEmail.text.isNullOrEmpty()){

            Toast.makeText(context,"Username or Email is Required",Toast.LENGTH_SHORT).show()
        }

        else if (txtPassword.text.isNullOrEmpty()){

            Toast.makeText(context,"Password is Required",Toast.LENGTH_SHORT).show()
        }
        else{

        launch {
            var password = Password()
            password.service = txtService.text.toString()
            password.usernameEmail = encrypt(usernameEmail.text.toString())
            password.passwordText = encrypt(txtPassword.text.toString())
            password.dateTime = currentDate
            password.color = selectedColor
            password.webLink = webLink
            context?.let {
                PasswordDatabase.getDatabase(it).PasswordDao().insertPassword(password)
                txtService.setText("")
                usernameEmail.setText("")
                txtPassword.setText("")
                tvWebLink.visibility = View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        }

    }
    var AES = "AES"
    @Throws(java.lang.Exception::class)
    private fun encrypt(Data: String): String {
        val key = generateKey("tK5UTui+DPh8lIlBxya5XVsmeDCoUl6vHhdIESMB6sQ=")
        val c = Cipher.getInstance(AES)
        c.init(Cipher.ENCRYPT_MODE, key)
        val encVal = c.doFinal(Data.toByteArray())
        return Base64.encodeToString(encVal, Base64.DEFAULT)
    }

    @Throws(java.lang.Exception::class)
    private fun decrypt(outputString: String?): String {
        val key = generateKey("tK5UTui+DPh8lIlBxya5XVsmeDCoUl6vHhdIESMB6sQ=")
        val c = Cipher.getInstance(AES)
        c.init(Cipher.DECRYPT_MODE, key)
        val decodedVal = Base64.decode(outputString, Base64.DEFAULT)
        val decVal = c.doFinal(decodedVal)
        return String(decVal)
    }

    @Throws(java.lang.Exception::class)
    private fun generateKey(password: String): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = password.toByteArray(charset("UTF-8"))
        digest.update(bytes, 0, bytes.size)
        val key = digest.digest()
        return SecretKeySpec(key, "AES")
    }

    private fun deletePassword(){
        launch {
            context?.let {
                PasswordDatabase.getDatabase(it).PasswordDao().deleteSpecificPassword(passwordID)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
        Toast.makeText(requireContext(), "Password has been deleted", Toast.LENGTH_SHORT).show()
    }

    private fun checkWebUrl(){
        if (Patterns.WEB_URL.matcher(boxWebLink.text.toString()).matches()){
            layoutWebUrl.visibility = View.GONE
            boxWebLink.isEnabled = false
            webLink = boxWebLink.text.toString()
            boxWebLink.visibility = View.VISIBLE
            tvWebLink.text = boxWebLink.text.toString()
        }else{
            Toast.makeText(requireContext(),"Url is not valid",Toast.LENGTH_SHORT).show()
        }
    }


    private val BroadcastReceiver : BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {

            var actionColor = p1!!.getStringExtra("action")

            when(actionColor!!){

                "Blue" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }

                "Yellow" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Purple" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Green" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Orange" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }


                "Black" -> {
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))

                }

                "WebUrl" ->{
                    layoutWebUrl.visibility = View.VISIBLE
                }
                "DeletePassword" -> {
                    deletePassword()
                }


                else -> {
                    layoutWebUrl.visibility = View.GONE
                    selectedColor = p1.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
            }
        }

    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()

    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath:String? = null
        var cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if (cursor == null){
            filePath = contentUri.path
        }else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK){
            if (data != null){
                var selectedImageUrl = data.data
                if (selectedImageUrl != null){
                    try {
                        var inputStream = requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        layoutImage.visibility = View.VISIBLE

                        selectedImagePath = getPathFromUri(selectedImageUrl)!!
                    }catch (e:Exception){
                        Toast.makeText(requireContext(),e.message,Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
       if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(),perms)){
           AppSettingsDialog.Builder(requireActivity()).build().show()
       }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRationaleDenied(requestCode: Int) {
    }

    override fun onRationaleAccepted(requestCode: Int) {
    }
}
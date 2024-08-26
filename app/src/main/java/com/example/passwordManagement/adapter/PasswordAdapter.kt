package com.example.passwordManagement.adapter

import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordManagement.R
import com.example.passwordManagement.entities.Password
import kotlinx.android.synthetic.main.password_item.view.*
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList

class PasswordAdapter() :
    RecyclerView.Adapter<PasswordAdapter.PasswordsViewHolder>() {

        var listener: OnItemClickListener? = null
        var arrList = ArrayList<Password>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordsViewHolder {
            return PasswordsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.password_item,parent,false))
    }

    override fun getItemCount(): Int {
        return arrList.size
    }

    fun setData(arrPasswordList: List<Password>){
        arrList = arrPasswordList as ArrayList<Password>
    }

    fun setOnClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    var AES = "AES"

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

    override fun onBindViewHolder(holder: PasswordsViewHolder, position: Int) {

        holder.itemView.txtService.text = arrList[position].service
        holder.itemView.txtPassword.text = decrypt(arrList[position].usernameEmail)
        holder.itemView.DateTime.text = arrList[position].dateTime

        if (arrList[position].color != null){
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(arrList[position].color))
        }else{
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor(R.color.ColorLightBlack.toString()))
        }

        if (arrList[position].webLink != ""){
            holder.itemView.WebLink.text = arrList[position].webLink
            holder.itemView.WebLink.visibility = View.VISIBLE
        }else{
            holder.itemView.WebLink.visibility = View.GONE
        }

        holder.itemView.cardView.setOnClickListener {
            listener!!.onClicked(arrList[position].id!!)
        }

    }

    class PasswordsViewHolder(view:View) : RecyclerView.ViewHolder(view){
    }

    interface OnItemClickListener{
        fun onClicked(PasswordId:Int)
    }

}
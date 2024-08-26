package com.example.passwordManagement.util

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.passwordManagement.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_password_bottom.*

class PasswordBottomFragment : BottomSheetDialogFragment() {
    var selectedColor = "#171C26"

    companion object {
        var passwordID = -1
        fun newInstance(id:Int): PasswordBottomFragment{
            val args = Bundle()
            val fragment = PasswordBottomFragment()
            fragment.arguments = args
            passwordID = id
            return fragment
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_password_bottom,null)
        dialog.setContentView(view)

        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams

        val behavior = param.behavior

        if (behavior is BottomSheetBehavior<*>){
            behavior.setBottomSheetCallback(object  : BottomSheetBehavior.BottomSheetCallback(){
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    var state = ""
                    when (newState) {
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            state = "DRAGGING"
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                            state = "SETTLING"
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            state = "EXPANDED"
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            state = "COLLAPSED"
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            state = "HIDDEN"
                            dismiss()
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }

                    }
                }

            })


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password_bottom,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (passwordID != -1){
            layoutDeletePassword.visibility = View.VISIBLE
        }else{
            layoutDeletePassword.visibility = View.GONE
        }
        setListener()
    }

    private fun setListener(){

        Box1.setOnClickListener {
            imgColor1.setImageResource(R.drawable.ic_tick)
            imgColor2.setImageResource(0)
            imgColor3.setImageResource(0)
            imgColor4.setImageResource(0)
            imgColor5.setImageResource(0)
            imgColor6.setImageResource(0)
            selectedColor = "#281A85"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Blue")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        Box2.setOnClickListener {
            imgColor1.setImageResource(0)
            imgColor2.setImageResource(R.drawable.ic_tick)
            imgColor3.setImageResource(0)
            imgColor4.setImageResource(0)
            imgColor5.setImageResource(0)
            imgColor6.setImageResource(0)
            selectedColor = "#E6C230"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Yellow")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        Box3.setOnClickListener {
            imgColor1.setImageResource(0)
            imgColor2.setImageResource(0)
            imgColor3.setImageResource(R.drawable.ic_tick)
            imgColor4.setImageResource(0)
            imgColor5.setImageResource(0)
            imgColor6.setImageResource(0)
            selectedColor = "#ae3b76"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Purple")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        Box4.setOnClickListener {
            imgColor1.setImageResource(0)
            imgColor2.setImageResource(0)
            imgColor3.setImageResource(0)
            imgColor4.setImageResource(R.drawable.ic_tick)
            imgColor5.setImageResource(0)
            imgColor6.setImageResource(0)
            selectedColor = "#20CC87"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Green")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        Box5.setOnClickListener {
            imgColor1.setImageResource(0)
            imgColor2.setImageResource(0)
            imgColor3.setImageResource(0)
            imgColor4.setImageResource(0)
            imgColor5.setImageResource(R.drawable.ic_tick)
            imgColor6.setImageResource(0)
            selectedColor = "#ff7746"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","Orange")
            intent.putExtra("selectedColor",selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        Box6.setOnClickListener {
            imgColor1.setImageResource(0)
            imgColor2.setImageResource(0)
            imgColor3.setImageResource(0)
            imgColor4.setImageResource(0)
            imgColor5.setImageResource(0)
            imgColor6.setImageResource(R.drawable.ic_tick)
            selectedColor = "#171C26"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Black")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        layoutWebUrl.setOnClickListener{
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","WebUrl")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
        layoutDeletePassword.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action","DeletePassword")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
    }
}
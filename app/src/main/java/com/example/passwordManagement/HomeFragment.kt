package com.example.passwordManagement

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.passwordManagement.adapter.PasswordAdapter
import com.example.passwordManagement.database.PasswordDatabase
import com.example.passwordManagement.entities.Password
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {

    var arrPassword = ArrayList<Password>()
    var passwordAdapter: PasswordAdapter = PasswordAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment().apply { arguments = Bundle().apply {} }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)

        recycler_view.setHasFixedSize(true)

        recycler_view.layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                var password = PasswordDatabase.getDatabase(it).PasswordDao().getAllPasswords()
                passwordAdapter.setData(password)
                arrPassword = password as ArrayList<Password>
                recycler_view.adapter = passwordAdapter
            }
        }

        passwordAdapter.setOnClickListener(onClicked)

        BtnCreatePassword.setOnClickListener {
            replaceFragment(CreatePasswordFragment.newInstance(),false)
        }

        imgBack2.setOnClickListener {
            activity?.finish()
        }

        search_view.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                var tempArr = ArrayList<Password>()

                for (arr in arrPassword){
                    if (arr.service!!.toLowerCase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }

                passwordAdapter!!.setData(tempArr)
                passwordAdapter!!.notifyDataSetChanged()
                return true
            }

        })


    }


    private val onClicked = object : PasswordAdapter.OnItemClickListener{
        override fun onClicked(PasswordID: Int) {


            var fragment :Fragment
            var bundle = Bundle()
            bundle.putInt("passwordID",PasswordID)
            fragment = CreatePasswordFragment.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment,false)
        }

    }


    fun replaceFragment(fragment:Fragment, istransition:Boolean){
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }


}
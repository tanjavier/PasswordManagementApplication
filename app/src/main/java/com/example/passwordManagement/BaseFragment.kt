package com.example.passwordManagement

import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(),CoroutineScope{

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job +Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
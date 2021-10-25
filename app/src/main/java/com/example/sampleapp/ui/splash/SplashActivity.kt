package com.example.sampleapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.sampleapp.R
import com.example.sampleapp.model.error.BaseError
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.ui.main.ErrorDialogFragment
import com.example.sampleapp.ui.main.MainActivity
import com.example.sampleapp.utils.errors.ErrorCode
import kotlinx.android.synthetic.main.splash_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    private val ratesObserver = Observer<List<Rate>> { rates ->
        if (rates.isNotEmpty()) {
            splashViewModel.getTransaction(rates)
        } else {
            showErrorRate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        splashViewModel.ratesLiveData.observe(this, ratesObserver)
        splashViewModel.getRates()

        startButton.setOnClickListener {
            continueToMainActivity()
        }
    }

    private fun continueToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        //finish()
    }

    private fun showErrorRate() {
        val error = BaseError(ErrorCode.NO_RATES_AVAILABLE, null)
        val listener = object : ErrorDialogFragment.ErrorListener {
            override fun onButtonClick() {
                continueToMainActivity()
            }
        }
        val dialog = ErrorDialogFragment.newInstance(error, listener)
        dialog.show(supportFragmentManager, ErrorDialogFragment.ERROR_FRAGMENT_TAG)
    }
}
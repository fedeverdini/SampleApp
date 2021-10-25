package com.example.sampleapp.ui.transaction.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapp.R
import com.example.sampleapp.model.DataEvent
import com.example.sampleapp.model.error.BaseError
import com.example.sampleapp.ui.main.ErrorDialogFragment
import com.example.sampleapp.ui.main.MainActivity
import com.example.sampleapp.ui.transaction.list.TransactionListFragment
import com.example.sampleapp.ui.transaction.rates.RateListFragment
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView
import com.example.sampleapp.utils.amount.IAmountUtils
import com.example.sampleapp.utils.extension.DoubleExtension.format
import com.example.sampleapp.utils.preferences.IPreferenceUtils
import com.example.sampleapp.utils.preferences.PreferenceUtils
import kotlinx.android.synthetic.main.transaction_details_fragment.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class TransactionDetailsFragment(private val sku: String) : Fragment() {

    private val transactionDetailsViewModel: TransactionDetailsViewModel by viewModel()

    private val amountUtils: IAmountUtils by inject()
    private val preferenceUtils: IPreferenceUtils by inject()

    private lateinit var adapter: TransactionDetailsAdapter

    private var page = 0

    private val uiStateObserver = Observer<DataEvent<TransactionDetailsUiState>> { event ->
        event.getContentIfNotHandled()?.let { state ->
            when (state) {
                TransactionDetailsUiState.Loading -> showLoading(true)
                TransactionDetailsUiState.ShowEmptyList -> showEmptyList()
                is TransactionDetailsUiState.SetTotal -> setTotal(state.amount)
                is TransactionDetailsUiState.ShowTransactionDetails -> showTransactionDetails(state.result)
                is TransactionDetailsUiState.ShowError -> showError(state.error)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.transaction_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionDetailsViewModel.uiState.observe(viewLifecycleOwner, uiStateObserver)

        setUpAdapter()
        setUpListeners()

        transactionDetailsViewModel.getTotalAmount(sku)
        transactionDetailsViewModel.getTransactionDetails(sku, page)
    }

    private fun setUpAdapter() {
        adapter = TransactionDetailsAdapter()

        txnDetailsRecyclerView.layoutManager = LinearLayoutManager(context)
        txnDetailsRecyclerView.adapter = adapter
    }

    private fun setUpListeners() {
        rateButton.setOnClickListener {
            val fragment = RateListFragment()
            (activity as MainActivity).replaceFragment(fragment, true)
        }
    }

    private fun setTotal(value: Double) {
        val currency = preferenceUtils.getString(PreferenceUtils.FINAL_CURRENCY)
        total.text = String.format(
            getString(R.string.transaction_details_total_amount),
            amountUtils.roundToTwoDecimals(value).format(2),
            currency
        )
    }

    private fun showTransactionDetails(transactions: List<TransactionDetailsView>) {
        showLoading(false)

        val list = transactions.toMutableList().apply { add(0, getHeaders()) }
        adapter.setTransactionItems(list)

        /*if (transactions.conversionRateError) {
            conversionErrorMessage.visibility = View.VISIBLE
        }*/

        rateButton.visibility = View.VISIBLE
    }

    private fun showEmptyList() {
        showLoading(false)
        emptyListView.visibility = View.VISIBLE
        txnDetailsRecyclerView.visibility = View.GONE
        conversionErrorMessage.visibility = View.GONE
        rateButton.visibility = View.GONE
        total.visibility = View.GONE
    }

    private fun showLoading(status: Boolean) {
        (activity as MainActivity).showLoading(status)
    }

    private fun getHeaders(): TransactionDetailsView {
        return TransactionDetailsView(
            sku = getString(R.string.transaction_details_sku),
            originalCurrency = getString(R.string.transaction_details_original_amount),
            originalAmount = 0.0,
            finalCurrency = getString(R.string.transaction_details_converted_amount),
            finalAmount = 0.0
        )
    }

    private fun showError(error: BaseError) {
        showLoading(false)
        val listener = object : ErrorDialogFragment.ErrorListener {
            override fun onButtonClick() {
                (activity as MainActivity).replaceFragment(TransactionListFragment())
            }
        }
        (activity as MainActivity).showError(error, listener)
    }
}
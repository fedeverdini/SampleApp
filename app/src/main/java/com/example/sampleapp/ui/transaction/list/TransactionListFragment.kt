package com.example.sampleapp.ui.transaction.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sampleapp.R
import com.example.sampleapp.model.DataEvent
import com.example.sampleapp.model.error.BaseError
import com.example.sampleapp.ui.transaction.details.TransactionDetailsFragment
import com.example.sampleapp.ui.main.MainActivity
import kotlinx.android.synthetic.main.transaction_list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: TransactionListViewModel by viewModel()

    private lateinit var adapter: TransactionListAdapter

    private var page = 0

    private val uiStateObserver = Observer<DataEvent<ProductListUiState>> { event ->
        event.getContentIfNotHandled()?.let { state ->
            when (state) {
                ProductListUiState.Loading -> showLoading(true)
                ProductListUiState.ShowEmptyList -> showEmptyList()
                is ProductListUiState.AddProductItems -> showTransactionList(state.result.toList())
                is ProductListUiState.ShowError -> showError(state.error)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.transaction_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiState.observe(viewLifecycleOwner, uiStateObserver)

        setListeners()
        setUpAdapter()

        viewModel.getProductList(page)
    }

    override fun onRefresh() {
        swipeToRefreshLayout.isRefreshing = false
        viewModel.getProductList(page)
    }

    private fun setListeners() {
        emptyListRefreshButton.setOnClickListener {
            onRefresh()
        }
    }

    private fun setUpAdapter() {
        val listener = object : TransactionListAdapter.TransactionClickListener {
            override fun onItemClick(sku: String) {
                val fragment = TransactionDetailsFragment(sku)
                (activity as MainActivity).replaceFragment(fragment)
            }
        }

        adapter = TransactionListAdapter(listener = listener)

        transactionListRecyclerView.layoutManager = LinearLayoutManager(context)
        transactionListRecyclerView.adapter = adapter
    }

    private fun showLoading(status: Boolean) {
        (activity as MainActivity).showLoading(status)
    }

    private fun showTransactionList(result: List<String>) {
        showLoading(false)
        emptyListView.visibility = View.GONE
        emptyListRefreshButton.visibility = View.GONE
        swipeToRefreshLayout.isRefreshing = false
        adapter.addTransactionItems(result)
        transactionListRecyclerView.visibility = View.VISIBLE
    }

    private fun showEmptyList() {
        showLoading(false)
        showEmptyListView()
        emptyListRefreshButton.visibility = View.GONE
    }

    private fun showError(error: BaseError) {
        showLoading(false)
        showEmptyListView()
        emptyListRefreshButton.visibility = View.VISIBLE
        swipeToRefreshLayout.isRefreshing = false
        (activity as MainActivity).showError(error)
    }

    private fun showEmptyListView() {
        emptyListView.visibility = View.VISIBLE
        swipeToRefreshLayout.isRefreshing = false
        transactionListRecyclerView.visibility = View.GONE
    }
}
package com.example.sampleapp.ui.transaction.rates

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
import com.example.sampleapp.model.rate.Rate
import com.example.sampleapp.ui.main.MainActivity
import kotlinx.android.synthetic.main.rate_list_fragment.*
import kotlinx.android.synthetic.main.rate_list_fragment.emptyListView
import kotlinx.android.synthetic.main.transaction_details_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RateListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel: RateListViewModel by viewModel()

    private lateinit var adapter: RateListAdapter

    private val uiStateObserver = Observer<DataEvent<RateListUiState>> { event ->
        event.getContentIfNotHandled()?.let { state ->
            when (state) {
                RateListUiState.Loading -> showLoading(true)
                RateListUiState.ShowEmptyList -> showEmptyList()
                is RateListUiState.AddRateItems -> showRateList(state.result)
                is RateListUiState.ShowError -> showError(state.error)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.rate_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiState.observe(viewLifecycleOwner, uiStateObserver)

        setListeners()
        setUpAdapter()

        viewModel.getRateList()
    }

    override fun onRefresh() {
        swipeToRefreshLayout.isRefreshing = false
        viewModel.getRateList()
    }

    private fun setListeners() {
        swipeToRefreshLayout.setOnRefreshListener {
            onRefresh()
        }

        emptyListRefreshButton.setOnClickListener {
            onRefresh()
        }
    }

    private fun setUpAdapter() {
        adapter = RateListAdapter()

        rateListRecyclerView.layoutManager = LinearLayoutManager(context)
        rateListRecyclerView.adapter = adapter
    }

    private fun showLoading(status: Boolean) {
        (activity as MainActivity).showLoading(status)
    }

    private fun showRateList(result: List<Rate>) {
        showLoading(false)
        emptyListView.visibility = View.GONE
        emptyListRefreshButton.visibility = View.GONE
        swipeToRefreshLayout.isRefreshing = false
        adapter.setTransactionItems(result)
        rateListRecyclerView.visibility = View.VISIBLE
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
        rateListRecyclerView.visibility = View.GONE
    }
}
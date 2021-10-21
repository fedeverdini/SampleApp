package com.example.sampleapp.ui.transaction.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapp.R
import com.example.sampleapp.ui.transaction.view.TransactionDetailsView
import com.example.sampleapp.utils.extension.DoubleExtension.format
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class TransactionDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val HEADER = 0
        private const val ITEM = 1
    }

    private var list: MutableList<TransactionDetailsView> = mutableListOf()

    fun setTransactionItems(txnList: List<TransactionDetailsView>) {
        this.list = txnList.toMutableList()
        notifyDataSetChanged()
    }

    fun addTransactionItems(txnList: List<TransactionDetailsView>) {
        val positionStart = list.size
        this.list.addAll(txnList)
        notifyItemRangeInserted(positionStart, txnList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_details_recyclerview_item, parent, false)
        return when (viewType) {
            HEADER -> HeaderViewHolder(view)
            else -> ItemViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER
            else -> ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        when (holder) {
            is HeaderViewHolder -> holder.bind(item)
            is ItemViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class HeaderViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val sku: TextView = itemView.findViewById(R.id.sku)
        private val originalAmount: TextView = itemView.findViewById(R.id.originalAmount)
        private val convertedAmount: TextView = itemView.findViewById(R.id.convertedAmount)

        fun bind(txnDetail: TransactionDetailsView) {
            this.sku.text = txnDetail.sku
            this.originalAmount.text = txnDetail.originalCurrency
            this.convertedAmount.text = txnDetail.finalCurrency
        }
    }

    class ItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        KoinComponent {

        private val context: Context by inject()

        private val sku: TextView = itemView.findViewById(R.id.sku)
        private val originalAmount: TextView = itemView.findViewById(R.id.originalAmount)
        private val convertedAmount: TextView = itemView.findViewById(R.id.convertedAmount)

        fun bind(txnDetail: TransactionDetailsView) {
            this.sku.text = txnDetail.sku
            this.originalAmount.text = String.format(
                context.getString(
                    R.string.amount_with_currency,
                    txnDetail.originalAmount.format(2),
                    txnDetail.originalCurrency
                )
            )
            this.convertedAmount.text = String.format(
                context.getString(
                    R.string.amount_with_currency,
                    txnDetail.finalAmount.format(2),
                    txnDetail.finalCurrency
                )
            )
        }
    }
}
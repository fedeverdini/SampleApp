package com.example.sampleapp.ui.transaction.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapp.R

class TransactionListAdapter(
    private var transactionList: MutableList<String> = mutableListOf(),
    private var listener: TransactionClickListener? = null
) : RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {

    fun addTransactionItems(data: List<String>) {
        val positionStart = transactionList.size
        this.transactionList.addAll(data)
        notifyItemRangeInserted(positionStart, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.transaction_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.bind(transaction, listener)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txnContainer: ConstraintLayout = itemView.findViewById(R.id.txnContainer)
        private val txnSku: TextView = itemView.findViewById(R.id.txnSku)

        fun bind(sku: String, listener: TransactionClickListener?) {
            txnSku.text = sku

            txnContainer.setOnClickListener {
                listener?.onItemClick(sku)
            }
        }
    }

    interface TransactionClickListener {
        fun onItemClick(sku: String)
    }
}
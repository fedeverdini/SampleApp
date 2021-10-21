package com.example.sampleapp.ui.transaction.rates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapp.R
import com.example.sampleapp.model.rate.Rate

class RateListAdapter(
    private var rateList: MutableList<Rate> = mutableListOf()
) : RecyclerView.Adapter<RateListAdapter.ViewHolder>() {

    fun setTransactionItems(data: List<Rate>) {
        this.rateList = data.toMutableList()
        notifyDataSetChanged()
    }

    fun addTransactionItems(data: List<Rate>) {
        val positionStart = rateList.size
        this.rateList.addAll(data)
        notifyItemRangeInserted(positionStart, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rate_recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rate = rateList[position]
        holder.bind(rate)
    }

    override fun getItemCount(): Int {
        return rateList.size
    }

    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val from: TextView = itemView.findViewById(R.id.from)
        private val to: TextView = itemView.findViewById(R.id.to)
        private val rate: TextView = itemView.findViewById(R.id.rate)

        fun bind(rate: Rate) {
            this.from.text = rate.from
            this.to.text = rate.to
            this.rate.text = rate.rate
        }
    }
}
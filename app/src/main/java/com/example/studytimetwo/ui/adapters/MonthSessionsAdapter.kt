package com.example.studytimetwo.ui.adapters

import com.example.studytimetwo.R
import kotlinx.android.synthetic.main.month_list_item.view.*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView


class MonthSessionsAdapter( val listener: (Int) -> Unit) : RecyclerView.Adapter<MonthSessionsAdapter.MonthsWithSessionsViewHolder>() {


    /*
        Manually call setData from the Activity because we need to update the recycler view when we have data.
        When there is a change to the years list we set this adapter's data with set data
     */

    private val months = arrayListOf<String>("January", "February" ,"March", "April", "May", "June", "July", "August", "September", "October", "November", "December")


    fun setData(months: List<Int>){
        monthsList = months
        notifyDataSetChanged()
    }

    var monthsList = emptyList<Int>()

    class MonthsWithSessionsViewHolder(val cardView: CardView): RecyclerView.ViewHolder(cardView){

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonthsWithSessionsViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.month_list_item, parent, false) as CardView
        return MonthsWithSessionsViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: MonthsWithSessionsViewHolder, position: Int) {
        val month = monthsList[position]
        holder.cardView.month_item_text_view.text = months[monthsList[position] - 1]

        holder.cardView.setOnClickListener { listener(month) } // setOnClickListener is a lambda so we define it by invoking the listener lambda member passing it month as parameter
    }

    override fun getItemCount() = monthsList.size


}
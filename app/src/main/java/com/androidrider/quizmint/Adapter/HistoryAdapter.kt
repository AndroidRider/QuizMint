package com.androidrider.quizmint.Adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidrider.quizmint.Model.HistoryModel
import com.androidrider.quizmint.R
import com.androidrider.quizmint.databinding.HistoryItemBinding
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class HistoryAdapter(private var historyList:ArrayList<HistoryModel>):
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(HistoryItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount()=historyList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        var dataList=historyList[position]

        holder.binding.coin.text=if (dataList.isWithdrawal){"- ${dataList.coin}"}else{"+ ${dataList.coin}"}
        // Set text color based on whether it's a withdrawal or deposit
        val context = holder.binding.coin.context
        holder.binding.coin.setTextColor(
            if (dataList.isWithdrawal) {
                context.getColor(R.color.historyRedColor)
            } else {
                context.getColor(R.color.historyGreenColor)
            }
        )


        holder.binding.historyName.text = if (dataList.isWithdrawal) { "Coin Withdrawal" } else { "Coin Win" }

        val timestamp = Timestamp(dataList.timeAndDate.toLong())
        val date = Date(timestamp.time)
        val calendar = Calendar.getInstance()
        calendar.time = date

        val currentCalendar = Calendar.getInstance()

        val formattedDate = when {
            DateUtils.isToday(calendar.timeInMillis) -> {
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                "Today ${timeFormat.format(date)}"
            }
            isYesterday(calendar) -> {
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                "Yesterday ${timeFormat.format(date)}"
            }
            else -> {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy, hh:mm a", Locale.getDefault())
                dateFormat.format(date)
            }
        }
        holder.binding.txtTimeAndDate.text = formattedDate


    }

    class HistoryViewHolder(var binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root) {}

    private fun isYesterday(calendar: Calendar): Boolean {
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DATE, -1)
        }
        return calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)
    }

    fun updateList(newList: List<HistoryModel>) {
        historyList = ArrayList(newList)
        notifyDataSetChanged()
    }
}
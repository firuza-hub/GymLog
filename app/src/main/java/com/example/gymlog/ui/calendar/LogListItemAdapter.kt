package com.example.gymlog.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gymlog.R
import com.example.gymlog.data.models.WorkoutLog
import com.example.gymlog.databinding.FragmentLoglistItemBinding

class LogListItemAdapter(val redirectToLogView: (id:String) -> Unit) : RecyclerView.Adapter<LogListItemAdapter.LogListItemViewHolder>() {

    private var data = mutableListOf<WorkoutLog>()

    class LogListItemViewHolder(val binding: FragmentLoglistItemBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogListItemViewHolder {
        val binding = DataBindingUtil.inflate<FragmentLoglistItemBinding>(LayoutInflater.from(parent.context), R.layout.fragment_loglist_item,parent, false )
        return LogListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogListItemViewHolder, position: Int) {
        val currentLog = data[position]
        holder.binding.item = currentLog
        holder.binding.root.setOnClickListener{redirectToLogView(currentLog.id!!)}
    }



    override fun getItemCount(): Int {
        return data.size
    }

    fun setData( newData: List<WorkoutLog>){
        data = newData as MutableList<WorkoutLog>
        notifyDataSetChanged()
    }
}
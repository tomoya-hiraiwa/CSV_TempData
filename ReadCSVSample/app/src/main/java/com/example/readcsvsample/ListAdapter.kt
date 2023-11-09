package com.example.readcsvsample


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.readcsvsample.databinding.ListItemBinding

class ListAdapter(private val dataList: MutableList<TempData>):RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    inner class ListViewHolder(private val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(data: TempData){
            binding.listKen.text = data.ken
            binding.listPlace.text = data.place
            binding.listMaxTemp.text = data.temp
            binding.listMaxTempInYear.text = data.maxTemp
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = dataList[position]
        holder.bindData(data)
    }
}
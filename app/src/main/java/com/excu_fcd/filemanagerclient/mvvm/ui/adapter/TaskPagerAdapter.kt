package com.excu_fcd.filemanagerclient.mvvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.excu_fcd.core.data.model.Task
import com.excu_fcd.filemanagerclient.R
import com.excu_fcd.filemanagerclient.mvvm.ui.adapter.viewholder.TaskViewHolder
import kotlin.random.Random

class TaskPagerAdapter : AbsAdapter<Task, TaskViewHolder>(callback = callback) {

    companion object {
        val callback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.state == oldItem.state
            }
        }
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val binding = holder.binding
        binding.operationName.text = "Task: $position"
        binding.imageState.setImageResource(if (Random.nextBoolean()) R.drawable.ic_done_24 else R.drawable.ic_close_24)
    }
}
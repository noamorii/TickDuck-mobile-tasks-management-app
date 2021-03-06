package cz.cvut.fel.pda.tickduck.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fel.pda.tickduck.R
import cz.cvut.fel.pda.tickduck.databinding.TodoItemBinding
import cz.cvut.fel.pda.tickduck.model.Todo

class TodoAdapter(
    private val listener: Listener
) : ListAdapter<Todo, TodoAdapter.TaskHolder>(Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class TaskHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = TodoItemBinding.bind(view)

        fun setData(task: Todo, listener: Listener) = with(binding) {
            taskTitle.text = task.name
            taskCheckbox.isChecked = task.isCompleted
            taskCheckbox.buttonTintList = ColorStateList.valueOf(task.priorityEnum.toArgb(root.context))
            taskCheckbox.setOnClickListener {
                listener.onClickCheckbox(task.copy(isCompleted = taskCheckbox.isChecked))
            }
            itemView.setOnClickListener {
                listener.onClickItem(task)
            }
        }

        companion object {
            fun create(parent: ViewGroup): TaskHolder {
                return TaskHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.todo_item, parent, false))
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun onClickItem(task: Todo)
        fun deleteTodo(id: Int)
        fun onClickCheckbox(task: Todo)
    }
}
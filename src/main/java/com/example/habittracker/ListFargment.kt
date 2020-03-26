package com.example.habittracker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_habits_list.*

interface ClickListener {
    fun onItemClick(clickedItemIndex: Int)
}

class ListFragment: Fragment(), ClickListener {
    var indexOfClickedHabit = -1
    lateinit var adapter: ListFragment.Adapter

    private lateinit var habitClickListener: HabitClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        habitClickListener = context as HabitClickListener
    }

    companion object {
        private const val ARGS_NAME = "args_name"

        fun newInstance(name: String) : ListFragment {
            val fragment = ListFragment()
            val bundle = Bundle()
            bundle.putString(name, ARGS_NAME)
            fragment.arguments = bundle
            return fragment
        }
    }

    val name: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_habits_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = Adapter(mutableListOf(), this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(clickedItemIndex: Int) {
        indexOfClickedHabit = clickedItemIndex
        val habit = adapter.getHabit(clickedItemIndex)
        habitClickListener.onHabitClick(habit, clickedItemIndex)
    }

    class Adapter(
        private val habits: MutableList<Habit>, private val listener: ClickListener
    ) : RecyclerView.Adapter<Adapter.ViewHolder>() {
        class ViewHolder(itemView: View, private val listener: ClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            private val habitTitle: TextView = itemView.findViewById(R.id.habitTitle)
            private val habitDescription: TextView = itemView.findViewById(R.id.habitDescription)
            private val habitPriority: TextView = itemView.findViewById(R.id.habitPriority)
            private val habitType: TextView = itemView.findViewById(R.id.habitType)
            private val repetitionsCount: TextView = itemView.findViewById(R.id.repetitionsCount)
            private val periodicity: TextView = itemView.findViewById(R.id.periodicity)

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                listener.onItemClick(adapterPosition)
            }

            @SuppressLint("SetTextI18n")
            fun bind(habit: Habit) {
                habitTitle.text = habit.title
                habitDescription.text = habit.description
                habitPriority.text = "${priorityToText[habit.priority]} приоритет"
                habitType.text = when (habit.type) {
                    HabitType.Bad -> "Вредная привычка"
                    HabitType.Good -> "Хорошая привычка"
                }
                repetitionsCount.text = "Повторений: ${habit.count}"
                periodicity.text = "Периодичность: ${habit.periodicity}"
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.layout.habit_list_item, parent, false), listener)
        }

        override fun getItemCount(): Int = habits.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(habits[position])
        }

        fun addHabit(habit: Habit) {
            habits.add(habit)
            notifyItemInserted(habits.lastIndex)
        }

        fun getHabit(habitIndex: Int): Habit {
            return habits[habitIndex]
        }

        fun changeHabit(habit: Habit, habitIndex: Int) {
            habits[habitIndex] = habit
            notifyItemChanged(habitIndex)
        }

        fun deleteHabit(habitIndex: Int) {
            habits.removeAt(habitIndex)
            notifyItemRemoved(habitIndex)
        }
    }
}
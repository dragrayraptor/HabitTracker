package com.example.habittracker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_pager.*

interface FabClickListener {
    fun onFabClick()
}

interface HabitSaveClickListener {
    fun onHabitSave(habit: Habit)
}

interface HabitClickListener {
    fun onHabitClick(habit: Habit, index: Int)
}

class MainActivity : AppCompatActivity(), FabClickListener, HabitSaveClickListener, HabitClickListener {

    //    var indexOfClickedHabit = -1
//    private lateinit var adapter: Adapter
//
    private lateinit var appBarConfiguration: AppBarConfiguration

    private var viewPagerFragment = ViewPagerFragment()
    private val infoFragment = InfoFragment()
    private var habitAdditionFragment: HabitAdditionFragment? = null

    private var lastClickedHabit: Habit? = null
    private var lastClickedHabitIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        setSupportActionBar(toolbar)

        val drawerToggle =
            ActionBarDrawerToggle(
                this,
                drawer_layout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )
        drawer_layout.addDrawerListener(drawerToggle)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_navigation_fragment, viewPagerFragment)
                .commit()
        }
        nav_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager
                        .beginTransaction()
                        .remove(infoFragment)
                        .show(viewPagerFragment)
                        .commit()
                }
                else -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(viewPagerFragment)
                        .add(R.id.content_navigation_fragment, infoFragment)
                        .commit()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onFabClick() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .hide(viewPagerFragment)
        habitAdditionFragment = HabitAdditionFragment()
        transaction
            .add(R.id.content_navigation_fragment, habitAdditionFragment!!)
            .commit()
    }

    override fun onHabitSave(habit: Habit) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .remove(habitAdditionFragment!!)

        val viewPagerAdapter = viewPager.adapter as HabitsPagerAdapter
        val habitsList = when (habit.type) {
            HabitType.Good -> viewPagerAdapter.goodHabits
            HabitType.Bad -> viewPagerAdapter.badHabits
        }
        if (lastClickedHabit == null) {
            habitsList.adapter.addHabit(habit)
        } else {
            if (lastClickedHabit!!.type == habit.type) {
                habitsList.adapter.changeHabit(habit, lastClickedHabitIndex)
            } else {
                habitsList.adapter.addHabit(habit)
                if (habit.type == HabitType.Good)
                    viewPagerAdapter.badHabits.adapter.deleteHabit(lastClickedHabitIndex)
                else
                    viewPagerAdapter.goodHabits.adapter.deleteHabit(lastClickedHabitIndex)
            }
            lastClickedHabit = null
            lastClickedHabitIndex = -1
        }

        transaction
            .show(viewPagerFragment)
            .commit()
    }

    override fun onHabitClick(habit: Habit, index: Int) {
        lastClickedHabit = habit
        lastClickedHabitIndex = index
        habitAdditionFragment = HabitAdditionFragment.newInstance(habit)
        supportFragmentManager
            .beginTransaction()
            .hide(viewPagerFragment)
            .add(R.id.content_navigation_fragment, habitAdditionFragment!!)
            .commit()
    }
}
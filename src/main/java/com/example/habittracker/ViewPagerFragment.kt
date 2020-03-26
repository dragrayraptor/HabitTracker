package com.example.habittracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_pager.*


class HabitsPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {
    val goodHabits: ListFragment = ListFragment.newInstance("good_habits")
    val badHabits: ListFragment = ListFragment.newInstance("bad_habits")

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> goodHabits
        else -> badHabits
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0)
            "Хорошие привычки"
        else
            "Вредные привычки"
    }
}

class ViewPagerFragment: Fragment() {
    private lateinit var fabClickListener: FabClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fabClickListener = context as FabClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pager, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = HabitsPagerAdapter(childFragmentManager)
        tabLayout.setupWithViewPager(viewPager)

        fab.setOnClickListener { view ->
            fabClickListener.onFabClick()
        }
    }

}
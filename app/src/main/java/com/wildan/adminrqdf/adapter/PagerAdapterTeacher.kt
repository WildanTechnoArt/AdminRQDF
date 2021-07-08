package com.wildan.adminrqdf.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.wildan.adminrqdf.fragment.ApprovedFragment
import com.wildan.adminrqdf.fragment.NotApprovedFragment

class PagerAdapterTeacher(fm: FragmentManager, behavior: Int) :
    FragmentStatePagerAdapter(fm, behavior) {

    private val pages =
        listOf(ApprovedFragment(), NotApprovedFragment())

    //Mengembalikan Fragment yang terkait dengan posisi tertentu
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    //Mengembalikan jumlah tampilan yang tersedia.
    override fun getCount(): Int {
        return pages.size
    }
}
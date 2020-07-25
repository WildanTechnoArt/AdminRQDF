package com.wildan.adminrqdf.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.adapter.PagerAdapterTeacher

class TeacherListFragment : Fragment() {

    companion object {
        const val BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_teacher_list, container, false)
        setHasOptionsMenu(true)

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar
        ((activity as AppCompatActivity).setSupportActionBar(toolbar))

        val viewPager = view.findViewById(R.id.container) as ViewPager

        val tabsTeacher = view.findViewById(R.id.tabs_teacher) as TabLayout

        //Memanggil dan Memasukan Value pada Class PagerAdapter(FragmentManager dan JumlahTab)
        
        val pageAdapter = PagerAdapterTeacher(
            childFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )

        //Memasang Adapter pada ViewPager
        viewPager.adapter = pageAdapter

        /*
         Menambahkan Listener yang akan dipanggil kapan pun halaman berubah atau
         bergulir secara bertahap, sehingga posisi tab tetap singkron
         */
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsTeacher))

        //Callback Interface dipanggil saat status pilihan tab berubah.
        tabsTeacher.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //Dipanggil ketika tab memasuki state/keadaan yang dipilih.
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return view
    }
}
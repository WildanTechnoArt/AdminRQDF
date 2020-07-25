package com.wildan.adminrqdf.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.adapter.PagerAdapterClass
import kotlinx.android.synthetic.main.activity_class_room.*

class ClassRoomActivity : AppCompatActivity() {

    private val tabMenu = arrayOf("Beranda", "Santri", "Diskusi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_room)
        prepare()
    }

    private fun prepare() {
        setSupportActionBar(toolbar)

        val pageAdapter = PagerAdapterClass(this)

        view_pager.adapter = pageAdapter

        TabLayoutMediator(
            tab_layout,
            view_pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text = tabMenu[position]
            }).attach()
    }
}
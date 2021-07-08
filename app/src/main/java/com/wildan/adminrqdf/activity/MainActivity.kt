package com.wildan.adminrqdf.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.fragment.ClassListFragment
import com.wildan.adminrqdf.fragment.TeacherListFragment
import com.wildan.adminrqdf.utils.UtilsConstant.KEY_FRAGMENT
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var pageContent: Fragment? = ClassListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        pageContent?.let { supportFragmentManager.putFragment(outState, KEY_FRAGMENT, it) }
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onNavigationItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.class_list -> pageContent = ClassListFragment()
            R.id.teacher_list -> pageContent = TeacherListFragment()
            R.id.logout -> {
                val builder = MaterialAlertDialogBuilder(this)
                    .setTitle("Konfirmasi")
                    .setMessage("Anda Yakin Ingin Keluar?")
                    .setPositiveButton("Ya") { _, _ ->
                        progressBar?.visibility = View.VISIBLE
                        AuthUI.getInstance()
                            .signOut(this)
                            .addOnSuccessListener {
                                progressBar?.visibility = View.GONE
                                Toast.makeText(this, getString(R.string.request_logout), Toast.LENGTH_SHORT).show()
                                finish()
                            }.addOnFailureListener {
                                progressBar?.visibility = View.GONE
                                Toast.makeText(this, getString(R.string.request_error), Toast.LENGTH_SHORT).show()
                            }
                    }
                    .setNegativeButton("Tidak"){ dialog, _ ->
                        dialog.dismiss()
                        bottom_navigation?.selectedItemId = R.id.teacher_list
                    }
                builder.setCancelable(false)
                val dialog = builder.create()
                dialog.show()
            }
        }

        pageContent?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, it)
                .commit()
        }
        return true
    }

    private fun init(savedInstanceState: Bundle?) {
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        if (savedInstanceState == null) {
            pageContent?.let {
                supportFragmentManager.beginTransaction().replace(R.id.container, it).commit()
            }
        } else {
            pageContent = supportFragmentManager.getFragment(savedInstanceState, KEY_FRAGMENT)
            pageContent?.let {
                supportFragmentManager.beginTransaction().replace(R.id.container, it).commit()
            }
        }
    }
}
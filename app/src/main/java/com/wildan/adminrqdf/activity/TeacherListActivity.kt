package com.wildan.adminrqdf.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.adapter.FirestoreTeacherAdapter
import com.wildan.adminrqdf.model.TeacherModel
import kotlinx.android.synthetic.main.activity_teaacher_list.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class TeacherListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teaacher_list)
        prepare()
        checkClass()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun prepare() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Daftar Guru"
        }

        swipe_refresh?.setOnRefreshListener {
            checkClass()
        }
    }

    private fun requestData() {
        val query = FirebaseFirestore.getInstance()
            .collection("darulfalah")
            .document("teacher")
            .collection("teacherList")

        val options = FirestoreRecyclerOptions.Builder<TeacherModel>()
            .setQuery(query, TeacherModel::class.java)
            .setLifecycleOwner(this)
            .build()

        rv_teachers?.layoutManager = LinearLayoutManager(this)
        rv_teachers?.setHasFixedSize(true)

        val adapter = FirestoreTeacherAdapter(options)
        rv_teachers?.adapter = adapter
    }

    private fun checkClass() {
        swipe_refresh.isRefreshing = true

        val db = FirebaseFirestore.getInstance()
        db.collection("darulfalah")
            .document("teacher")
            .collection("teacherList")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot?.isEmpty == true) {
                    tv_not_teachers?.visibility = View.VISIBLE
                    rv_teachers?.visibility = View.GONE
                } else {
                    tv_not_teachers?.visibility = View.GONE
                    rv_teachers?.visibility = View.VISIBLE
                    requestData()
                }

                swipe_refresh.isRefreshing = false
            }
    }
}
package com.wildan.adminrqdf.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore

import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.adapter.FirestoreClassAdapter
import com.wildan.adminrqdf.model.ClassModel
import com.wildan.adminrqdf.activity.CreateClassActivity
import kotlinx.android.synthetic.main.fragment_class_list.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class ClassListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (view.context as AppCompatActivity).setSupportActionBar(toolbar)
        (view.context as AppCompatActivity).supportActionBar?.title = "Daftar Kelas"
        prepare()
        checkClass()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_class_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create_class -> {
                startActivity(Intent(context, CreateClassActivity::class.java))
            }
        }
        return true
    }

    private fun prepare() {
        swipe_refresh?.setOnRefreshListener {
            checkClass()
        }
    }

    private fun requestData() {
        val query = FirebaseFirestore.getInstance()
            .collection("classList")
            .orderBy("teacherName")

        val options = FirestoreRecyclerOptions.Builder<ClassModel>()
            .setQuery(query, ClassModel::class.java)
            .setLifecycleOwner(this)
            .build()

        rv_class_list?.layoutManager = LinearLayoutManager(context)
        rv_class_list?.setHasFixedSize(true)

        val adapter = context?.let { FirestoreClassAdapter(options) }
        rv_class_list?.adapter = adapter
    }

    private fun checkClass() {
        swipe_refresh?.isRefreshing = true

        val db = FirebaseFirestore.getInstance()
        db.collection("classList")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot?.isEmpty == true) {
                    tv_no_class?.visibility = View.VISIBLE
                    rv_class_list?.visibility = View.GONE
                } else {
                    tv_no_class?.visibility = View.GONE
                    rv_class_list?.visibility = View.VISIBLE
                    requestData()
                }

                swipe_refresh?.isRefreshing = false
            }
    }
}
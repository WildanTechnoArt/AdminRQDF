package com.wildan.adminrqdf.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.adapter.FirestorePostAdapter
import com.wildan.adminrqdf.model.PostData
import com.wildan.adminrqdf.utils.UtilsConstant.CLASS_ID
import kotlinx.android.synthetic.main.fragment_class_post.*

class ClassPostFragment : Fragment() {

    private lateinit var codeClass: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_class_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        codeClass = (context as AppCompatActivity).intent?.getStringExtra(CLASS_ID).toString()
        setupDatabse()
        getDataCount()
        swipe_refresh.setOnRefreshListener {
            setupDatabse()
            getDataCount()
        }
    }

    private fun setupDatabse() {
        val query = FirebaseFirestore.getInstance()
            .collection("classRoom")
            .document(codeClass)
            .collection("posts")

        val options = FirestoreRecyclerOptions.Builder<PostData>()
            .setQuery(query, PostData::class.java)
            .setLifecycleOwner(this)
            .build()

        rv_post_list.layoutManager = LinearLayoutManager(context)
        rv_post_list.setHasFixedSize(true)

        val adapter = FirestorePostAdapter(options)
        rv_post_list.adapter = adapter
    }

    private fun getDataCount() {
        val db = FirebaseFirestore.getInstance()
            .collection("classRoom")
            .document(codeClass)
            .collection("posts")

        db.addSnapshotListener { snapshot, _ ->
            if ((snapshot?.size() ?: 0) > 0) {
                rv_post_list.visibility = View.VISIBLE
                tv_nothing_posts.visibility = View.GONE
            } else {
                rv_post_list.visibility = View.GONE
                tv_nothing_posts.visibility = View.VISIBLE
            }
            swipe_refresh.isRefreshing = false
        }
    }
}
package com.wildan.adminrqdf.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.adapter.FirestoreNotApprovedAdapter
import com.wildan.adminrqdf.model.TeacherModel
import com.wildan.adminrqdf.presenter.ApprovedPresenter
import com.wildan.adminrqdf.view.ApprovedView
import kotlinx.android.synthetic.main.fragment_teachers.*

class NotApprovedFragment : Fragment(), ApprovedView.View {

    private lateinit var presenter: ApprovedView.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teachers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepare(view)
        checkClass()
    }

    private fun prepare(view: View) {
        presenter = ApprovedPresenter(view.context, this)
        swipe_refresh?.setOnRefreshListener {
            checkClass()
        }
    }

    private fun requestData() {
        val query = FirebaseFirestore.getInstance()
            .collection("darulfalah")
            .document("teacher")
            .collection("newRegistrants")
            .orderBy("username")

        val options = FirestoreRecyclerOptions.Builder<TeacherModel>()
            .setQuery(query, TeacherModel::class.java)
            .setLifecycleOwner(this)
            .build()

        rv_teachers?.layoutManager = LinearLayoutManager(context)
        rv_teachers?.setHasFixedSize(true)

        val adapter = FirestoreNotApprovedAdapter(options, presenter)
        rv_teachers?.adapter = adapter
    }

    private fun checkClass() {
        swipe_refresh?.isRefreshing = true

        val db = FirebaseFirestore.getInstance()
        db.collection("darulfalah")
            .document("teacher")
            .collection("newRegistrants")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot?.isEmpty == true) {
                    tv_not_teachers?.visibility = View.VISIBLE
                    rv_teachers?.visibility = View.GONE
                } else {
                    tv_not_teachers?.visibility = View.GONE
                    rv_teachers?.visibility = View.VISIBLE
                    requestData()
                }

                swipe_refresh?.isRefreshing = false
            }
    }

    override fun onSuccess(message: String) {
        swipe_refresh?.isRefreshing = false
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun handleError(message: String) {
        swipe_refresh?.isRefreshing = false
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {
        swipe_refresh?.isRefreshing = true
    }

    override fun hideProgressBar() {
        swipe_refresh?.isRefreshing = false
    }
}

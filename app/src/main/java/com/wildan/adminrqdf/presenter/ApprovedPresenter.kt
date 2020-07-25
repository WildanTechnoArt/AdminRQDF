package com.wildan.adminrqdf.presenter

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.view.ApprovedView

class ApprovedPresenter(
    private val context: Context,
    private val view: ApprovedView.View
) : ApprovedView.Presenter {

    override fun postData(userId: String?, data: HashMap<String, String?>) {
        view.showProgressBar()

        val db = FirebaseFirestore.getInstance()
        db.collection("darulfalah")
            .document("teacher")
            .collection("teacherList")
            .document(userId.toString())
            .set(data)
            .addOnSuccessListener {
                deleteTeacher(userId.toString())
            }.addOnFailureListener {
                view.hideProgressBar()
                view.handleError(it.message.toString())
            }
    }

    private fun deleteTeacher(userId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("darulfalah")
            .document("teacher")
            .collection("newRegistrants")
            .document(userId)
            .delete()
            .addOnSuccessListener {
                view.onSuccess(context.getString(R.string.success_add_teacher))
            }.addOnFailureListener {
                view.hideProgressBar()
                view.handleError(it.message.toString())
            }
    }
}
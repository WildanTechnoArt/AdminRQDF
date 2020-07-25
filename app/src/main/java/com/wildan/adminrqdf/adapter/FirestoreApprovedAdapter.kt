package com.wildan.adminrqdf.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.wildan.adminrqdf.GlideApp
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.activity.TeacherProfileActivity
import com.wildan.adminrqdf.model.TeacherModel
import com.wildan.adminrqdf.utils.UtilsConstant.GET_PROFILE
import com.wildan.adminrqdf.utils.UtilsConstant.IS_OFFICIAL_TEACHER
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.approved_item.view.*

class FirestoreApprovedAdapter(options: FirestoreRecyclerOptions<TeacherModel>) :
    FirestoreRecyclerAdapter<TeacherModel, FirestoreApprovedAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.approved_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: TeacherModel) {
        val getUserId = snapshots.getSnapshot(position).id
        val context = holder.containerView.context

        holder.apply {
            val db = FirebaseFirestore.getInstance()
            db.collection("photos")
                .document(getUserId)
                .addSnapshotListener { snapshot, _ ->
                    val getPhoto = snapshot?.getString("photoUrl").toString()
                    GlideApp.with(containerView.context)
                        .load(getPhoto)
                        .placeholder(R.drawable.profile_placeholder)
                        .into(containerView.img_profile)
                }

            containerView.tv_teacher_name.text = item.username.toString()

            containerView.tv_nomor_induk.text = String.format(
                context?.getString(R.string.show_register_number).toString(),
                item.registrationNumber.toString()
            )

            containerView.card_teacher.setOnClickListener {
                toProfileActivity(position, context)
            }

            containerView.btn_profile.setOnClickListener {
                toProfileActivity(position, context)
            }
        }
    }

    private fun toProfileActivity(position: Int, context: Context) {
        val getUserId = snapshots.getSnapshot(position).id
        val intent = Intent(context, TeacherProfileActivity::class.java)
        intent.putExtra(GET_PROFILE, getUserId)
        intent.putExtra(IS_OFFICIAL_TEACHER, true)
        (context as AppCompatActivity).startActivity(intent)
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}
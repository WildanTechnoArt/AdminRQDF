package com.wildan.adminrqdf.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.wildan.adminrqdf.GlideApp
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.activity.ProfileUserActivity
import com.wildan.adminrqdf.model.MemberData
import com.wildan.adminrqdf.utils.UtilsConstant.USER_ID
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.member_item.view.*

class FirestoreMemberAdapter(options: FirestoreRecyclerOptions<MemberData>) :
    FirestoreRecyclerAdapter<MemberData, FirestoreMemberAdapter.ViewHolder>(options) {

    companion object{
        val memberIdList = arrayListOf<String>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.member_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: MemberData) {

        val context = holder.itemView.context
        val userId = item.userId.toString()

        memberIdList.add(snapshots.getSnapshot(position).id)

        holder.apply {
            containerView.tv_username.text = item.username.toString()
            containerView.tv_status.text = item.status.toString()

            val db = FirebaseFirestore.getInstance()
            db.collection("photos")
                .document(userId)
                .get()
                .addOnSuccessListener {
                    val photoUrl = it.getString("photoUrl").toString()
                    GlideApp.with(context.applicationContext)
                        .load(photoUrl)
                        .placeholder(R.drawable.profile_placeholder)
                        .into(containerView.img_profile)
                }

            containerView.setOnClickListener {
                val intent = Intent(context, ProfileUserActivity::class.java)
                intent.putExtra(USER_ID, userId)
                context.startActivity(intent)
            }
        }
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}
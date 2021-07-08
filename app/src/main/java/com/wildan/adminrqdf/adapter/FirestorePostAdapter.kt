package com.wildan.adminrqdf.adapter

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.wildan.adminrqdf.GlideApp
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.model.PostData
import com.wildan.adminrqdf.utils.UtilsConstant.TYPE_POST_ASSIGNMENT
import kotlinx.android.synthetic.main.assignment_item.view.*
import kotlinx.android.synthetic.main.post_item.view.*

class FirestorePostAdapter(private val options: FirestoreRecyclerOptions<PostData>) :
    FirestoreRecyclerAdapter<PostData, RecyclerView.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder: RecyclerView.ViewHolder
        val view: View

        if (viewType == TYPE_POST_ASSIGNMENT) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.assignment_item, parent, false)
            viewHolder = PostAssignmentViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.post_item, parent, false)
            viewHolder = PostContentViewHolder(view)
        }

        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        return if (options.snapshots[position].postType == TYPE_POST_ASSIGNMENT) {
            0
        } else {
            1
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, item: PostData) {

        val context = holder.itemView.context
        val post = getItem(position)
        val view = holder.itemView
        val getLinkUrl = item.fileUrl.toString()

        if (post.postType == TYPE_POST_ASSIGNMENT) {
            GlideApp.with(context.applicationContext)
                .load(item.urlPhoto.toString())
                .placeholder(R.drawable.profile_placeholder)
                .into(view.img_assig_profile)

            view.tv_assig_username.text = item.username.toString()
            view.tv_assig_id.text = "NIP: ${item.nomorInduk.toString()}"
            view.tv_assignment_desc.text = item.postContent.toString()

            view.btn_view_assignment.setOnClickListener {
                try {
                    val pdfUrl = Uri.parse(getLinkUrl)
                    val intent = Intent(Intent.ACTION_VIEW, pdfUrl)
                    context.startActivity(intent)
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(
                        context,
                        "Tidak ada aplikasi yang dapat menangani permintaan ini. Silakan instal browser web",
                        Toast.LENGTH_SHORT
                    ).show()
                    ex.printStackTrace()
                }
            }
        } else {
            GlideApp.with(context.applicationContext)
                .load(item.urlPhoto.toString())
                .placeholder(R.drawable.profile_placeholder)
                .into(view.img_profile)

            view.tv_username.text = item.username.toString()
            view.tv_nomor_induk.text = "NIP: ${item.nomorInduk.toString()}"
            view.tv_post_caption.text = item.postContent.toString()
        }
    }

    inner class PostContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class PostAssignmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
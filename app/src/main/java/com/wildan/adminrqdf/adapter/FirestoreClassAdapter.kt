package com.wildan.adminrqdf.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.activity.DetailClassActivity
import com.wildan.adminrqdf.model.ClassModel
import com.wildan.adminrqdf.utils.UtilsConstant.CLASS_ID
import com.wildan.adminrqdf.utils.UtilsConstant.SEND_ID
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.class_item.view.*

class FirestoreClassAdapter(options: FirestoreRecyclerOptions<ClassModel>) :
    FirestoreRecyclerAdapter<ClassModel, FirestoreClassAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.class_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: ClassModel) {

        val context = holder.itemView.context
        val getClassKey = snapshots.getSnapshot(position).id
        val teacherUID = item.teacherUid.toString()

        holder.apply {
            containerView.tv_username.text = item.teacherName.toString()
            containerView.tv_lesson.text = item.lesson.toString()
            containerView.tv_time.text = item.datetime.toString()
            containerView.tv_level.text = item.level.toString()
            containerView.setOnClickListener {
                val intent = Intent(context, DetailClassActivity::class.java)
                intent.putExtra(CLASS_ID, getClassKey)
                intent.putExtra(SEND_ID, teacherUID)
                (context as AppCompatActivity).startActivity(intent)
            }
        }
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}
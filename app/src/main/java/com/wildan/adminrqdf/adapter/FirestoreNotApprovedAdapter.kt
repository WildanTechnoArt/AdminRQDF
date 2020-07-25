package com.wildan.adminrqdf.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import com.wildan.adminrqdf.GlideApp
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.activity.TeacherProfileActivity
import com.wildan.adminrqdf.model.TeacherModel
import com.wildan.adminrqdf.utils.UtilsConstant.GET_PROFILE
import com.wildan.adminrqdf.utils.UtilsConstant.IS_OFFICIAL_TEACHER
import com.wildan.adminrqdf.view.ApprovedView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.input_dialog.view.*
import kotlinx.android.synthetic.main.not_approved_item.view.*
import kotlinx.android.synthetic.main.not_approved_item.view.btn_accept

class FirestoreNotApprovedAdapter(
    options: FirestoreRecyclerOptions<TeacherModel>,
    private val view: ApprovedView.Presenter
) :
    FirestoreRecyclerAdapter<TeacherModel, FirestoreNotApprovedAdapter.ViewHolder>(options) {

    private var name: String? = null
    private var email: String? = null
    private var phone: String? = null
    private var gender: String? = null
    private var address: String? = null

    private var getUserId: String? = null
    private var context: Context? = null
    private lateinit var data: HashMap<String, String?>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.not_approved_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: TeacherModel) {
        getUserId = snapshots.getSnapshot(position).id
        context = holder.containerView.context

        name = item.username.toString()
        email = item.email.toString()
        phone = item.phone.toString()
        gender = item.gender.toString()
        address = item.address.toString()

        holder.apply {
            val db = FirebaseFirestore.getInstance()
            db.collection("photos")
                .document(getUserId.toString())
                .addSnapshotListener { snapshot, _ ->
                    val getPhoto = snapshot?.getString("photoUrl").toString()
                    GlideApp.with(containerView.context)
                        .load(getPhoto)
                        .placeholder(R.drawable.profile_placeholder)
                        .into(containerView.img_profile)
                }

            containerView.tv_teacher_name.text = name

            containerView.tv_email.text =
                String.format(context?.getString(R.string.show_email).toString(), email)

            containerView.btn_accept.setOnClickListener {
                showDialogInput()
            }

            containerView.card_teacher.setOnClickListener {
                toProfileActivity(position)
            }

            containerView.btn_profile.setOnClickListener {
                toProfileActivity(position)
            }
        }
    }

    private fun toProfileActivity(position: Int) {
        getUserId = snapshots.getSnapshot(position).id
        val intent = Intent(context, TeacherProfileActivity::class.java)
        intent.putExtra(GET_PROFILE, getUserId)
        intent.putExtra(IS_OFFICIAL_TEACHER, false)
        (context as AppCompatActivity).startActivity(intent)
    }

    @SuppressLint("InflateParams")
    private fun showDialogInput() {
        var alertDialog: AlertDialog? = null

        val builder = context?.let { MaterialAlertDialogBuilder(it) }
        val dialogView = (context as AppCompatActivity).layoutInflater.inflate(
            R.layout.input_dialog,
            null
        )

        dialogView.btn_accept.setOnClickListener {
            val getRegisterNumber = dialogView.input_number.text.toString()
            if (getRegisterNumber.isEmpty()) {
                Toast.makeText(context, "Nomor Induk Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else {
                data = hashMapOf(
                    "registrationNumber" to getRegisterNumber,
                    "username" to name,
                    "email" to email,
                    "phone" to phone,
                    "gender" to gender,
                    "address" to address
                )

                view.postData(getUserId, data)

                alertDialog?.dismiss()
            }
        }

        dialogView.btn_cancel.setOnClickListener {
            alertDialog?.dismiss()
        }

        builder?.setView(dialogView)
        builder?.setCancelable(false)
        builder?.setTitle("Masukan Nomor Induk")

        alertDialog = builder?.create()
        alertDialog?.show()
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}
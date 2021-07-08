package com.wildan.adminrqdf.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.wildan.adminrqdf.GlideApp
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.utils.UtilsConstant.GET_PROFILE
import com.wildan.adminrqdf.utils.UtilsConstant.IS_OFFICIAL_TEACHER
import kotlinx.android.synthetic.main.activity_teacher_profile.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class TeacherProfileActivity : AppCompatActivity() {

    private var isOfficialTeacher: Boolean? = null
    private var mUserId: String? = null
    private var mPhotoUrl: String? = null
    private var mName: String? = null
    private var mRegistrationNumber: String? = null
    private var mEmail: String? = null
    private var mPhone: String? = null
    private var mGender: String? = null
    private var mAddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_profile)
        init()
        getTeacherProfile()
        showPhotoProfile()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun init() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "Profil Guru"
        }

        isOfficialTeacher = intent?.getBooleanExtra(IS_OFFICIAL_TEACHER, false)
        mUserId = intent?.getStringExtra(GET_PROFILE).toString()
        swipe_refresh.setOnRefreshListener {
            getTeacherProfile()
            showPhotoProfile()
        }
    }

    private fun getRegistrantTeacher() {
        swipe_refresh.isRefreshing = true

        val db = FirebaseFirestore.getInstance()
        db.collection("darulfalah")
            .document("teacher")
            .collection("newRegistrants")
            .document(mUserId.toString())
            .get()
            .addOnSuccessListener {
                mRegistrationNumber = it.getString("registrationNumber")
                mName = it.getString("username").toString()
                mEmail = it.getString("email").toString()
                mGender = it.getString("gender").toString()
                mPhone = it.getString("phone").toString()
                mAddress = it.getString("address").toString()

                if (mRegistrationNumber != null) {
                    tv_registration_number.visibility = View.VISIBLE
                    tv_registration_number.text = mRegistrationNumber.toString()
                }

                tv_username.text = mName
                tv_email.text = mEmail
                tv_phone_number.text = mPhone
                tv_gender.text = mGender
                tv_address.text = mAddress
            }.addOnFailureListener {
                swipe_refresh.isRefreshing = false
                Toast.makeText(this, getString(R.string.request_error), Toast.LENGTH_SHORT).show()
            }
    }

    private fun getOfficialTeacher() {
        swipe_refresh.isRefreshing = true

        val db = FirebaseFirestore.getInstance()
        db.collection("darulfalah")
            .document("teacher")
            .collection("teacherList")
            .document(mUserId.toString())
            .get()
            .addOnSuccessListener {
                mRegistrationNumber = it.getString("registrationNumber")
                mName = it.getString("username").toString()
                mEmail = it.getString("email").toString()
                mGender = it.getString("gender").toString()
                mPhone = it.getString("phone").toString()
                mAddress = it.getString("address").toString()

                if (mRegistrationNumber != null) {
                    tv_registration_number.visibility = View.VISIBLE
                    tv_registration_number.text = mRegistrationNumber.toString()
                }

                tv_username.text = mName
                tv_email.text = mEmail
                tv_phone_number.text = mPhone
                tv_gender.text = mGender
                tv_address.text = mAddress
            }.addOnFailureListener {
                swipe_refresh.isRefreshing = false
                Toast.makeText(this, getString(R.string.request_error), Toast.LENGTH_SHORT).show()
            }
    }

    private fun showPhotoProfile() {
        val db = FirebaseFirestore.getInstance()
        db.collection("photos")
            .document(mUserId.toString())
            .get()
            .addOnSuccessListener {
                swipe_refresh.isRefreshing = false

                mPhotoUrl = it.getString("photoUrl").toString()

                GlideApp.with(this)
                    .load(mPhotoUrl)
                    .placeholder(R.drawable.profile_placeholder)
                    .into(img_profile)

            }.addOnFailureListener {
                swipe_refresh.isRefreshing = false
                Toast.makeText(this, getString(R.string.request_error), Toast.LENGTH_SHORT).show()
            }
    }

    private fun getTeacherProfile() {
        if (isOfficialTeacher == true) {
            getOfficialTeacher()
        } else {
            getRegistrantTeacher()
        }
    }
}
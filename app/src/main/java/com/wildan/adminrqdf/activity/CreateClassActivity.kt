package com.wildan.adminrqdf.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.wildan.adminrqdf.GlideApp
import com.wildan.adminrqdf.R
import com.wildan.adminrqdf.database.SharedPrefManager
import com.wildan.adminrqdf.model.ClassModel
import com.wildan.adminrqdf.utils.UtilsConstant
import com.wildan.adminrqdf.utils.Validation
import kotlinx.android.synthetic.main.activity_create_class.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class CreateClassActivity : AppCompatActivity() {

    private var mLesson: String? = null
    private var mLevel: String? = null
    private var mDate: String? = null
    private var mStarttime: String? = null
    private var mEndttime: String? = null
    private var mTeacherId: String? = null

    private var mTeacherPhoto: String? = null
    private var mTeacherName: String? = null
    private var mRegistrationNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_class)
        prepate()
        listOnClickListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.create_class, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.create_new_class -> {
                if (mTeacherId == "null") {
                    Toast.makeText(this, "Guru Masih Kosong", Toast.LENGTH_SHORT).show()
                } else {
                    createClass()
                }
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        mTeacherId = SharedPrefManager.getInstance(this).getTeacherUID.toString()
        if (mTeacherId != "null") {
            swipe_refresh?.isRefreshing = true
            tv_add_teacher?.visibility = View.GONE
            tv_teacher_name?.visibility = View.VISIBLE
            tv_nomor_induk?.visibility = View.VISIBLE
            img_profile?.visibility = View.VISIBLE
            btn_change?.visibility = View.VISIBLE
            btn_profile?.visibility = View.VISIBLE
            card_teacher?.isEnabled = false
            getTeacherProfile()
            getTeacherPhoto()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SharedPrefManager.getInstance(this).deleteTeacher()
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPrefManager.getInstance(this).deleteTeacher()
    }

    private fun prepate() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "Buat Kelas"
        }

        // Days
        val days =
            listOf("Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu", "Setiap Hari")
        val daysAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, days)
        (add_date as? AutoCompleteTextView)?.setAdapter(daysAdapter)

        // Levels
        val levels =
            listOf("1a", "1b", "2a", "2b", "3a", "3b", "4a", "4b", "5a", "5b", "Kelas Poindok")
        val levelsAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, levels)
        (input_levels as? AutoCompleteTextView)?.setAdapter(levelsAdapter)

        // Levels
        val lessons = listOf(
            "Tahfidz Qur'an",
            "Manhaji 1",
            "Manhaji 2",
            "Manhaji 3",
            "Manhaji 4",
            "Miftah 1",
            "Miftah 2",
            "Miftah 3",
            "Miftah 4",
            "Jurumiyah",
            "Tuhfatul Atfal",
            "Matan Jazari",
            "Safinatunnaja",
            "Doa"
        )
        val lessonsAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, lessons)
        (add_lesson as? AutoCompleteTextView)?.setAdapter(lessonsAdapter)

        // Times
        val times = listOf(
            "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"
        )
        val timesAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, times)
        (add_starttime as? AutoCompleteTextView)?.setAdapter(timesAdapter)
        (add_endtime as? AutoCompleteTextView)?.setAdapter(timesAdapter)
    }

    private fun listOnClickListener() {
        card_teacher.setOnClickListener {
            startActivity(Intent(this, TeacherListActivity::class.java))
        }

        btn_profile.setOnClickListener {
            val intent = Intent(this, TeacherProfileActivity::class.java)
            intent.putExtra(UtilsConstant.GET_PROFILE, mTeacherId)
            intent.putExtra(UtilsConstant.IS_OFFICIAL_TEACHER, true)
            startActivity(intent)
        }

        btn_change.setOnClickListener {
            startActivity(Intent(this, TeacherListActivity::class.java))
        }
    }

    private fun getTeacherProfile() {
        val db = FirebaseFirestore.getInstance()
        db.collection("darulfalah")
            .document("teacher")
            .collection("teacherList")
            .document(mTeacherId.toString())
            .get()
            .addOnSuccessListener {
                mRegistrationNumber = it?.getString("registrationNumber").toString()
                mTeacherName = it?.getString("username").toString()

                tv_teacher_name?.text = mTeacherName
                tv_nomor_induk?.text = mRegistrationNumber
            }.addOnFailureListener {
                swipe_refresh?.isRefreshing = false
                Toast.makeText(this, getString(R.string.request_error), Toast.LENGTH_SHORT).show()
            }
    }

    private fun getTeacherPhoto() {
        val db = FirebaseFirestore.getInstance()
        db.collection("photos")
            .document(mTeacherId.toString())
            .get()
            .addOnSuccessListener {
                mTeacherPhoto = it?.getString("photoUrl").toString()
                GlideApp.with(this)
                    .load(mTeacherPhoto)
                    .placeholder(R.drawable.profile_placeholder)
                    .into(img_profile)

                swipe_refresh?.isRefreshing = false
            }.addOnFailureListener {
                swipe_refresh?.isRefreshing = false
                Toast.makeText(this, getString(R.string.request_error), Toast.LENGTH_SHORT).show()
            }
    }

    private fun createClass() {

        mLesson = add_lesson?.text.toString()
        mLevel = input_levels.text.toString()
        mDate = add_date?.text.toString()
        mStarttime = add_starttime?.text.toString()
        mEndttime = add_endtime?.text.toString()

        if (Validation.validateFields(mLesson) || Validation.validateFields(mDate) ||
            Validation.validateFields(mLevel) || Validation.validateFields(mStarttime)
            || Validation.validateFields(mEndttime)
        ) {
            Toast.makeText(this, "Ada Data Yang Masih Kosong", Toast.LENGTH_SHORT).show()
        } else {
            swipe_refresh?.isRefreshing = true

            val data = ClassModel()
            data.registrationNumber = mRegistrationNumber
            data.teacherName = mTeacherName
            data.teacherUid = mTeacherId
            data.lesson = mLesson
            data.level = mLevel
            data.date = mDate
            data.startTime = mStarttime
            data.endTime = mEndttime
            data.datetime = "$mDate, $mStarttime - $mEndttime"

            val db = FirebaseFirestore.getInstance()
            db.collection("classList")
                .document()
                .set(data)
                .addOnSuccessListener {
                    SharedPrefManager.getInstance(this).deleteTeacher()
                    swipe_refresh?.isRefreshing = false
                    Toast.makeText(this, "Kelas Berhasil Dibuat", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                    swipe_refresh?.isRefreshing = false
                }
        }
    }
}

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
import com.wildan.adminrqdf.adapter.FirestoreDiscussionAdapter
import com.wildan.adminrqdf.model.ForumMessage
import com.wildan.adminrqdf.utils.UtilsConstant.CLASS_ID
import kotlinx.android.synthetic.main.fragment_chat_group.*

class ChatGroupFragment : Fragment() {

    private var adapter: FirestoreDiscussionAdapter? = null

    private lateinit var codeClass: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepare()
        getChatMessage()
    }

    private fun getChatMessage() {
        val query = FirebaseFirestore.getInstance()
            .collection("classList")
            .document(codeClass)
            .collection("chatRoom")
            .orderBy("date")

        val options = FirestoreRecyclerOptions.Builder<ForumMessage>()
            .setQuery(query, ForumMessage::class.java)
            .setLifecycleOwner(this)
            .build()

        rv_chat_list.layoutManager = LinearLayoutManager(context)
        rv_chat_list.setHasFixedSize(true)

        adapter = FirestoreDiscussionAdapter(options)
        rv_chat_list.adapter = adapter
    }

    private fun prepare() {
        swipe_refresh?.isEnabled = false
        codeClass = (context as AppCompatActivity).intent?.getStringExtra(CLASS_ID).toString()
    }
}
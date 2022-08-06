package com.ebm.chatmessagingapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ebm.chatmessagingapp.adapters.MessageAdapter
import com.ebm.chatmessagingapp.R
import com.ebm.chatmessagingapp.entity.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var mDbRef: DatabaseReference

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

//      Action Bar - Batu 41
        val actionBar = supportActionBar
        actionBar!!.setDisplayShowHomeEnabled(true)
        actionBar.title = Html.fromHtml("<font color='#ffffff'>"+ name +"</font>");
        actionBar.setIcon(R.drawable.icon_doodle)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sentButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        chatRecyclerView.adapter = messageAdapter

        // logic for adding data for recyclerView
        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children) {

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                    messageAdapter.notifyDataSetChanged()
                    chatRecyclerView.smoothScrollToPosition(messageList.size); // scroll to end-of-page

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        // adding to message to DB
        sendButton.setOnClickListener {

            val message = messageBox.text.toString()
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())


            if(message != "") {
                val messageObject = Message(message, senderUid, currentDate)

                mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject)
                mDbRef.child("chats").child(receiverRoom!!).child("messages").push()
                    .setValue(messageObject)

                messageBox.setText("")
            }
        }

    }
}
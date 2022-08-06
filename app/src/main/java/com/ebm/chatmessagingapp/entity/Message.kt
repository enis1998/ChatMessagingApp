package com.ebm.chatmessagingapp.entity

class Message {

    var message: String? = null
    var senderId: String? = null
    var z_messageDate: String? = null

    constructor(){}

    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderId = senderId
    }

    constructor(message: String?, senderId: String?, z_messageDate: String?) {
        this.message = message
        this.senderId = senderId
        this.z_messageDate = z_messageDate
    }

}
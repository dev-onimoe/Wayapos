package com.wayapaychat.wayapos.models

import com.google.gson.internal.LinkedTreeMap

class Transactions(var amount : Long? = null, var description : String? = null, var date : String? = null, var ref : String? = null ) {


}

class AgencyTransactions(payload : LinkedTreeMap<String, Any>) {
    var tranAmount : Number? = payload.get("tranAmount") as Number?
    var tranType : String? = payload.get("tranType") as String?
    var tranDate : String? = payload.get("tranDate") as String?
    var paymentReference : String? = payload.get("paymentReference") as String?
    var id : Number? = payload.get("id") as Number?
    var acct : String? = payload.get("acctNum") as String?
    var del_flg : Boolean? = payload.get("del_flg") as Boolean?
    var posted_flg : Boolean? = payload.get("posted_flg") as Boolean?
    var tranId : String? = payload.get("tranId") as String?
    var partTranType : String? = payload.get("partTranType") as String?
    var tranNarrate : String? = payload.get("tranNarrate") as String?
    var tranCrncyCode : String? = payload.get("tranCrncyCode") as String?
    var tranGL : String? = payload.get("tranGL") as String?
    var tranPart : Number? = payload.get("tranPart") as Number?
    var relatedTransId : String? = payload.get("relatedTransId") as String?
    var createdAt : String? = payload.get("createdAt") as String?
    var updatedAt : String? = payload.get("updatedAt") as String?
    var tranCategory : String? = payload.get("tranCategory") as String?
    var createdBy : String? = payload.get("createdBy") as String?
    var createdEmail : String? = payload.get("createdEmail") as String?
    var senderName : String? = payload.get("senderName") as String?
    var receiverName : String? = payload.get("receiverName") as String?
    var transChannel : String? = payload.get("transChannel") as String?
    var channel_flg : Boolean = payload.get("channel_flg") as Boolean
}



enum class AgencyTransferType {
    TRANSFER,
    COMMISSION,
    WITHDRAW
}
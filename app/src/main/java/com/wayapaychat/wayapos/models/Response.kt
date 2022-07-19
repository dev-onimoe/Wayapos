package com.wayapaychat.wayapos.models

import com.google.gson.internal.LinkedTreeMap

open class Response {

    class Success() : Response() {

        lateinit var body : LinkedTreeMap<String, Any>

        constructor(body : LinkedTreeMap<String, Any>) : this() {
            this.body = body
        }
    }

    class Failure() : Response() {

        lateinit var exception : Exception

        constructor(payload : Exception) : this() {
            this.exception = payload
        }

    }
}

class TerminalTransactionBody(payload: LinkedTreeMap<String, Any>) {

    var respBody = payload.get("respBody")
    var respCode = payload.get("respCode")
    var respDesc = payload.get("respDesc")
}

class ResponseBody2(payload: LinkedTreeMap<String, Any>) {

    var respBody = payload.get("respBody")
    var respCode = payload.get("respCode")
    var respDesc = payload.get("respDesc")
}

class ResponseBody(payload: LinkedTreeMap<String, Any>) {

    var status = payload.get("status")
    var message = payload.get("message")
    var data = payload.get("data")

}

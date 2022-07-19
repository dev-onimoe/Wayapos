package com.wayapaychat.wayapos.models

import com.google.gson.internal.LinkedTreeMap

class WalletAccount(var payload : LinkedTreeMap<String, Any>) {

    var accountNo : String = payload.get("accountNo") as String
    var accountName : String = payload.get("acct_name") as String
    var clr_bal_amt : Double = payload.get("clr_bal_amt") as Double
}
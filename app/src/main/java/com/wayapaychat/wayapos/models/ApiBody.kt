package com.wayapaychat.wayapos.models

class ApiBody(var from: String, var pageNo : Int, var pageSize : Int, var params : Params, var to : String) {

}

class Params(var additionalProp1 : HashMap<String, Object>,
             var additionalProp2 : HashMap<String, Object>,
             var additionalProp3 : HashMap<String, Object>) {

}
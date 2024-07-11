package com.androidrider.quizmint.Model

class UserModel {
    var name = ""
    var email = ""
    var password = ""
    var profileImageUrl = ""

    constructor()

    constructor(name: String, email: String, password: String, profileImageUrl: String) {
        this.name = name
        this.email = email
        this.password = password
        this.profileImageUrl = profileImageUrl
    }
}







//package com.androidrider.quizmint.Model
//
//class UserModel{
//    var name=""
//    var email=""
//    var password=""
//
//    constructor()
//    constructor(name: String, email: String, password: String) {
//        this.name = name
//        this.email = email
//        this.password = password
//    }
//
//}
//
//

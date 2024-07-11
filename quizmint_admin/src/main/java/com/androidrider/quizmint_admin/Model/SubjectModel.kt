package com.androidrider.quizmint_admin.Model

data class SubjectModel(

    var subject : String? = "",
    var img: String? = ""
)

data class Question(
    val questionText: String = "",
    val options: List<String> = emptyList(),
    val correctOptionIndex: Int = -1
)
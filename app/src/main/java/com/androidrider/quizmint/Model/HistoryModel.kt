package com.androidrider.quizmint.Model


class HistoryModel{
    var coin:String=""
    var timeAndDate:String=""
    var isWithdrawal:Boolean=false

    constructor()
    constructor(coin: String, timeAndDate: String, isWithdrawal: Boolean) {
        this.coin = coin
        this.timeAndDate = timeAndDate
        this.isWithdrawal = isWithdrawal
    }

}


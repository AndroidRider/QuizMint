package com.androidrider.quizmint.Model


class HistoryModel{
    var coin:String=""
    var amount: String=""
    var timeAndDate:String=""
    var isWithdrawal:Boolean=false

    constructor()

    constructor(coin: String, amount: String, timeAndDate: String, isWithdrawal: Boolean) {
        this.coin = coin
        this.amount = amount
        this.timeAndDate = timeAndDate
        this.isWithdrawal = isWithdrawal
    }


}

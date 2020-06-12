package com.example.pokemon

import android.location.Location

class Pokemon{
        var name:String?=null
        var des:String?=null
        var image:Int?=null
        var power:Double?=null
        var location:Location?=null
        var isCatch:Boolean?=false

    constructor(image:Int, name:String, des:String,
                power:Double,
                log :Double,lat:Double
                ){
        this.image=image
        this.name=name
        this.des=des
        this.power=power
        this.location=Location(name)
        this.location!!.longitude=log
        this.location!!.latitude=lat
    }

    }
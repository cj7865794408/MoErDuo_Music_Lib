package com.cyl.musicapi.bean

import com.google.gson.annotations.SerializedName

data class LyricJsonBean(@SerializedName("time")
                         val time: Long,
                         @SerializedName("info")
                         val info: String
)

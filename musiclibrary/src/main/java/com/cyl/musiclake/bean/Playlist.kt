package com.cyl.musiclake.bean

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import org.litepal.crud.LitePalSupport

@SuppressLint("ParcelCreator")
/**
 * 功能：本地歌单
 * 作者：yonglong on 2016/9/13 21:59
 * 邮箱：643872807@qq.com
 * 版本：2.5
 */
class Playlist() : LitePalSupport(), Parcelable {


    var id: Long = 0
    //歌单id
    var pid: String? = null
    //歌单名
    var name: String? = null
    //歌曲数量
    var count: Long = 0
    //更新日期
    var updateDate: Long = 0
    //创建日期
    var date: Long = 0
    //描述
    var des: String? = null
    //排列顺序
    var order: String? = null
    //封面
    var coverUrl: String? = null
    //类型 0：本地歌单 1：在线同步歌单 2：百度音乐电台 3:网易云歌单 4:百度排行榜
    var type: Int = 0
    var playCount: Long = 0


    //歌曲集合
    var musicList = mutableListOf<Music>()

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        pid = parcel.readString()
        name = parcel.readString()
        count = parcel.readLong()
        updateDate = parcel.readLong()
        date = parcel.readLong()
        des = parcel.readString()
        order = parcel.readString()
        coverUrl = parcel.readString()
        type = parcel.readInt()
    }


    constructor(pid: String?, name: String?) : this() {
        this.pid = pid
        this.name = name
    }

    override fun toString(): String {
        return "Playlist(id=$id, name=$name, count=$count, updateDate=$updateDate, date=$date, des=$des, order=$order, coverUrl=$coverUrl, type=$type, musicList=$musicList)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(pid)
        parcel.writeString(name)
        parcel.writeLong(count)
        parcel.writeLong(updateDate)
        parcel.writeLong(date)
        parcel.writeString(des)
        parcel.writeString(order)
        parcel.writeString(coverUrl)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Playlist> {
        const val PT_LOCAL = 0
        const val PT_MY = 1
        const val PT_BAIDU = 2
        const val PT_NETEASE = 3
        const val PT_XIAOJIA= 4
        override fun createFromParcel(parcel: Parcel): Playlist {
            return Playlist(parcel)
        }

        override fun newArray(size: Int): Array<Playlist?> {
            return arrayOfNulls(size)
        }
    }


}

class MusicToPlaylist : LitePalSupport() {
    var id: Long = 0
    var pid: String? = null
    var mid: String? = null
    var count: Long = 0
    var updateDate: Long = 0
    var createDate: Long = 0
}
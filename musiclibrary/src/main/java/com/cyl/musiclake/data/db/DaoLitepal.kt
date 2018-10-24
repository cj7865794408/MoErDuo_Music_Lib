package com.cyl.musiclake.data.db

import com.cyl.musiclake.MusicApp.count
import com.cyl.musiclake.bean.*
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.utils.LogUtil
import org.litepal.LitePal

/**
 * 数据库操作类
 * Created by master on 2018/4/26.
 */

object DaoLitepal {

/*
 **********************************
 * 播放历史操作
 **********************************
 */
    /**
     * 获取搜索历史
     */
    fun getAllSearchInfo(title: String? = null): MutableList<SearchHistoryBean> {
        return if (title == null) {
            LitePal.findAll(SearchHistoryBean::class.java)
        } else {
            LitePal.where("title like ?", "%$title%").find(SearchHistoryBean::class.java)
        }
    }

    /**
     * 增加搜索
     */
    fun addSearchInfo(info: String) {
        val id = System.currentTimeMillis()
        val queryInfo = SearchHistoryBean(id, info)
        queryInfo.saveOrUpdate("title = ?", info)
    }


    /**
     * 删除搜索历史
     */
    fun deleteSearchInfo(info: String) {
        LitePal.deleteAll(SearchHistoryBean::class.java, "title = ? ", info)
    }

    /**
     * 删除所有搜索历史
     */
    fun clearAllSearch() {
        LitePal.deleteAll(SearchHistoryBean::class.java)
    }

    /*
     **********************************
     * 播放歌单操作
     **********************************
     */
    fun saveOrUpdateMusic(music: Music, isAsync: Boolean = false) {
        var isSuccues: Boolean
        if (isAsync) {
            music.saveOrUpdateAsync("mid = ?", music.mid)
        } else {
            isSuccues = music.saveOrUpdate("mid = ?", music.mid)
            LogUtil.d("issuccues:$isSuccues")
        }
    }


    /**
     * 扫描更新本地歌曲信息，如果
     */
    fun saveOrUpdateLocalMusic(music: Music, pid: String, isAsync: Boolean = false): Boolean {
        var isSuccues: Boolean = false
        if (isAsync) {
            music.saveOrUpdateAsync("mid = ? and artistId = ?", music.mid, pid)
            isSuccues = true
            return isSuccues
        } else {
            isSuccues = music.saveOrUpdate("mid = ? and artistId = ?", music.mid, pid)
        }
        return isSuccues
    }

    fun addToPlaylist(music: Music, pid: String): Boolean {
        saveOrUpdateLocalMusic(music, pid)
        return if (count == 0) {
            val count = LitePal.where("mid = ? and pid = ?", music.mid, pid)
                    .count(MusicToPlaylist::class.java)
            val mtp = MusicToPlaylist()
            mtp.mid = music.mid
            mtp.pid = pid
            mtp.count = 1
            mtp.createDate = System.currentTimeMillis()
            mtp.updateDate = System.currentTimeMillis()
            mtp.save()
        } else {
            val mtp = MusicToPlaylist()
            mtp.count++
            mtp.updateDate = System.currentTimeMillis()
            mtp.saveOrUpdate("mid = ? and pid =?", music.mid, pid)

        }
    }

    fun saveOrUpdatePlaylist(playlist: Playlist): Boolean {
        playlist.updateDate = System.currentTimeMillis()
        return playlist.saveOrUpdate("pid = ?", playlist.pid)
    }

    fun deleteMusic(music: Music) {
        LitePal.delete(Music::class.java, music.id)
        LitePal.deleteAll(MusicToPlaylist::class.java, "mid=?", music.mid)
    }

    fun deleteAllMusic(pid: String) {
        LitePal.deleteAll(Music::class.java, "artistId=?",pid)
    }
    fun deletePlaylist(playlist: Playlist) {
        LitePal.delete(Music::class.java, playlist.id)
        LitePal.deleteAll(MusicToPlaylist::class.java, "pid=?", playlist.pid)
    }

    fun clearPlaylist(pid: String) {
        LitePal.deleteAll(MusicToPlaylist::class.java, "pid=?", pid)
    }

    fun getMusicList(pid: String, order: String = ""): MutableList<Music> {
        val musicLists = mutableListOf<Music>()
        when (pid) {
            Constants.PLAYLIST_LOVE_ID -> {
                val data = LitePal.where("isLove = ? ", "1").find(Music::class.java)
                musicLists.addAll(data)
            }
            Constants.PLAYLIST_LOCAL_ID -> {
                val data = LitePal.where("isOnline = ? ", "0").find(Music::class.java)
                musicLists.addAll(data)
            }
            else -> {
                val data = LitePal.where("pid = ?", pid).find(MusicToPlaylist::class.java)
                for (it in data) {
                    val musicList = LitePal.where("mid = ?", it.mid).find(Music::class.java)
                    musicLists.addAll(musicList)
                }
            }
        }
        return musicLists
    }

   fun getNewMusicList(pid: String): MutableList<Music> {
        val musicLists = mutableListOf<Music>()
        val musicList = LitePal.where("artistId = ?", pid).find(Music::class.java)
        musicLists.addAll(musicList)
        return musicLists
    }
//    fun getMusicList(pid: String): MutableList<Music> {
//        val musicLists = mutableListOf<Music>()
//        val data = LitePal.where("pid = ?", pid).find(MusicToPlaylist::class.java)
//        for (it in data) {
//            val musicList = LitePal.where("mid = ?", it.mid).find(Music::class.java)
//            musicLists.addAll(musicList)
//        }
//        return musicLists
//    }

    fun getAllPlaylist(): List<Playlist> {
        return LitePal.where("pid != ? and pid !=?", Constants.PLAYLIST_QUEUE_ID, Constants.PLAYLIST_HISTORY_ID).find(Playlist::class.java)
    }


    fun getPlaylist(pid: String): Playlist {
        return LitePal.where("pid = ?", pid).find(Playlist::class.java).first()
    }

    fun getMusicInfo(mid: String): MutableList<Music>? {
        return LitePal.where("mid =? ", mid).find(Music::class.java)
    }

    fun removeSong(pid: String, mid: String) {
        LitePal.deleteAll(MusicToPlaylist::class.java, "pid=? and mid=?", pid, mid)
    }

    fun searchLocalMusic(info: String): MutableList<Music> {
        return LitePal.where("title LIKE ? or artist LIKE ? or album LIKE ?", "%$info%", "%$info%", "%$info%").find(Music::class.java)
    }

    fun getAllAlbum(): MutableList<Album> {
        return LitePal.findAll(Album::class.java)
    }

    fun getAllArtist(): MutableList<Artist> {
        return LitePal.findAll(Artist::class.java)
    }


    fun updateArtistList(): MutableList<Artist> {
        val sql = "SELECT music.artistid,music.artist,count(music.title) as num FROM music where music.isonline=0 and music.type=\"local\" GROUP BY music.artist"
        val cursor = LitePal.findBySQL(sql)
        val results = mutableListOf<Artist>()
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val artist = MusicCursorWrapper(cursor).artists
                artist.saveOrUpdate("artistId = ?", artist.artistId.toString())
                results.add(artist)
            }
        }
        // 记得关闭游标
        cursor?.close()
        return results
    }


    fun updateAlbumList(): MutableList<Album> {
        val sql = "SELECT music.albumid,music.album,music.artistid,music.artist,count(music.title) as num FROM music WHERE music.isonline=0 and music.type=\"local\" GROUP BY music.album"
        val cursor = LitePal.findBySQL(sql)
        val results = mutableListOf<Album>()
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val album = MusicCursorWrapper(cursor).album
                album.saveOrUpdate("albumId = ?", album.albumId)
                results.add(album)
            }
        }
        // 记得关闭游标
        cursor?.close()
        return results
    }
}

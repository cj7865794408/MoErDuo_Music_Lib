package com.cyl.musiclake.data

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.text.TextUtils
import com.cyl.musiclake.bean.Album
import com.cyl.musiclake.bean.Artist
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.db.DaoLitepal
import com.cyl.musiclake.utils.CoverLoader
import org.litepal.LitePal


object SongLoader {
    /**
     * 获取所有艺术家
     *
     * @param context
     * @return
     */
    fun getAllArtists(): MutableList<Artist> {
        val result = DaoLitepal.getAllArtist()
        if (result.size == 0) {
            return DaoLitepal.updateArtistList()
        }
        return result
    }


    /**
     * 获取艺术家所有歌曲
     *
     * @param context
     * @return
     */
    fun getSongsForArtist(artistName: String?): MutableList<Music> {
        return LitePal.where("isonline =0 and artist like ?", "%$artistName%").find(Music::class.java)
    }

    /**
     * 获取专辑所有歌曲
     *
     * @param context
     * @return
     */
    fun getSongsForAlbum(albumName: String?): MutableList<Music> {
        return LitePal.where("isonline =0 and album like ?", "%$albumName%").find(Music::class.java)
    }

    /**
     * 获取所有专辑
     *
     * @param context
     * @return
     */
    fun getAllAlbums(): MutableList<Album> {
        val result = DaoLitepal.getAllAlbum()
        if (result.size == 0) {
            return DaoLitepal.updateAlbumList()
        }
        return result
    }


    /**
     * Android 扫描获取到的数据
     *
     * @param context
     * @param cursor
     * @return
     */
    private fun getSongsForMedia(context: Context, cursor: Cursor?): MutableList<Music> {
        val results = mutableListOf<Music>()
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val is_music = cursor.getInt(9)
                    val id = cursor.getLong(0)
                    val title = cursor.getString(1)
                    val artist = cursor.getString(2)
                    val album = cursor.getString(3)
                    val duration = cursor.getInt(4)
                    val trackNumber = cursor.getInt(5)
                    val artistId = cursor.getString(6)
                    val albumId = cursor.getString(7)
                    val path = cursor.getString(8)
                    val coverUri = CoverLoader.getCoverUri(context, albumId)
                    val music = Music()
                    music.type = Constants.LOCAL
                    music.isOnline = false
                    music.mid = id.toString()
                    music.album = album
                    music.albumId = albumId
                    music.artist = if (artist == "<unknown>") "未知歌手" else artist
                    music.artistId = artistId
                    music.uri = path
                    coverUri?.let { music.coverUri = it }
                    music.trackNumber = trackNumber
                    music.duration = duration.toLong()
                    music.title = title
                    music.date = System.currentTimeMillis()
                    DaoLitepal.saveOrUpdateMusic(music)
                    results.add(music)
                } while (cursor.moveToNext())
            }
            cursor?.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return results
    }


    /**
     * 获取所有收藏的歌曲
     *
     * @param context
     * @return
     */
    fun getFavoriteSong(): MutableList<Music> {
        return DaoLitepal.getMusicList(Constants.PLAYLIST_LOVE_ID)
    }

    fun getSongsForDB(): MutableList<Music> {
        return DaoLitepal.getMusicList(Constants.PLAYLIST_LOCAL_ID)
    }

    fun getLocalMusic(context: Context, isReload: Boolean = false): MutableList<Music> {
        val data = getSongsForDB()
        if (data.size == 0 || isReload) {
            data.clear()
            val musicLists = getAllLocalSongs(context)
            if (isReload) {
                DaoLitepal.updateAlbumList()
                DaoLitepal.updateArtistList()
            }
            musicLists.forEach {
                data.add(it)
            }
        }
        return data
    }

    fun getMusicInfo(mid: String): Music? {
        val data = DaoLitepal.getMusicInfo(mid)
        if (data != null) {
            if (data.size > 0)
                return data[0]
        }
        return null
    }


    fun updateFavoriteSong(music: Music): Boolean {
        music.isLove = !music.isLove
//        DaoLitepal.saveOrUpdateMusic(music)
        return music.isLove
    }

    /**
     * 本地歌曲
     * 添加歌曲
     */
    private fun insertSongs(musics: List<Music>) {
    }

    /**
     * 本地歌曲
     * 添加歌曲
     */
    fun updateMusic(music: Music) {
        DaoLitepal.saveOrUpdateMusic(music, true)
    }

    /**
     * 删除歌曲
     */
    fun removeSong(music: Music) {
        DaoLitepal.deleteMusic(music)
    }

    fun getAllLocalSongs(context: Context): MutableList<Music> {
        return getSongsForMedia(context, makeSongCursor(context, null, null))
    }

    fun searchSongs(context: Context, searchString: String): MutableList<Music> {
        return getSongsForMedia(context, makeSongCursor(context, "title LIKE ? or artist LIKE ? or album LIKE ? ",
                arrayOf("%$searchString%", "%$searchString%", "%$searchString%")))
    }

    fun getSongListInFolder(context: Context, path: String): MutableList<Music> {
        val whereArgs = arrayOf("$path%")
        return getSongsForMedia(context, makeSongCursor(context, MediaStore.Audio.Media.DATA + " LIKE ?", whereArgs, null))
    }

    fun makeSongCursor(context: Context, selection: String?, paramArrayOfString: Array<String>?): Cursor? {
        val songSortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        return makeSongCursor(context, selection, paramArrayOfString, songSortOrder)
    }

    private fun makeSongCursor(context: Context, selection: String?, paramArrayOfString: Array<String>?, sortOrder: String?): Cursor? {
        var selectionStatement = "duration>60000 AND is_music=1 AND title != ''"

        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = "$selectionStatement AND $selection"
        }
        return context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf("_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id", MediaStore.Audio.Media.DATA, "is_music"),
                selectionStatement, paramArrayOfString, sortOrder)
    }

}

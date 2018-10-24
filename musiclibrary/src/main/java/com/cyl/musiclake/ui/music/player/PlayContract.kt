package com.cyl.musiclake.ui.music.player

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v7.graphics.Palette

import com.cyl.musiclake.base.BaseContract
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.QueryRadioAudioListBean
import com.cyl.musiclake.bean.SpecailRadioBean


interface PlayContract {

    interface View : BaseContract.BaseView {

        fun setPlayingBitmap(albumArt: Bitmap?)

        fun setPlayingBg(albumArt: Drawable?)

        fun setPalette(palette: Palette?)

        fun showLyric(lyric: String?, init: Boolean)

        fun updatePlayStatus(isPlaying: Boolean)

        fun updatePlayMode()

        fun updateProgress(progress: Long, max: Long)

        fun showNowPlaying(music: Music?)

        fun showList(data: SpecailRadioBean?)

        fun showMusicList(data: QueryRadioAudioListBean?)

        fun isCollect(data: SpecailRadioBean)

        fun getDBMusicList(list: List<Music>,p:Int?,name:String?)
    }

    interface Presenter : BaseContract.BasePresenter<View> {

        fun updateNowPlaying(music: Music?)

        fun loadSpeData(parentId: Boolean?)

        fun loadMusicList(aId: String)

        fun loadCollect(audioId: String?, state: Int?)
    }
}

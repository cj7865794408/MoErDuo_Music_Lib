package com.cyl.musiclake.api

import com.cyl.musicapi.playlist.ErrorInfo
import com.cyl.musicapi.playlist.MusicInfo
import com.cyl.musicapi.playlist.PlaylistApiService
import com.cyl.musicapi.playlist.PlaylistInfo
import com.cyl.musiclake.ApiToJson
import com.cyl.musiclake.MusicApp
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.QueryRadioAudioListBean
import com.cyl.musiclake.bean.SpecailRadioBean
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.net.ApiManager
import com.cyl.musiclake.ui.my.user.User
import com.cyl.musiclake.ui.my.user.UserStatus
import com.cyl.musiclake.utils.SPUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

/**
 * Created by master on 2018/4/5.
 */

object PlaylistApiServiceImpl {
    private val TAG = "PlaylistApiServiceImpl"

    private val playlistApiService: PlaylistApiService
        get() = ApiManager.getInstance().create(PlaylistApiService::class.java, Constants.BASE_PLAYER_URL)

    private val token: String
        get() = UserStatus.getUserInfo(MusicApp.getAppContext()).token

    private val specaillistApiService: PlaylistApiService
        get() = ApiManager.getInstance().create(PlaylistApiService::class.java, Constants.NEW_BASE_URL)

    //    private val MEDtoken: String
//        get() = "ad75090d8de540dc985793f7529f6cd3"
//    private val MEDStudentId: String
//        get() = "b16f9aa9a2694afe8a6a7f3a63a5ec23"
//     val MEDtoken: String
//        get() = "c3c1c3e31dd14f499caacb2d2b3aefea"
//     val MEDStudentId: String
//        get() = "bba5be2582ed4796bc00cc791521b9e5"

    /**
     * 获取全部歌单
     */
    fun getPlaylist(): Observable<MutableList<Playlist>> {
        return playlistApiService.getOnlinePlaylist(token)
                .flatMap { it ->
                    val json = it.string()
                    val data = Gson().fromJson<List<Playlist>>(json, object : TypeToken<List<Playlist>>() {
                    }.type)
                    val result = mutableListOf<Playlist>()
                    for (playlistInfo in data) {
                        val playlist = Playlist()
                        playlist.id = playlistInfo.id
                        playlist.name = playlistInfo.name
                        playlist.type = Playlist.PT_MY
                        result.add(playlist)
                    }
                    Observable.create(ObservableOnSubscribe<MutableList<Playlist>> {
                        if (data.isEmpty() && json.contains("msg")) {
                            val msg = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                            it.onError(Throwable(msg.msg))
                        } else {
                            it.onNext(result)
                            it.onComplete()
                        }
                    })
                }
    }

    /**
     * 获取歌单歌曲列表
     */
    fun getMusicList(pid: String): Observable<MutableList<Music>> {
        return playlistApiService.getMusicList(token, pid)
                .flatMap { it ->
                    val json = it.string()

                    val data = Gson().fromJson<MutableList<MusicInfo>>(json, object : TypeToken<MutableList<MusicInfo>>() {
                    }.type)
                    val musicList = mutableListOf<Music>()
                    for (musicInfo in data) {
                        val music = MusicUtils.getMusic(musicInfo)
                        musicList.add(music)
                    }
                    Observable.create(ObservableOnSubscribe<MutableList<Music>> {
                        if (data.isEmpty() && json.contains("msg")) {
                            val msg = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                            it.onError(Throwable(msg.msg))
                        } else {
                            it.onNext(musicList)
                            it.onComplete()
                        }
                    })
                }
    }


    /**
     * 新建歌单
     * 调用接口成功返回{"id": "","name": ""}
     * 调用接口失败返回{"msg":""}
     *
     * @param name
     * @return
     */
    fun createPlaylist(name: String): Observable<Playlist> {
        val newPlaylistInfo = PlaylistInfo(name)
        return playlistApiService.createPlaylist(token, newPlaylistInfo)
                .flatMap { it ->
                    val json = it.string()
                    val data = Gson().fromJson<PlaylistInfo>(json.toString(), PlaylistInfo::class.java)
                    val playlist = Playlist(data?.id, data?.name)
                    Observable.create(ObservableOnSubscribe<Playlist> {
                        if (data == null && json.contains("msg")) {
                            val msg = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                            it.onError(Throwable(msg.msg))
                        } else {
                            it.onNext(playlist)
                            it.onComplete()
                        }
                    })
                }
    }

    /**
     * 删除歌单
     * 调用接口成功返回{}
     * 调用接口失败返回{"msg":""}
     */
    fun deletePlaylist(pid: String): Observable<String> {
        return playlistApiService.deleteMusic(token, pid)
                .flatMap { it ->
                    val json = it.string()
                    val errorInfo = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                    Observable.create(ObservableOnSubscribe<String> {
                        if (errorInfo.msg.isEmpty()) {
                            it.onNext("歌单删除成功")
                            it.onComplete()
                        } else {
                            it.onError(Throwable(errorInfo.msg))
                        }
                    })
                }
    }

    /**
     * 重命名歌单
     * 调用接口成功返回{}
     * 调用接口失败返回{"msg":""}
     */
    fun renamePlaylist(pid: String, name: String): Observable<String> {
        val playlist = PlaylistInfo(name = name)
        return playlistApiService.renameMusic(token, pid, playlist)
                .flatMap { it ->
                    val json = it.string()
                    val errorInfo = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                    Observable.create(ObservableOnSubscribe<String> {
                        if (errorInfo.msg.isEmpty()) {
                            it.onNext("修改成功")
                            it.onComplete()
                        } else {
                            it.onError(Throwable(errorInfo.msg))
                        }
                    })
                }
    }

    /**
     * 收藏歌曲
     * 调用接口成功返回{}
     * 调用接口失败返回{"msg":""}
     */
    fun collectMusic(pid: String, music: Music): Observable<String>? {
        val musicInfo = MusicUtils.getMusicInfo(music)
//        LogUtil.e(Gson().toJson(musicInfo).toString())
        return playlistApiService.collectMusic(token, pid, musicInfo)
                .flatMap { it ->
                    val json = it.string()
                    Observable.create(ObservableOnSubscribe<String> {
                        if (json == "{}") {
                            it.onNext("收藏成功")
                            it.onComplete()
                        } else {
                            try {
                                val errorInfo = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                                it.onNext(errorInfo.msg)
                                it.onComplete()
                            } catch (e: Throwable) {
                                it.onError(Throwable(e.message))
                            }
                        }
                    })
                }
    }

    /**
     * 取消收藏
     * 调用接口成功返回{}
     * 调用接口失败返回{"msg":""}
     */
    fun disCollectMusic(pid: String, music: Music): Observable<String> {
        return playlistApiService.disCollectMusic(token, pid, music.collectId.toString())
                .flatMap { it ->
                    val json = it.string()
                    val errorInfo = Gson().fromJson<ErrorInfo>(json.toString(), ErrorInfo::class.java)
                    Observable.create(ObservableOnSubscribe<String> {
                        if (errorInfo.msg.isEmpty()) {
                            it.onNext("已取消收藏")
                            it.onComplete()
                        } else {
                            it.onError(Throwable(errorInfo.msg))
                        }
                    })
                }
    }


    /**
     * 用户登录
     */
    fun login(token: String, openid: String): Observable<User> {
        return playlistApiService.getUserInfo(token, openid)
                .flatMap { data ->
                    val user = User()
                    user.nick = data.nickname
                    user.name = data.nickname
                    user.avatar = data.avatar
                    user.token = data.token
                    Observable.create(ObservableOnSubscribe<User> {
                        if (!user.token.isEmpty()) {
                            it.onNext(user)
                            it.onComplete()
                        } else {
                            it.onError(Throwable("登录异常"))
                        }
                    })
                }
    }


    /**
     * 网易云排行榜
     */
    fun getNeteaseRank(ids: IntArray, limit: Int): Observable<MutableList<Playlist>> {
        return playlistApiService.getNeteaseRank(ids, limit)
                .flatMap { data ->
                    val list = mutableListOf<Playlist>()
                    data.forEach {
                        val playlist = Playlist()
                        playlist.coverUrl = it.cover
                        playlist.des = it.description
                        playlist.pid = it.id
                        playlist.name = it.name
                        playlist.type = Playlist.PT_NETEASE
                        playlist.playCount = it.playCount
                        playlist.musicList = MusicUtils.getMusicList(it.list)
                        list.add(playlist)
                    }
                    Observable.create(ObservableOnSubscribe<MutableList<Playlist>> {
                        it.onNext(list)
                        it.onComplete()
                    })
                }
    }

    /**
     * 专题列表
     */
    fun QueryRadioList(getMyFavorite: Boolean): Observable<SpecailRadioBean> {
        return specaillistApiService.QueryRadioList(SPUtils.getXiaoJiaToken(), SPUtils.getStudentId(), ApiToJson.getNewParmData("getMyFavorite", getMyFavorite, Constants.QueryRadioList))
                .flatMap { it ->
                    val json = it.string()
                    val info = Gson().fromJson<SpecailRadioBean>(json.toString(), SpecailRadioBean::class.java)
                    Observable.create(ObservableOnSubscribe<SpecailRadioBean> {
                        if (info.isSuccess) {
                            it.onNext(info)
                            it.onComplete()
                        } else {
                            it.onError(Throwable("网络异常"))
                        }
                    })
                }
    }

    /**
     * 收藏音频
     */
    fun StudentAddAudioToFavorite(rid: String?, state: Int?): Observable<SpecailRadioBean> {

        var api: String? = if (state == 0) {
            Constants.StudentAddAudioToFavorite
        } else {
            Constants.StudentDeleteAudioFromFavorite
        }
        return specaillistApiService.StudentAddAudioToFavorite(SPUtils.getXiaoJiaToken(), SPUtils.getStudentId(), ApiToJson.getNewParmData("audioId", rid, api))
                .flatMap { it ->
                    val json = it.string()
                    val info = Gson().fromJson<SpecailRadioBean>(json.toString(), SpecailRadioBean::class.java)
                    Observable.create(ObservableOnSubscribe<SpecailRadioBean> {
                        if (info.isSuccess) {
                            it.onNext(info)
                            it.onComplete()
                        } else {
                            it.onError(Throwable("网络异常"))
                        }
                    })
                }
    }


    /**
     * 音乐列表
     */
    fun QueryRadioAudioList(aId: String): Observable<QueryRadioAudioListBean> {
        return specaillistApiService.QueryRadioAudioList(SPUtils.getXiaoJiaToken(), SPUtils.getStudentId(), ApiToJson.getNewParmData("radioId", aId, Constants.QueryRadioAudioList))
                .flatMap { it ->
                    val json = it.string()
                    val info = Gson().fromJson<QueryRadioAudioListBean>(json.toString(), QueryRadioAudioListBean::class.java)
                    Observable.create(ObservableOnSubscribe<QueryRadioAudioListBean> {
                        if (info.isSuccess) {
                            it.onNext(info)
                            it.onComplete()
                        } else {
                            it.onError(Throwable("网络异常"))
                        }
                    })
                }
    }

}

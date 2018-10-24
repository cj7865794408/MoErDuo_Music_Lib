package com.cyl.musicapi.playlist


import com.cyl.musicapi.TypedInput
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Created by master on 2018/4/5.
 */

interface PlaylistApiService {
    /**
     * 获取歌单
     *
     * @param token 秘钥
     * @return
     */
    @GET("playlist")
    fun getOnlinePlaylist(@Header("accesstoken") token: String): Observable<ResponseBody>

    /**
     * 获取歌单数据
     *
     * @param token 秘钥
     * @param id    歌单id
     * @return
     */
    @GET("playlist/{id}")
    fun getMusicList(@Header("accesstoken") token: String, @Path("id") id: String): Observable<ResponseBody>

    /**
     * 删除歌单
     *
     * @param token 秘钥
     * @param id    歌单id
     * @return
     */
    @DELETE("playlist")
    fun deleteMusic(@Header("accesstoken") token: String, @Query("id") id: String): Observable<ResponseBody>


    /**
     * 重命名歌单
     *
     * @param token    秘钥
     * @param id       歌单id
     * @param playlist 歌单信息
     * @return
     */
    @PUT("playlist/{id}")
    @Headers("Content-Type: application/json")
    fun renameMusic(@Header("accesstoken") token: String, @Path("id") id: String, @Body playlist: PlaylistInfo): Observable<ResponseBody>

    /**
     * 新建歌单
     *
     * @param token
     * @return
     */
    @POST("playlist")
    @Headers("Content-Type: application/json")
    fun createPlaylist(@Header("accesstoken") token: String, @Body playlist: PlaylistInfo): Observable<ResponseBody>

    /**
     * 收藏歌曲
     *
     * @param token
     * @param id        歌单id
     * @param musicInfo 歌曲信息
     * @return
     */
    @POST("playlist/{id}")
    @Headers("Content-Type: application/json")
    fun collectMusic(@Header("accesstoken") token: String, @Path("id") id: String, @Body musicInfo: MusicInfo): Observable<ResponseBody>

    /**
     * 取消收藏歌曲
     *
     * @param token 秘钥
     * @return
     */
    @DELETE("playlist/{id}")
    fun disCollectMusic(@Header("accesstoken") token: String, @Path("id") id: String, @Query("id") songid: String): Observable<ResponseBody>

    /**
     * 获取用户信息
     *
     * @param token
     * @param openid
     * @return
     */
    @GET("auth/qq/android")
    fun getUserInfo(@Query("access_token") token: String,
                    @Query("openid") openid: String): Observable<UserInfo>

    /**
     * 获取网易云排行榜
     *
     * @param token
     * @param openid
     * @return
     */
    @GET("music/netease/rank")
    fun getNeteaseRank(@Query("ids[]") ids: IntArray, @Query("limit") limit: Int): Observable<MutableList<PlaylistInfo>>

    /**
     * 获取磨耳朵专辑列表
     *
     * @param token
     * @return
     */
    @POST("single_api")
    @Headers("Content-Type: application/json")
    fun QueryRadioList(@Header("auth-token") token: String,@Header("studentid") studentid:String, @Body data: TypedInput): Observable<ResponseBody>

    /**
     * 获取音乐列表
     *
     * @param token
     * @return
     */
    @POST("single_api")
    @Headers("Content-Type: application/json")
    fun QueryRadioAudioList(@Header("auth-token") token: String,@Header("studentid") studentid:String, @Body data: TypedInput): Observable<ResponseBody>
    /**
     * 收藏音频
     *
     * @param token
     * @return
     */
    @POST("single_api")
    @Headers("Content-Type: application/json")
    fun StudentAddAudioToFavorite(@Header("auth-token") token: String,@Header("studentid") studentid:String, @Body data: TypedInput): Observable<ResponseBody>

}

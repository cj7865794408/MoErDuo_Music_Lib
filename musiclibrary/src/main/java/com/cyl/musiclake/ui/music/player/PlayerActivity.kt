package com.cyl.musiclake.ui.music.player

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.graphics.Palette
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.cyl.musiclake.R
import com.cyl.musiclake.api.MusicUtils
import com.cyl.musiclake.base.BaseActivity
import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.bean.Playlist
import com.cyl.musiclake.bean.QueryRadioAudioListBean
import com.cyl.musiclake.bean.SpecailRadioBean
import com.cyl.musiclake.common.Constants
import com.cyl.musiclake.data.SongLoader
import com.cyl.musiclake.event.*
import com.cyl.musiclake.player.FloatLyricViewManager
import com.cyl.musiclake.player.PlayManager
import com.cyl.musiclake.ui.OnlinePlaylistUtils
import com.cyl.musiclake.ui.UIUtils
import com.cyl.musiclake.ui.downloadMusic
import com.cyl.musiclake.ui.main.SpecialActivity
import com.cyl.musiclake.ui.music.dialog.MusicLyricDialog
import com.cyl.musiclake.ui.music.local.adapter.MyPagerAdapter
import com.cyl.musiclake.ui.music.playqueue.PlayQueueDialog
import com.cyl.musiclake.utils.*
import com.cyl.musiclake.view.DepthPageTransformer
import com.cyl.musiclake.view.LyricView
import com.cyl.musiclake.view.MultiTouchViewPager
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_player.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : BaseActivity<PlayPresenter>(), PlayContract.View {
    override fun getDBMusicList(mlist: List<Music>, position: Int?, namid: String?) {
        var daoList: List<Music> = mlist
        if (daoList != null)
            musicList = daoList as ArrayList<Music>
        if (musicList != null && musicList.size > 0) {
            var index = SPUtils.getPlayPosition()
            if (index <= 0) {
                index = 0
            }
            if (position!! > 0) {
                index = position
            }
            if (position == 0 && namid != null) {
                index = position
            }
            var name: String? = namid
            if (TextUtils.isEmpty(name)) {
                name = "磨耳朵" + musicList?.get(index)?.id
            }
            loadPlayData(index, name)
        } else {
            mPresenter!!.loadSpeData(true)
        }
    }


    override fun isCollect(data: SpecailRadioBean) {
        hideLoading()
        if (data != null) {
            if (data.isSuccess) {
                playingMusic?.let {
                    it.isLove = SongLoader.updateFavoriteSong(it)
                    if (it.isLove) {
                        ToastUtils.showCenter("收藏成功！")
                    } else {
                        ToastUtils.showCenter("已取消收藏！")
                    }
                }
            } else {
                if (data.message != null && !TextUtils.isEmpty(data.message)) {
                    ToastUtils.show(data.message + "")
                }
            }
        }
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    var aRadios: MutableList<SpecailRadioBean.DataBean>? = java.util.ArrayList()
    @SuppressLint("LongLogTag")
    override fun showList(data: SpecailRadioBean?): Unit =//专辑列表
            if (data != null && data.data != null) {
                hideLoading()
                aRadios = data.data.list
                val iData = SpecailRadioBean.DataBean()
                val radiosBeanList = java.util.ArrayList<SpecailRadioBean.DataBean.RadiosBean>()
                val myAudios = data.data.myAudios
                if (myAudios != null && myAudios.size > 0) {
                    if (aRadios == null)
                        aRadios = java.util.ArrayList()
                    for (i in myAudios.indices) {
                        val myAudioDataBean = myAudios[i]
                        val loadData = SpecailRadioBean.DataBean.RadiosBean()
                        loadData.radioAudioCount = if (myAudioDataBean.radioAudios == null) 0 else myAudioDataBean.radioAudios.size
                        loadData.name = myAudioDataBean.name
                        loadData.img = myAudioDataBean.typeImg
                        loadData.id = "shoucang$i" + loadData.name
                        if (loadData.radioAudioCount > 0) {
                            val mlIstanbul = java.util.ArrayList<Music>()
                            for (j in 0 until myAudioDataBean.radioAudios.size) {
                                val musicData = Music()
                                val raData = myAudioDataBean.radioAudios[j]
                                musicData.isPlay = SPUtils.getPlayPosition() == j && PlayManager.isPlaying() && SPUtils.getPId() != null && !TextUtils.isEmpty(SPUtils.getPId()) && SPUtils.getPId() == musicData.artistId
                                musicData.artist = myAudioDataBean.name
                                musicData.artistId = loadData.id
                                musicData.trackNumber = loadData.radioAudioCount
                                musicData.type = Constants.XIAOJIA
                                musicData.mid = raData.id
                                musicData.title = raData.name
                                musicData.uri = raData.audioUrl
                                musicData.coverUri = myAudioDataBean.typeImg
                                musicData.date = raData.createTime
                                musicData.year = raData.playTime
                                musicData.isLove = true
                                val list = raData.lyricJson.list
                                if (list != null) {
                                    for (k in list.indices) {
                                        val da = list[k]
                                        da.lrcTime = UIUtils.hmTime(da.time)
                                    }
                                    musicData.lrcList = list
                                }
                                val lyric = StringBuffer()
                                if (musicData.lrcList != null) {
                                    for (p in 0 until musicData.lrcList!!.size) {
                                        val it = musicData.lrcList!![p]
                                        lyric.append("[" + it.lrcTime + ", " + it.info.replace("\r", "") + "]")
                                        lyric.append("\n")
                                    }
                                    musicData.lyric = lyric.toString()
                                }
                                mlIstanbul.add(musicData)
                            }
                            loadData.musicList = mlIstanbul
                            radiosBeanList.add(loadData)
                        }
                    }
                    iData.name = "我的收藏"
                    if (radiosBeanList.size > 0) {
                        iData.radios = radiosBeanList
                        aRadios!!.add(0, iData)
                    }
                }
                var radiosData: SpecailRadioBean.DataBean.RadiosBean? = null
                if (aRadios != null && aRadios?.size!! > 0) {
                    var sId = SPUtils.getPId()
                    if (sId != null && !TextUtils.isEmpty(sId)) {
                        for (i in aRadios!!.indices) {
                            var data: SpecailRadioBean.DataBean = aRadios!![i]
                            data?.let {
                                if (data.radios != null && data.radios.size > 0) {
                                    for (k in data.radios) {
                                        if (k?.id!! == sId) {
                                            radiosData = k
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (aRadios!![0].radios.size > 0)
                            radiosData = aRadios!![0].radios[0]
                    }
                } else {
                    ToastUtils.show("暂无任何电台！")
                    finish()
                }

                if (radiosData != null) {
                    radiosData?.let {
                        playlist!!.type = 1
                        playlist!!.pid = it?.id
                        playlist!!.name = it?.name
                        playlist!!.coverUrl = it?.img
                        playlist!!.count = it?.radioAudioCount?.toLong()!!
                    }
                    if (radiosData?.musicList != null && radiosData?.musicList?.size!! > 0) {
                        musicList = radiosData?.musicList as ArrayList<Music>
                        loadPlayData()
                    } else {
                        mPresenter?.createDB(playlist?.pid!!)
                        playlist!!.pid?.let { mPresenter!!.loadMusicList(it) }
                    }!!
                } else {
                    ToastUtils.show("暂无任何电台！")
                    finish()
                }
            } else {
                ToastUtils.show("网络异常，请重新尝试！")
                finish()
            }


    fun hmTime(time: Long): String {
        val formatter = SimpleDateFormat("mm:ss")
        formatter.timeZone = TimeZone.getTimeZone("GMT+00:00")
        val fmTime = formatter.format(time)
        return "$fmTime:00"
    }

    override fun showMusicList(data: QueryRadioAudioListBean?) =
            if (data != null && data.data != null && data.data.radioAudios != null && data.data.radioAudios.size > 0) {
                hideLoading()
                //音乐列表
                for (raData in data.data.radioAudios) {
                    val newData = Music()
                    newData.artist = playlist!!.name
                    newData.artistId = playlist!!.pid
                    newData.type = Constants.XIAOJIA
                    newData.mid = raData.id
                    newData.title = raData.name
                    newData.uri = raData.audioUrl
                    newData.coverUri = playlist!!.coverUrl
                    newData.date = raData.createTime
                    newData.year = raData.playTime
                    newData.isLove = raData.isHasFavorite
                    val list = raData.lyricJson.list
                    if (list != null) {
                        for (i in list.indices) {
                            val da = list[i]
                            da.lrcTime = hmTime(da.time)
                        }
                        newData.lrcList = list
                    }
                    val lyric = StringBuilder()
                    newData.lrcList!!.forEach {
                        lyric.append("[" + it.lrcTime + ", " + it.info.replace("\r", "") + "]")
                        lyric.append("\n")
                    }
                    newData.lyric = lyric.toString()
                    musicList.add(newData)
                }
                if (musicList != null && musicList.size > 0) {
                    loadPlayData()
                } else {
                    ToastUtils.show("网络异常，请重新尝试！")
                    finish()
                }
            } else {
                hideLoading()
                ToastUtils.show("网络异常，请重新尝试！")
                finish()
            }

    private var playingMusic: Music? = null
    private var coverView: View? = null
    private var lyricView: View? = null
    private val viewPagerContent = mutableListOf<View>()
    private var mLyricView: LyricView? = null
    private var coverAnimator: ObjectAnimator? = null
    private var musicList = ArrayList<Music>()
    private var playlist = Playlist()
    override fun showNowPlaying(music: Music?) {
        if (music != null) {
            playingMusic = music
            //更新标题
            titleIv.text = music?.artist
            subTitleTv.text = music?.title
            updateMusicType(playingMusic?.type)
            //更新收藏状态
            music?.isLove?.let {
                collectIv.setBackgroundResource(if (it) R.mipmap.aixin_red_img else R.mipmap.aixin_gray_icon)
            }
            coverAnimator?.start()
        }
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_player
    }

    override fun hasToolbar(): Boolean {
        return false
    }

    override fun initView() {
        detailView.animation = moveToViewLocation()
        updatePlayMode()
    }

    override fun updatePlayMode() {
        UIUtils.updatePlayMode(playModeIv, false)
    }

    lateinit var rxPermissions: RxPermissions

    //需要检查的权限
    private val mPermissionList = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)

    /**
     * 检查权限
     */
    @SuppressLint("CheckResult")
    private fun checkPermissionAndThenLoad() {
        rxPermissions.request(*mPermissionList)
                .subscribe { granted ->
                    if (granted!!) {
                        initPlayData()
                    } else Snackbar.make(wel_container, resources.getString(R.string.permission_hint),
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction(resources.getString(R.string.sure)) { checkPermissionAndThenLoad() }.show()
                }
    }

    /**
     * 加载播放数据
     */
    private fun initPlayData() {
        var sId = SPUtils.getPId()
        var namid = intent.extras.getString("nameid")
        var position = intent.extras.getInt("position")
        if (namid != null) {
            musicList = intent.extras.get("playList") as ArrayList<Music>
        } else {
            SPUtils.setXiaoJiaToken(intent.extras.getString("xj_token"))
            SPUtils.setStudentId(intent.extras.getString("xj_sid"))
            SPUtils.setServiceId(intent.extras.getString("xj_serviceid"))
        }
        if (SPUtils.getXiaoJiaToken() == null) {
            ToastUtils.show("參數異常，token爲空！")
            finish()
            return
        }
        if (sId != null && !TextUtils.isEmpty(sId)) {
            mPresenter?.getMusicListDB(SPUtils.getPId(), position, namid)
        } else
            mPresenter!!.loadSpeData(true)
    }

    @SuppressLint("LongLogTag")
    override fun initData() {
        rxPermissions = RxPermissions(this)
        if (SystemUtils.isMarshmallow()) {
            checkPermissionAndThenLoad()
        } else {
            initPlayData()
        }
    }

    /**
     * 加载播放音频
     */
    private fun loadPlayData() {
        SPUtils.setPid(playlist.pid)
        mPresenter?.insertMusicListDB(playlist.pid, musicList)
        var index = SPUtils.getPlayPosition()
        if (index <= 0) {
            index = 0
        }
        PlayManager.play(index!!, musicList, musicList[index].artistId + musicList[index].id)
        setupViewPager(viewPager)
        initAlbumPic(coverView?.findViewById(R.id.civ_cover))
        mPresenter?.updateNowPlaying(PlayManager.getPlayingMusic())
        //初始加載歌詞
        //更新播放状态
        PlayManager.isPlaying().let {
            updatePlayStatus(it)
        }
        showLyric(FloatLyricViewManager.lyricInfo, true)
        updateMusicType(playingMusic?.type)
    }

    fun loadPlayData(index: Int?, name: String?) {
        PlayManager.play(index!!, musicList, name)
        setupViewPager(viewPager)
        initAlbumPic(coverView?.findViewById(R.id.civ_cover))
        mPresenter?.updateNowPlaying(PlayManager.getPlayingMusic())
        //初始加載歌詞
        //更新播放状态
        PlayManager.isPlaying().let {
            updatePlayStatus(it)
        }
        showLyric(FloatLyricViewManager.lyricInfo, true)
        updateMusicType(playingMusic?.type)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)//must store the new intent unless getIntent() will return the old one
        initData()
    }

    override fun listener() {
        super.listener()
        backIv.setOnClickListener {
            closeActivity()
        }
        progressSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.let {
                    PlayManager.seekTo(it)
                    mLyricView?.setCurrentTimeMillis(it.toLong())
                }
            }

        })
        playPauseIv.setOnClickListener {
            PlayManager.playPause()
        }

    }

    override fun initInjector() {
        mActivityComponent.inject(this)
    }


    fun nextPlay(view: View?) {
        if (UIUtils.isFastClick()) return
        PlayManager.next()
    }

    fun prevPlay(view: View?) {
        if (UIUtils.isFastClick()) return
        PlayManager.prev()
    }

    fun changePlayMode(view: View?) {
        UIUtils.updatePlayMode(view as ImageView, true)
    }

    /**
     * 打开播放队列
     */
    fun openPlayQueue(view: View?) {
        PlayQueueDialog.newInstance().showIt(this)
    }

    /**
     * 歌曲收藏
     */
    fun collectMusic(view: View?) {
        if (playingMusic != null) {
            showLoading()
            UIUtils.collectMusic(view as ImageView, playingMusic)
        }
    }

    /**
     * 添加到歌單
     */
    fun addToPlaylist(view: View?) {
        if (playingMusic != null)
            OnlinePlaylistUtils.addToPlaylist(this, playingMusic)
    }


    /**
     * 分享歌曲
     */
    fun shareMusic(view: View?) {
        MusicUtils.qqShare(this, PlayManager.getPlayingMusic())
    }

    fun downloadMusic(view: View?) {
        if (playingMusic != null)
            downloadMusic(playingMusic)
    }

    override fun setPlayingBitmap(albumArt: Bitmap?) {
        coverView?.findViewById<ImageView>(R.id.civ_cover)?.setImageBitmap(albumArt)
    }

    override fun setPlayingBg(albumArt: Drawable?) {
        //加载背景图过度
//        TransitionAnimationUtils.startChangeAnimation(playingBgIv, albumArt)
    }

    override fun updatePlayStatus(isPlaying: Boolean) {
        if (isPlaying) {
            playPauseIv.play()
            coverAnimator?.isStarted?.let {
                if (it) coverAnimator?.resume() else coverAnimator?.start()
            }
        } else {
            coverAnimator?.pause()
            playPauseIv.pause()
        }
    }

    private var mPalette: Palette? = null
    private var mSwatch: Palette.Swatch? = null

    override fun setPalette(palette: Palette?) {

        mPalette = palette
        mSwatch = ColorUtil.getMostPopulousSwatch(palette)

        val paletteColor: Int
        if (mSwatch != null) {
            paletteColor = Color.WHITE
            val artistColor = Color.WHITE
            titleIv.setTextColor(ColorUtil.getOpaqueColor(artistColor))
            subTitleTv.setTextColor(artistColor)
        } else {
            mSwatch = palette?.mutedSwatch ?: palette?.vibrantSwatch
            if (mSwatch != null) {
                paletteColor = Color.WHITE
//                val artistColor = mSwatch!!.titleTextColor
                val artistColor = Color.WHITE
                titleIv.setTextColor(ColorUtil.getOpaqueColor(artistColor))
                subTitleTv.setTextColor(artistColor)
            } else {
                paletteColor = Color.WHITE
                titleIv.setTextColor(ContextCompat.getColor(context!!, android.R.color.white))
                subTitleTv.setTextColor(ContextCompat.getColor(context!!, android.R.color.secondary_text_light))
            }
        }
        //set icon color
        val blackWhiteColor = ColorUtil.getBlackWhiteColor(paletteColor)
//        val statusBarColor = ColorUtil.getStatusBarColor(paletteColor)

        progressTv.setTextColor(blackWhiteColor)
        durationTv.setTextColor(blackWhiteColor)
        playModeIv.setColorFilter(blackWhiteColor)
//        prevPlayIv.setColor(blackWhiteColor)
//        nextPlayIv.setColor(blackWhiteColor)
        backIv.setColorFilter(blackWhiteColor)
//        playQueueIv.setColor(blackWhiteColor)
        downloadIv.setColor(blackWhiteColor)
        shareIv.setColor(blackWhiteColor)
        playlistAddIv.setColor(blackWhiteColor)
        playPauseIv.btnColor = blackWhiteColor
    }

    override fun showLyric(lyric: String?, init: Boolean) {
        if (init) {
            //初始化歌词配置
            mLyricView?.setTextSize(40)
            mLyricView?.setHighLightTextColor(SPUtils.getFontColor())
            mLyricView?.setTouchable(true)
            mLyricView?.setOnPlayerClickListener { progress, content ->
                if (progress.toInt() == 0 && content == null) {
                    viewPager.currentItem = 0
                } else {
                    PlayManager.seekTo(progress.toInt())
                    if (!PlayManager.isPlaying()) {
                        PlayManager.playPause()
                    }
                }

            }
        }
        mLyricView?.setLyricContent(lyric)

        searchLyricIv.setOnClickListener {
            MusicLyricDialog().apply {
                if (playingMusic != null) {
                    title = playingMusic?.title
                    artist = playingMusic?.artist
                    duration = PlayManager.getDuration().toLong()
                    searchListener = {
                    }
                    textSizeListener = {
                        mLyricView?.setTextSize(it)
                    }
                    textColorListener = {
                        mLyricView?.setHighLightTextColor(it)
                    }
                    lyricListener = {
                        mLyricView?.setLyricContent(it)
                    }
                }
            }.show(this)

        }
        searchLyricIv_text.setOnClickListener {
            val intent = Intent(context, SpecialActivity::class.java)
            startActivity(intent)
        }
    }

    override fun updateProgress(progress: Long, max: Long) {
        progressSb.progress = progress.toInt()
        progressSb.max = max.toInt()
        progressTv.text = FormatUtil.formatTime(progress)
        durationTv.text = FormatUtil.formatTime(max)

        mLyricView?.setCurrentTimeMillis(progress)
    }

    private fun setupViewPager(viewPager: MultiTouchViewPager) {
        viewPagerContent.clear()
        //初始化View
        coverView = LayoutInflater.from(this).inflate(R.layout.frag_player_coverview, viewPager, false)
        lyricView = LayoutInflater.from(this).inflate(R.layout.frag_player_lrcview, viewPager, false)
        mLyricView = lyricView?.findViewById(R.id.lyricShow)
        coverView?.let {
            viewPagerContent.add(it)
        }
        lyricView?.let {
            viewPagerContent.add(it)
        }
        mLyricView?.setOnClickListener { viewPager.currentItem = 0 }
        lyricView?.setOnClickListener {
            viewPager.currentItem = 0
        }
        val mAdapter = MyPagerAdapter(viewPagerContent)
        viewPager.adapter = mAdapter
        viewPager.setPageTransformer(true, DepthPageTransformer())
        viewPager.offscreenPageLimit = 2
        viewPager.currentItem = 0
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                LogUtil.d("PlayControlFragment", "--$position")
                if (position == 0) {
                    searchLyricIv.visibility = View.GONE
                    mLyricView?.setIndicatorShow(false)
                } else {
                    searchLyricIv.visibility = View.GONE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun moveToViewLocation(): TranslateAnimation {
        val mHiddenAction = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        mHiddenAction.duration = 300
        return mHiddenAction
    }


    /**
     * 初始化旋转动画
     */
    private fun initAlbumPic(view: View?) {
        if (view == null) return
        coverAnimator = ObjectAnimator.ofFloat(view, "rotation", 0F, 359F).apply {
            duration = (20 * 1000).toLong()
            repeatCount = -1
            repeatMode = ObjectAnimator.RESTART
            interpolator = LinearInterpolator()
        }
        view.setOnClickListener {
            viewPager.currentItem = 1
        }
    }


    /**
     * 更新歌曲類型
     */
    private fun updateMusicType(type: String?) {
        val value: String? = when (type) {
            Constants.QQ -> {
                getString(R.string.res_qq)
            }
            Constants.BAIDU -> {
                getString(R.string.res_baidu)
            }
            Constants.NETEASE -> {
                getString(R.string.res_wangyi)
            }
            Constants.XIAMI -> {
                getString(R.string.res_xiami)
            }
            Constants.XIAOJIA -> {
                getString(R.string.xiaojia_local)
            }
            else -> {
                getString(R.string.res_local)
            }
        }
        value?.let {
            if (playingMusic != null)
                coverView?.findViewById<TextView>(R.id.tv_source)?.text = playingMusic!!.title
        }
    }

    /**
     * 重置播放
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRerstPlayEvent(event: PlayMusicEvent) {
        if (event == null) return
        if (event.list != null && event.list.size > 0) {
            musicList = event.list as ArrayList<Music>
            loadPlayData(event.index, event.nid)
        }
    }

    /**
     * 收藏
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCollectEvent(event: InfoChangeEvent) {
        when (event.type) {
            Constants.PLAYLIST_LOVE_ID -> {
                if (!event?.music?.isLove!!) {
                    mPresenter?.loadCollect(event?.music?.mid!!, 0)
                } else {
                    mPresenter?.loadCollect(event?.music?.mid!!, 1)
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPlayModeChangedEvent(event: PlayModeEvent) {
        updatePlayMode()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMetaChangedEvent(event: MetaChangedEvent) {
        mPresenter?.updateNowPlaying(event.music)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updatePlayStatus(event: StatusChangedEvent) {
        playPauseIv.setLoading(!event.isPrepared)
        updatePlayStatus(event.isPlaying)
    }

    override fun onBackPressed() {
        closeActivity()
    }


    private fun closeActivity() {
        overridePendingTransition(0, 0)
        ActivityCompat.finishAfterTransition(this)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        coverAnimator = null
        EventBus.getDefault().unregister(this)
        Constants.radiosData = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }
}

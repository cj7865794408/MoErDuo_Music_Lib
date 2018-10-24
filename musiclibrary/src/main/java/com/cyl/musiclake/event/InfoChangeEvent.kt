package com.cyl.musiclake.event

import com.cyl.musiclake.bean.Music
import com.cyl.musiclake.common.Constants


/**
 * Created by master on 2018/4/5.
 */

class InfoChangeEvent(var type: String? = Constants.PLAYLIST_CUSTOM_ID,var music: Music?)

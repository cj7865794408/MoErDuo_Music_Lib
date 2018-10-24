package com.cyl.musiclake.event;

import com.cyl.musiclake.bean.Music;

import java.util.List;

public class PlayMusicEvent {

    private int index;
    private String nid;
    private List<Music> list;

    public PlayMusicEvent(int index, String nid, List<Music> list) {
        this.index = index;
        this.nid = nid;
        this.list = list;
    }

    public int getIndex() {
        return index;
    }

    public String getNid() {
        return nid;
    }

    public List<Music> getList() {
        return list;
    }

}

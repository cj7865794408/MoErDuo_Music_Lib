package com.cyl.musiclake.event;

import com.cyl.musiclake.bean.SpecailRadioBean;

public class AdapterEvent {
    private int position;
    private int itemposition;
    private SpecailRadioBean.DataBean.RadiosBean radiosBean;
    public AdapterEvent(int position, int itemposition, SpecailRadioBean.DataBean.RadiosBean radiosBean)
    {
        this.position =position;
        this.itemposition = itemposition;
        this.radiosBean = radiosBean;
    }

    public SpecailRadioBean.DataBean.RadiosBean getRadiosBean() {
        return radiosBean;
    }

    public void setRadiosBean(SpecailRadioBean.DataBean.RadiosBean radiosBean) {
        this.radiosBean = radiosBean;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getItemposition() {
        return itemposition;
    }

    public void setItemposition(int itemposition) {
        this.itemposition = itemposition;
    }
}

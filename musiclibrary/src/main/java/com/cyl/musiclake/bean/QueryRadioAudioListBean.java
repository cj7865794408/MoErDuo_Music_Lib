package com.cyl.musiclake.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class QueryRadioAudioListBean implements Serializable {
    private int resultCode;
    private DataBean data;
    private boolean success;

    public int getCode() {
        return resultCode;
    }

    public void setCode(int code) {
        this.resultCode = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean implements Serializable {
        private List<RadioAudiosBean> radioAudios;

        public List<RadioAudiosBean> getRadioAudios() {
            return radioAudios;
        }

        public void setRadioAudios(List<RadioAudiosBean> radioAudios) {
            this.radioAudios = radioAudios;
        }

        public static class RadioAudiosBean implements Serializable {
            /**
             * id : 00c92640-42ed-4c55-818b-8be10a95bf0c
             * createTime : 1529401988563
             * status : 0
             * name : You Have One.I Have One  你拍一.我拍一
             * radioId : 4f09905a-a19c-43da-89e4-8b8115d87308
             * playTime : 0:54
             * audioUrl : https://ajiau.oss-cn-shenzhen.aliyuncs.com/radio/2018/0619/152940198656946.mp3
             * lyricUrl : https://ajiau.oss-cn-shenzhen.aliyuncs.com/radio/2018/0620/152947636956801.json
             * shareMsg : You Have One.I Have One  你拍一.我拍一
             * orderNum : 1006
             */

            private String id;
            private long createTime;
            private int status;
            private String name;
            private String radioId;
            private String playTime;
            private String audioUrl;
            private String lyricUrl;
            private String shareMsg;
            private int orderNum;
            private int radioAudioCount;
            private boolean hasFavorite;

            public boolean isHasFavorite() {
                return hasFavorite;
            }

            public void setHasFavorite(boolean hasFavorite) {
                this.hasFavorite = hasFavorite;
            }

            private LyricInfoBeans lyricJson;

           public static class LyricInfoBeans implements Serializable {
                ArrayList<LyricBaseInfoBeans> list;

                public ArrayList<LyricBaseInfoBeans> getList() {
                    return list;
                }

                public void setList(ArrayList<LyricBaseInfoBeans> list) {
                    this.list = list;
                }
            }

           public static class LyricBaseInfoBeans implements Serializable
            {
                long time;
                String info;
                String lrcTime;

                public String getLrcTime() {
                    return lrcTime;
                }

                public void setLrcTime(String lrcTime) {
                    this.lrcTime = lrcTime;
                }

                public long getTime() {
                    return time;
                }

                public void setTime(long time) {
                    this.time = time;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }
            }
            public LyricInfoBeans getLyricJson() {
                return lyricJson;
            }

            public void setLyricJson(LyricInfoBeans lyricJson) {
                this.lyricJson = lyricJson;
            }

            public int getRadioAudioCount() {
                return radioAudioCount;
            }

            public void setRadioAudioCount(int radioAudioCount) {
                this.radioAudioCount = radioAudioCount;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRadioId() {
                return radioId;
            }

            public void setRadioId(String radioId) {
                this.radioId = radioId;
            }

            public String getPlayTime() {
                return playTime;
            }

            public void setPlayTime(String playTime) {
                this.playTime = playTime;
            }

            public String getAudioUrl() {
                return audioUrl;
            }

            public void setAudioUrl(String audioUrl) {
                this.audioUrl = audioUrl;
            }

            public String getLyricUrl() {
                return lyricUrl;
            }

            public void setLyricUrl(String lyricUrl) {
                this.lyricUrl = lyricUrl;
            }

            public String getShareMsg() {
                return shareMsg;
            }

            public void setShareMsg(String shareMsg) {
                this.shareMsg = shareMsg;
            }

            public int getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(int orderNum) {
                this.orderNum = orderNum;
            }
        }
    }
}

package com.cyl.musiclake.bean;

import java.io.Serializable;
import java.util.List;

public class SpecailRadioBean implements Serializable {

    /**
     * code : 0
     * data : {"radios":[{"img":"http://ajiau.oss-cn-shenzhen.aliyuncs.com/radio/2017/0325/149041300984262.png","name":"朗朗上口","id":"4f09905a-a19c-43da-89e4-8b8115d87308"},{"img":"http://ajiau.oss-cn-shenzhen.aliyuncs.com/radio/2017/0325/149041273310543.png","name":"初级英语儿歌","id":"65edbaaf-c5ce-4794-b5b4-31722de3c3e9"},{"img":"http://ajiau.oss-cn-shenzhen.aliyuncs.com/radio/2017/0323/149024949502550.png","name":"热门电影","id":"e5340131-da77-4c6e-8239-538e7c77fd0a"},{"img":"http://ajiau.oss-cn-shenzhen.aliyuncs.com/radio/2017/0323/149024943252546.png","name":"双语电台","id":"a475b22b-bd23-418c-99c9-fb0ac37c7262"}]}
     * success : true
     */

    private int resultCode;
    private ListDataBean data;
    private boolean success;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return resultCode;
    }

    public void setCode(int code) {
        this.resultCode = code;
    }

    public ListDataBean getData() {
        return data;
    }

    public void setData(ListDataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public static class ListDataBean implements Serializable{
        List<DataBean> list;
        List<MyAudioDataBean> myAudios;

        public List<MyAudioDataBean> getMyAudios() {
            return myAudios;
        }

        public void setMyAudios(List<MyAudioDataBean> myAudios) {
            this.myAudios = myAudios;
        }

        public List<DataBean> getList() {
            return list;
        }

        public void setList(List<DataBean> list) {
            this.list = list;
        }
    }

    public static class MyAudioDataBean implements Serializable
    {
        String name;
        String typeImg;
        private List<QueryRadioAudioListBean.DataBean.RadioAudiosBean> favoriteAudios;

        public String getTypeImg() {
            return typeImg;
        }

        public void setTypeImg(String typeImg) {
            this.typeImg = typeImg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<QueryRadioAudioListBean.DataBean.RadioAudiosBean> getRadioAudios() {
            return favoriteAudios;
        }

        public void setRadioAudios(List<QueryRadioAudioListBean.DataBean.RadioAudiosBean> radioAudios) {
            this.favoriteAudios = radioAudios;
        }
    }
    public static class DataBean implements Serializable{
        private List<RadiosBean> radioAudios;
        private String name;
        private String typeImg;

        public String getTypeImg() {
            return typeImg;
        }

        public void setTypeImg(String typeImg) {
            this.typeImg = typeImg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<RadiosBean> getRadios() {
            return radioAudios;
        }

        public void setRadios(List<RadiosBean> radios) {
            this.radioAudios = radios;
        }

        public static class RadiosBean implements Serializable{
            /**
             * img : http://ajiau.oss-cn-shenzhen.aliyuncs.com/radio/2017/0325/149041300984262.png
             * name : 朗朗上口
             * id : 4f09905a-a19c-43da-89e4-8b8115d87308
             */

            private String img;
            private String name;
            private String id;
            private int radioAudioCount;
            private boolean isCheck;
            private List<Music> musicList;

            public List<Music> getMusicList() {
                return musicList;
            }

            public void setMusicList(List<Music> musicList) {
                this.musicList = musicList;
            }

            public boolean isCheck() {
                return isCheck;
            }

            public void setCheck(boolean check) {
                isCheck = check;
            }

            public int getRadioAudioCount() {
                return radioAudioCount;
            }

            public void setRadioAudioCount(int radioAudioCount) {
                this.radioAudioCount = radioAudioCount;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}

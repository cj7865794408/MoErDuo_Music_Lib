package com.cyl.musiclake.data.db;//package com.cyl.musiclake.data.db;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import org.litepal.crud.DataSupport;
//
///**
// * 作者：yonglong on 2016/8/9 10:50
// * 邮箱：643872807@qq.com
// * 版本：2.5
// */
//public class Music extends DataSupport implements Parcelable {
//    // 歌曲类型 本地/网络
//    private Type type;
//    // 歌曲id
//    private String mid;
//    // 音乐标题
//    private String title;
//    // 艺术家
//    private String artist;//{123,123,13}
//    // 专辑
//    private String album;
//    // 专辑id
//    private String artistId;//{123,123,13}
//    // 专辑id
//    private String albumId;
//    // 专辑内歌曲个数
//    private int trackNumber;
//    // 持续时间
//    private long duration;
//    // 收藏
//    private boolean love;
//    // [本地|网络]
//    private boolean online;
//    // 音乐路径
//    private String uri;
//    // [本地|网络] 音乐歌词地址
//    private String lrcPath;
//    // [本地|网络]专辑封面路径
//    private String coverUri;
//    // [网络]专辑封面
//    private String coverBig;
//    // [网络]small封面
//    private String coverSmall;
//    // 文件名
//    private String fileName;
//    // 文件大小
//    private long fileSize;
//    // 发行日期
//    private String year;
//    //音乐质量/前缀
//    private String prefix;
//    //更新日期
//    private long date;
//    //在线歌曲是否限制播放，false 可以播放
//    private boolean cp;
//    //歌曲评论Id
//    private String commentId;
//    //收藏id
//    private String collectId;
//
//
////    private Album albumBean;
////    private List<Artist> artists;
//
//
//    public Music(long id, String albumId, String artistId, String title, String artist, String album, long duration, int trackNumber, String uri) {
//        this.mid = String.valueOf(id);
//        this.title = title;
//        this.artist = artist;
//        this.album = album;
//        this.artistId = artistId;
//        this.albumId = albumId;
//        this.duration = duration;
//        this.trackNumber = trackNumber;
//        this.uri = uri;
//        this.type = Type.LOCAL;
//        this.online = false;
//    }
//
//
//    public Music() {
//        this.mid = "";
//        this.albumId = "";
//        this.artistId = "";
//
//        this.title = "未知";
//        this.artist = "未知";
//        this.album = "未知";
//
//        this.uri = "";
//        this.lrcPath = "";
//        this.coverUri = "";
//        this.fileSize = -1;
//        this.duration = -1;
//
//        this.year = "未知";
//        this.type = Type.LOCAL;
//    }
//
//    protected Music(Parcel in) {
//        mid = in.readString();
//        title = in.readString();
//        artist = in.readString();
//        album = in.readString();
//        artistId = in.readString();
//        albumId = in.readString();
//        trackNumber = in.readInt();
//        duration = in.readLong();
//        love = in.readByte() != 0;
//        online = in.readByte() != 0;
//        uri = in.readString();
//        lrcPath = in.readString();
//        coverUri = in.readString();
//        coverBig = in.readString();
//        coverSmall = in.readString();
//        fileName = in.readString();
//        fileSize = in.readLong();
//        year = in.readString();
//        prefix = in.readString();
//    }
//
//    public static final Creator<Music> CREATOR = new Creator<Music>() {
//        @Override
//        public Music createFromParcel(Parcel in) {
//            return new Music(in);
//        }
//
//        @Override
//        public Music[] newArray(int size) {
//            return new Music[size];
//        }
//    };
//
//    public String getPrefix() {
//        return prefix;
//    }
//
//    public void setPrefix(String prefix) {
//        this.prefix = prefix;
//    }
//
//    public String getId() {
//        return mid;
//    }
//
//    public void setId(String id) {
//        this.mid = id;
//    }
//
//    public int getTrackNumber() {
//        return trackNumber;
//    }
//
//    public void setTrackNumber(int trackNumber) {
//        this.trackNumber = trackNumber;
//    }
//
//    public String getArtistId() {
//        return artistId;
//    }
//
//    public void setArtistId(String artistId) {
//        this.artistId = artistId;
//    }
//
//    public String getAlbumId() {
//        return albumId;
//    }
//
//    public void setAlbumId(String albumId) {
//        this.albumId = albumId;
//    }
//
//    public String getAlbum() {
//        return album;
//    }
//
//    public void setAlbum(String album) {
//        this.album = album;
//    }
//
//    public String getArtist() {
//        return artist;
//    }
//
//    public void setArtist(String artist) {
//        this.artist = artist;
//    }
//
//    public boolean isOnline() {
//        return online;
//    }
//
//    public void setOnline(boolean online) {
//        this.online = online;
//    }
//
//    public boolean isLove() {
//        return love;
//    }
//
//    public void setLove(boolean love) {
//        this.love = love;
//    }
//
//    public String getCoverUri() {
//        return coverUri;
//    }
//
//    public void setCoverUri(String coverUri) {
//        this.coverUri = coverUri;
//    }
//
//    public String getCoverBig() {
//        return coverBig;
//    }
//
//    public void setCoverBig(String coverBig) {
//        this.coverBig = coverBig;
//    }
//
//    public String getCoverSmall() {
//        return coverSmall;
//    }
//
//    public void setCoverSmall(String coverSmall) {
//        this.coverSmall = coverSmall;
//    }
//
//    public long getDuration() {
//        return duration;
//    }
//
//    public void setDuration(long duration) {
//        this.duration = duration;
//    }
//
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public long getFileSize() {
//        return fileSize;
//    }
//
//    public void setFileSize(long fileSize) {
//        this.fileSize = fileSize;
//    }
//
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public Type getType() {
//        return type;
//    }
//
//    /**
//     * @param lower
//     * @return
//     */
//    public String getTypeName(boolean lower) {
//        if (type == Type.QQ) {
//            return lower ? "qq" : "QQ音乐";
//        } else if (type == Type.XIAMI) {
//            return lower ? "xiami" : "虾米音乐";
//        } else if (type == Type.BAIDU) {
//            return lower ? "baidu" : "百度音乐";
//        } else if (type == Type.NETEASE) {
//            return lower ? "netease" : "网易云音乐";
//        } else {
//            return "本地音乐";
//        }
//    }
//
//    public String getCollectId() {
//        return collectId;
//    }
//
//    public void setCollectId(String collectId) {
//        this.collectId = collectId;
//    }
//
//    public void setType(Type type) {
//        this.type = type;
//    }
//
//    public long getDate() {
//        return date;
//    }
//
//    public void setDate(long date) {
//        this.date = date;
//    }
//
//    public boolean isCp() {
//        return cp;
//    }
//
//    public void setCp(boolean cp) {
//        this.cp = cp;
//    }
//
//    public String getCommentId() {
//        return commentId;
//    }
//
//    public void setCommentId(String commentId) {
//        this.commentId = commentId;
//    }
//
//    public String getUri() {
//        return uri;
//    }
//
//    public void setUri(String uri) {
//        this.uri = uri;
//    }
//
//    public String getYear() {
//        return year;
//    }
//
//    public void setYear(String year) {
//        this.year = year;
//    }
//
//    public String getLrcPath() {
//        return lrcPath;
//    }
//
//    public void setLrcPath(String lrcPath) {
//        this.lrcPath = lrcPath;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(mid);
//        dest.writeString(title);
//        dest.writeString(artist);
//        dest.writeString(album);
//        dest.writeString(artistId);
//        dest.writeString(albumId);
//        dest.writeInt(trackNumber);
//        dest.writeLong(duration);
//        dest.writeByte((byte) (love ? 1 : 0));
//        dest.writeByte((byte) (online ? 1 : 0));
//        dest.writeString(uri);
//        dest.writeString(lrcPath);
//        dest.writeString(coverUri);
//        dest.writeString(coverBig);
//        dest.writeString(coverSmall);
//        dest.writeString(fileName);
//        dest.writeLong(fileSize);
//        dest.writeString(year);
//        dest.writeString(prefix);
//    }
//
//    public void setType(String type) {
//        if (type.equals("QQ") || type.equals("qq")) {
//            this.type = Type.QQ;
//        } else if (type.equals("XIAMI") || type.equals("xiami")) {
//            this.type = Type.XIAMI;
//        } else if (type.equals("BAIDU") || type.equals("baidu")) {
//            this.type = Type.BAIDU;
//        } else if (type.equals("NETEASE") || type.equals("netease")) {
//            this.type = Type.NETEASE;
//        } else {
//            this.type = Type.LOCAL;
//        }
//    }
//
//
//    public enum Type {
//        QQ,
//        XIAMI,
//        BAIDU,
//        LOCAL,
//        NETEASE
//    }
//
//    @Override
//    public String toString() {
//        return "Music{" +
//                "type=" + type +
//                ", id='" + mid + '\'' +
//                ", title='" + title + '\'' +
//                ", artist='" + artist + '\'' +
//                ", album='" + album + '\'' +
//                ", artistId=" + artistId +
//                ", albumId=" + albumId +
//                ", trackNumber=" + trackNumber +
//                ", duration=" + duration +
//                ", love=" + love +
//                ", online=" + online +
//                ", uri='" + uri + '\'' +
//                ", lrcPath='" + lrcPath + '\'' +
//                ", coverUri='" + coverUri + '\'' +
//                ", coverBig='" + coverBig + '\'' +
//                ", coverSmall='" + coverSmall + '\'' +
//                ", fileName='" + fileName + '\'' +
//                ", fileSize=" + fileSize +
//                ", year='" + year + '\'' +
//                ", prefix='" + prefix + '\'' +
//                '}';
//    }
//}

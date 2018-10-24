package com.cyl.musiclake.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.cyl.musiclake.MusicApp;
import com.cyl.musiclake.R;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 作者：yonglong on 2016/8/12 16:03
 * 邮箱：643872807@qq.com
 * 版本：2.5
 * 内部存儲工具類
 */
public class SPUtils {
    private static final String MUSIC_ID = "music_id";
    private static final String PLAY_POSITION = "play_position";
    private static final String PLAY_MODE = "play_mode";
    private static final String SPLASH_URL = "splash_url";
    private static final String WIFI_MODE = "wifi_mode";
    private static final String LYRIC_MODE = "lyric_mode";
    private static final String NIGHT_MODE = "night_mode";
    private static final String POSITION = "position";
    private static final String DESKTOP_LYRIC_SIZE = "desktop_lyric_size";
    private static final String DESKTOP_LYRIC_COLOR = "desktop_lyric_color";
    public static final String QQ_OPEN_ID = "qq_open_id";
    public static final String QQ_ACCESS_TOKEN = "qq_access_token";
    public static final String QQ_EXPIRES_IN = "expires_in";
    public static final String MUSIC_LIST_DATA = "music_list";

    //    public static ArrayList<Music> getMusic_list(String id) {
//        ArrayList<Music> list = null;
//        Map<String, ArrayList<Music>> map = (Map<String, ArrayList<Music>>) getObject(MusicApp.getAppContext(), "getContactsBeans", "getContactsBeans");
//        if (map != null) {
//            list = map.get(id);
//            return list;
//        }
//        return list;
//    }

    /**
     * 保存Token
     *
     * @param id
     */
    public static void setXiaoJiaToken(String id) {
        putAnyCommit("gettoken", id);
    }

    public static String getXiaoJiaToken() {
        return getAnyByKey("gettoken", "");
    }

    /**
     * 保存Token
     *
     * @param id
     */
    public static void setStudentId(String id) {
        putAnyCommit("getstudentid", id);
    }

    public static String getStudentId() {
        return getAnyByKey("getstudentid", "");
    }


    public static void setPid(String id) {
        putAnyCommit("getContactsBeansId", id);
    }

    public static String getPId() {
        return getAnyByKey("getContactsBeansId", "");
    }

//    public static void saveMusic_list(String id, ArrayList<Music> list) {
//        Map<String, ArrayList<Music>> map = new HashMap();
//        map.put(id, list);
//        savaObject(MusicApp.getAppContext(), "getContactsBeans", "getContactsBeans", map);
//    }


    public static int getPlayPosition() {
        return getAnyByKey(PLAY_POSITION, -1);
    }

    public static void setPlayPosition(int position) {
        putAnyCommit(PLAY_POSITION, position);
    }


    public static String getCurrentSongId() {
        return getAnyByKey(MUSIC_ID, "");
    }

    public static void saveCurrentSongId(String mid) {
        putAnyCommit(MUSIC_ID, mid);
    }

    public static long getPosition() {
        return getAnyByKey(POSITION, 0);
    }

    public static void savePosition(long id) {
        putAnyCommit(POSITION, id);
    }

    public static int getPlayMode() {
        return getAnyByKey(PLAY_MODE, 0);
    }

    public static void savePlayMode(int mode) {
        putAnyCommit(PLAY_MODE, mode);
    }

    public static String getSplashUrl() {
        return getAnyByKey(SPLASH_URL, "");
    }

    public static void saveSplashUrl(String url) {
        putAnyCommit(SPLASH_URL, url);
    }

    public static boolean getWifiMode() {
        return getAnyByKey(MusicApp.getAppContext().getString(R.string.setting_key_mobile_wifi), false);
    }

    public static void saveWifiMode(boolean enable) {
        putAnyCommit(MusicApp.getAppContext().getString(R.string.setting_key_mobile_wifi), enable);
    }

    public static boolean isShowLyricView() {
        return getAnyByKey(MusicApp.getAppContext().getString(R.string.setting_key_mobile_wifi), false);
    }

    public static void showLyricView(boolean enable) {
        putAnyCommit(MusicApp.getAppContext().getString(R.string.setting_key_mobile_wifi), enable);
    }


    public static boolean isNightMode() {
        return getAnyByKey(NIGHT_MODE, false);
    }

    public static void saveNightMode(boolean on) {
        putAnyCommit(NIGHT_MODE, on);
    }


    public static int getFontSize() {
        return getAnyByKey(DESKTOP_LYRIC_SIZE, 40);
    }

    public static void saveFontSize(int size) {
        putAnyCommit(DESKTOP_LYRIC_SIZE, size);
    }


    public static void saveFontColor(int color) {
        putAnyCommit(DESKTOP_LYRIC_COLOR, color);
    }

    public static int getFontColor() {
        return getAnyByKey(DESKTOP_LYRIC_COLOR, Color.RED);
    }

    public static int getWhiteFontColor() {
        return getAnyByKey(DESKTOP_LYRIC_COLOR, Color.WHITE);
    }

    /**
     * -------------------------------------------------------
     * <p>底层操作
     * -------------------------------------------------------
     */
    public static boolean getAnyByKey(String key, boolean defValue) {
        return getPreferences().getBoolean(key, defValue);
    }

    public static void putAnyCommit(String key, boolean value) {
        getPreferences().edit().putBoolean(key, value).apply();
    }

    public static float getAnyByKey(String key, float defValue) {
        return getPreferences().getFloat(key, defValue);
    }

    public static void putAnyCommit(String key, float value) {
        getPreferences().edit().putFloat(key, value).apply();
    }

    public static int getAnyByKey(String key, int defValue) {
        return getPreferences().getInt(key, defValue);
    }

    public static void putAnyCommit(String key, int value) {
        getPreferences().edit().putInt(key, value).apply();
    }

    public static long getAnyByKey(String key, long defValue) {
        return getPreferences().getLong(key, defValue);
    }

    public static void putAnyCommit(String key, long value) {
        getPreferences().edit().putLong(key, value).apply();
    }

    public static String getAnyByKey(String key, @Nullable String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public static void putAnyCommit(String key, @Nullable String value) {
        getPreferences().edit().putString(key, value).apply();
    }

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(MusicApp.getAppContext());
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @param ob
     * @方法说明:保存对象
     * @方法名称:savaObject
     * @返回void
     */
    @SuppressWarnings("finally")
    public static boolean savaObject(Context context, String spName, String key,
                                     Object ob) {
        if (ob == null) {
            return false;
        }
        boolean falg = false;
        SharedPreferences preferences = MusicApp.getAppContext()
                .getSharedPreferences(spName, context.MODE_PRIVATE);

        // 创建字节输出
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            // 创建对象输出流，并封装字节流
            oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(ob);
            // 将字节流编码成base64的字符窜

            String oAuth_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            falg = preferences.edit().putString(key, oAuth_Base64).commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }

                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return falg;
        }
    }

    /**
     * 调用apache的解码方法
     */
    public static byte[] getdeBASE64inCodec(String s) {
        if (s == null)
            return null;
        return new Base64().decode(s.getBytes());
    }

    /**
     * @param context
     * @param spName
     * @param key
     * @return
     * @方法说明:获取存储的对 * @方法名称:getObject
     * @返回Object
     */
    public static Object getObject(Context context, String spName, String key) {
        Object ob = null;
        SharedPreferences preferences = context.getSharedPreferences(spName,
                context.MODE_PRIVATE);
        String productBase64 = preferences.getString(key, "");
        // 读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());
        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        ObjectInputStream bis = null;
        try {
            // 再次封装
            bis = new ObjectInputStream(bais);
            // 读取对象
            ob = bis.readObject();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }

                if (bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ob;
    }

}

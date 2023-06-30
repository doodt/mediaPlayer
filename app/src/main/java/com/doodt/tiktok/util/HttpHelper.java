package com.doodt.tiktok.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by doodt on 2017-06-15.
 * 加密通讯类
 */

public class HttpHelper {
    private static String TAG = "HttpHelper";
    private static OkHttpClient okClient = new OkHttpClient();
    private static final MediaType FORM_UTF8 = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final MediaType FORM_JSON = MediaType.parse("application/json; charset=utf-8");

    static {
        //请求超时设置
        okClient.newBuilder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
    }

    /**
     * GET方式获取数据
     *
     * @param url     请求地址
     * @param timeout 连接超时时间（毫秒）
     * @return
     */
    public static String doGet(String url, int timeout) {
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(timeout);
            conn.connect();
            InputStream is = conn.getInputStream();
            if (null != is) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                return sb.length() != 0 ? sb.toString() : null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Get请求失败,url" + url + ",msg" + e.getCause());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }
        return null;
    }


    /**
     * POST方式获取信息
     *
     * @param url     地址
     * @param params  参数 a=b&c=d
     * @param timeout 连接超时时间（毫秒）
     * @return
     */
    private String doPost(String url, String params, int timeout) {
        PrintWriter out;
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();
            if (null != params) {
                out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                out.print(params);// 发送请求参数
                out.flush();// flush输出流的缓冲
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
            InputStream is = conn.getInputStream();// 定义BufferedReader输入流来读取URL的响应
            if (null != is) {
                in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
                return sb.length() != 0 ? sb.toString() : null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Post请求失败：" + e.getCause());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * OKHttp get 方式获取数据
     *
     * @param url 接口地址
     * @return 响应结果, JSONObject
     * @throws Exception
     */
    public static JSONObject okHttpGet(String url) throws Exception {
        JSONObject json = null;
        if (okClient != null) {
            String ts = String.valueOf(System.currentTimeMillis());
            //补充固定参数
            Request request = new Request.Builder().url(url).build();
            Response response = okClient.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("get remote data error:" + response);
            String res = response.body().string();
            if (null != res && res.length() > 0) {
                if (res.startsWith("{")) {
                    json = JSONObject.parseObject(res);
                } else {
                    throw new NullPointerException("illega result:" + res);
                }
            } else {
                throw new NullPointerException("null response");
            }
        }
        return json;
    }

    /**
     * POST获取请求结果
     *
     * @param url     接口地址
     * @param jsonStr 接口参数,json字符串
     * @throws IOException
     */
    public static JSONObject okHttpPost(String url, String jsonStr) throws Exception {
        JSONObject json = null;
        if (okClient != null) {
            String ts = String.valueOf(System.currentTimeMillis());
            //补充固定参数
            Request request = new Request.Builder().url(url).post(RequestBody.create(FORM_JSON, jsonStr)).build();
            Response response = okClient.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("get remote data error:" + response);
            String res = response.body().string();
            if (null != res && res.length() > 0) {
                if (res.startsWith("{")) {
                    json = JSONObject.parseObject(res);
                } else {
                    throw new NullPointerException("illega result:" + res);
                }
            } else {
                throw new NullPointerException("null response");
            }
        }
        return json;
    }

    /**
     * 获取网络图片
     *
     * @param url
     * @return
     */
    public static Bitmap getHttpImage(String url) {
        Bitmap bitmap = null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = okHttpClient.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();//得到图片的流
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.e("getHttpImage", "get remote image error:" + e.getMessage());
        }
        return bitmap;
    }

    /**
     * http url 转码
     *
     * @param urls
     * @return
     */
    public static String encordHttpUrl(String urls) {
        try {
            StringBuilder sb = new StringBuilder();
            String[] u = urls.trim().split("/");
            for (String s : u) {
                sb.append(isContainChinese(s) ? URLEncoder.encode(s, "utf-8") : s);
                sb.append("/");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串是否包含中文
     *
     * @param str 待校验字符串
     * @return true 包含中文字符  false 不包含中文字符
     */
    public static boolean isContainChinese(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】|\\s]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
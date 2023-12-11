package com.doodt.tiktok;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSink;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.multidex.MultiDexApplication;

import com.doodt.tiktok.exosource.GSYExoHttpDataSourceFactory;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;

import org.litepal.LitePal;

import java.io.File;
import java.util.Map;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener;
import tv.danmaku.ijk.media.exo2.ExoPlayerCacheManager;
import tv.danmaku.ijk.media.exo2.ExoSourceManager;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class TiktokApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化litepal
        LitePal.initialize(getApplicationContext());
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }*/
        //LeakCanary.install(this);

        //GSYVideoType.enableMediaCodec();
        //GSYVideoType.enableMediaCodecTexture();

        PlayerFactory.setPlayManager(Exo2PlayerManager.class);//EXO模式

        //PlayerFactory.setPlayManager(SystemPlayerManager.class);//系统模式
//        PlayerFactory.setPlayManager(IjkPlayerManager.class);//ijk模式
        //PlayerFactory.setPlayManager(AliPlayerManager.class);//aliplay模式
        CacheFactory.setCacheManager(ExoPlayerCacheManager.class);//exo缓存模式，支持m3u8，只支持exo
        //CacheFactory.setCacheManager(ProxyCacheManager.class);//代理缓存模式，支持所有模式，不支持m3u8等
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        //GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        //GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);

        //GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_CUSTOM);
//        GSYVideoType.setScreenScaleRatio(9.0f/16);

        //GSYVideoType.setRenderType(GSYVideoType.SUFRACE);
        //GSYVideoType.setRenderType(GSYVideoType.GLSURFACE);

        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);

        //GSYVideoType.setRenderType(GSYVideoType.SUFRACE);

        ExoSourceManager.setExoMediaSourceInterceptListener(new ExoMediaSourceInterceptListener() {
            @Override
            public MediaSource getMediaSource(String dataSource, boolean preview, boolean cacheEnable, boolean isLooping, File cacheDir) {
                //如果返回 null，就使用默认的
                return null;
            }

            /**
             * 通过自定义的 HttpDataSource ，可以设置自签证书或者忽略证书
             * demo 里的 GSYExoHttpDataSourceFactory 使用的是忽略证书
             * */
            @OptIn(markerClass = UnstableApi.class)
            @Override
            public DataSource.Factory getHttpDataSourceFactory(String userAgent, @Nullable TransferListener listener, int connectTimeoutMillis, int readTimeoutMillis, Map<String, String> mapHeadData, boolean allowCrossProtocolRedirects) {
                //如果返回 null，就使用默认的
                GSYExoHttpDataSourceFactory factory = new GSYExoHttpDataSourceFactory(userAgent, listener, connectTimeoutMillis, readTimeoutMillis, allowCrossProtocolRedirects);
                factory.setDefaultRequestProperties(mapHeadData);
                return factory;
            }

            @Override
            public DataSink.Factory cacheWriteDataSinkFactory(String CachePath, String url) {
                return null;
            }
        });

        /*GSYVideoManager.instance().setPlayerInitSuccessListener(new IPlayerInitSuccessListener() {
            ///播放器初始化成果回调
            @Override
            public void onPlayerInitSuccess(IMediaPlayer player, GSYModel model) {
                if (player instanceof IjkExo2MediaPlayer) {

                    /// 自定义你的
                    ((IjkExo2MediaPlayer) player).setTrackSelector(new DefaultTrackSelector());


//                     DefaultTrackSelector.Parameters parameters = trackSelector.buildUponParameters()
//                        .setMaxVideoBitrate(LO_BITRATE)
//                        .setForceHighestSupportedBitrate(true)
//                        .build();
//                    trackSelector.setParameters(parameters);


                    ((IjkExo2MediaPlayer) player).setLoadControl(new DefaultLoadControl());
                }
            }
        });*/

        /*ProxyCacheManager.instance().setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };
        ProxyCacheManager.instance().setTrustAllCerts(trustAllCerts);*/

    }
}

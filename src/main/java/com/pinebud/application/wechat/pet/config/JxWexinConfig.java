package com.pinebud.application.wechat.pet.config;

import com.pinebud.application.wechat.pet.handler.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
@Configuration
@ConditionalOnClass(WxMpService.class)
@EnableConfigurationProperties(JxWechatProperties.class)
public class JxWexinConfig {

    @Autowired
    private JxWechatProperties properties;

    @Bean
    public WxMpConfigStorage wechatConfig(){
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(properties.getAppId()); // 设置微信公众号的appid
        config.setSecret(properties.getSecret()); // 设置微信公众号的app corpSecret
        config.setToken(properties.getToken()); // 设置微信公众号的token
       // config.setAesKey(properties.getAesKey());
        return  config;
    }


    @Bean
    public WxMpService wxMpService(WxMpConfigStorage configStorage) {
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(configStorage);
        return wxService;
    }

    @Bean
    public WxMpMessageRouter router(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_KF_CREATE_SESSION)
                .handler(this.kfSessionHandler).end();
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_KF_CLOSE_SESSION).handler(this.kfSessionHandler)
                .end();
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_KF_SWITCH_SESSION)
                .handler(this.kfSessionHandler).end();

        // 门店审核事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_POI_CHECK_NOTIFY)
                .handler(this.storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.BUTTON_CLICK).handler(this.menuHandler).end();

        // 点击菜单连接事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.BUTTON_VIEW).handler(this.nullHandler).end();

        // 关注事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SUBSCRIBE).handler(this.subscribeHandler)
                .end();

        // 取消关注事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_UNSUBSCRIBE)
                .handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_LOCATION).handler(this.locationHandler)
                .end();

        // 接收地理位置消息
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_LOCATION)
                .handler(this.locationHandler).end();

        // 扫码事件
        newRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SCAN).handler(null).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }

    @Autowired
    private LocationHandler locationHandler;

    @Autowired
    private MenuHandler menuHandler;

    @Autowired
    private MsgHandler msgHandler;

    @Autowired
    protected LogHandler logHandler;

    @Autowired
    protected NullHandler nullHandler;

    @Autowired
    protected KfSessionHandler kfSessionHandler;

    @Autowired
    protected StoreCheckNotifyHandler storeCheckNotifyHandler;

    @Autowired
    private UnsubscribeHandler unsubscribeHandler;

    @Autowired
    private SubscribeHandler subscribeHandler;
}

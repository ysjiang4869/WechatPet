package com.pinebud.application.wechat.pet.io;

import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2016/10/28 0028.
 *
 */
@RestController
@Transactional
@RequestMapping(value = "/")
//处理接收到的用户消息
//接收消息-router转发到对应的handler处理
public class JxWechatMpController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final WxMpService mpsvc;

    private final WxMpMessageRouter router;

    @Autowired
    public JxWechatMpController(WxMpService mpsvc, WxMpMessageRouter router) {
        this.mpsvc = mpsvc;
        this.router = router;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "text/plain;charset=utf-8")
    public @ResponseBody String authGet(@RequestParam(name = "signature", required = false) String signature,
                                        @RequestParam(name = "timestamp", required = false) String timestamp,
                                        @RequestParam(name = "nonce", required = false) String nonce,
                                        @RequestParam(name = "echostr", required = false) String echostr) {
        System.out.println(signature);
        System.out.println(timestamp);
        System.out.println(nonce);
        System.out.println(echostr);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实~");

        }
        if (this.mpsvc.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = "application/xml; charset=UTF-8")
    public @ResponseBody String post(@RequestBody String requestBody,
                                     @RequestParam("signature") String signature,
                                     @RequestParam("timestamp") String timestamp,
                                     @RequestParam("nonce") String nonce,
                                     @RequestParam(name = "encrypt_type",
                                             required = false) String encType,
                                     @RequestParam(name = "msg_signature",
                                             required = false) String msgSignature) {
        this.logger.info("\n接收微信请求：[{},{},{},{},{}]\n{} ", signature, encType,
                msgSignature, timestamp, nonce, requestBody);

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equals(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
                    requestBody, this.mpsvc.getWxMpConfigStorage(), timestamp,
                    nonce, msgSignature);
            this.logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage
                    .toEncryptedXml(this.mpsvc.getWxMpConfigStorage());
        }

        this.logger.debug("\n组装回复信息：{}", out);

        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.router.route(message);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }
}

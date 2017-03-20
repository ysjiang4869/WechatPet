package com.pinebud.application.wechatsangzi.io;

import com.pinebud.application.wechatsangzi.handler.MsgHandler;
import com.pinebud.application.wechatsangzi.service.conversation.JxResDao;
import com.pinebud.application.wechatsangzi.service.flow.JxFlow;
import com.pinebud.application.wechatsangzi.service.flow.JxFlowDao;
import com.pinebud.application.wechatsangzi.service.pet.JxPet;
import com.pinebud.application.wechatsangzi.service.pet.JxPetDao;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by jiangyuesong on 2017/3/20 0020.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/test")
@Transactional
public class JxTest {
    @Autowired
    private JxPetDao petDao;

    @Autowired
    private JxFlowDao flowDao;

    @Autowired
    private JxResDao resDao;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private MsgHandler msgHandler;

    @RequestMapping(value = "/pet")
    public List get1(){
        JxPet pet=petDao.get("asdf");
        return petDao.find();
    }

    @RequestMapping(value = "/flow")
    public List get2(){
        short step=0;
        JxFlow flow=new JxFlow("xxx","test",step);
        flowDao.add(flow);
        return flowDao.find();
    }

    @RequestMapping(value = "/res")
    public List get3(){
        return resDao.find();
    }

    @RequestMapping(value = "/exe")
    public WxMpXmlOutMessage execute(@RequestParam(value = "content")String content,
                                     @RequestParam(value = "id")String id){
        WxMpXmlMessage wxMessage=new WxMpXmlMessage();
        wxMessage.setContent(content);
        wxMessage.setFromUser(id);
        return msgHandler.handle(wxMessage,null,wxMpService,null);
    }
}

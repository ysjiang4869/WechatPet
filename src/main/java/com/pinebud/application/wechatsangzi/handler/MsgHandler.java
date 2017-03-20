package com.pinebud.application.wechatsangzi.handler;

import com.pinebud.application.wechatsangzi.builder.TextBuilder;
import com.pinebud.application.wechatsangzi.service.flow.JxFlow;
import com.pinebud.application.wechatsangzi.service.flow.JxFlowDao;
import com.pinebud.application.wechatsangzi.service.pet.JxPet;
import com.pinebud.application.wechatsangzi.service.pet.JxPetDao;
import com.pinebud.application.wechatsangzi.utils.JsonUtils;
import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Binary Wang
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MsgHandler extends AbstractHandler {

    public MsgHandler() {
        try {
            gse = new GroovyScriptEngine(System.getProperty("user.dir")+"/script/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Autowired
    private JxFlowDao flowDao;

    @Autowired
    private JxPetDao petDao;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
            Map<String, Object> context, WxMpService weixinService,
            WxSessionManager sessionManager)    {

      /*  if (!wxMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT)) {
            //TODO 可以选择将消息保存到本地
        }*/

        //组装回复消息
        String content;
        HashMap<String,Object> params=new HashMap<>();
        params.put("message",wxMessage);
        params.put("flowDao",flowDao);
        params.put("petDao",petDao);
        JxFlow flow=flowDao.get(wxMessage.getFromUser());
        if(flow!=null){
            params.put("step",flow.getStep());
            content=this.execute(flow.getFlow(),params);
            return new TextBuilder().build(content, wxMessage, weixinService);
        }
        content=this.execute("general.groovy",params);
        //返回不为null且不是进入工作流
        if(content!=null&&!content.equals("flow")){
            return new TextBuilder().build(content, wxMessage, weixinService);
        }
        flow=flowDao.get(wxMessage.getFromUser());
        if(flow!=null){
            params.put("step",flow.getStep());
            content=this.execute(flow.getFlow(),params);
            return new TextBuilder().build(content, wxMessage, weixinService);
        }
        content=this.post(wxMessage);
        return new TextBuilder().build(content, wxMessage, weixinService);
    }

    private GroovyScriptEngine gse;

    private String execute(String scriptname,Map<String,Object> params){
        Binding binding  = new Binding();
        Object ret=null;
        for (Object o : params.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String key = (String) entry.getKey();
            Object val = entry.getValue();
            binding.setVariable(key,val);
        }
        try {
            ret=gse.run(scriptname,binding);
        } catch (ResourceException | ScriptException e) {
            e.printStackTrace();
        }
        return (String)ret;
    }

    private String post(WxMpXmlMessage wxMessage){
        JSONObject inputText=new JSONObject();
        JSONObject perception=new JSONObject();
        JSONObject userInfo=new JSONObject();
        JSONObject selfInfo=new JSONObject();
        inputText.put("text",wxMessage.getContent());
        userInfo.put("apiKey","74ca22e4b15a1fe5822adaeec945ca99");
        JxPet pet=petDao.get(wxMessage.getFromUser());
        int id=0;
        if(pet!=null){
            id=pet.getId();
        }
        userInfo.put("userId",id);
        perception.put("inputText",inputText);
        perception.put("selfInfo",selfInfo);
        JSONObject postbody=new JSONObject();
        postbody.put("perception",perception);
        postbody.put("userInfo",userInfo);
        RestTemplate restTemplate=new RestTemplate();
        Charset charset=Charset.forName("UTF-8");
        StringHttpMessageConverter stringHttpMessageConverter=new StringHttpMessageConverter(charset);
        List<HttpMessageConverter<?>> messageConverters=restTemplate.getMessageConverters();
        messageConverters.set(1,stringHttpMessageConverter);
        restTemplate.setMessageConverters(messageConverters);
        HttpHeaders header =new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request= new HttpEntity<>(postbody.toString(), header);
        String response= restTemplate.postForObject("http://openapi.tuling123.com/openapi/api/v2",request,String.class);
        JSONObject res=new JSONObject(response);
        JSONArray array=res.getJSONArray("results");
        String ret="";
        for(int i=array.length()-1;i>=0;i--){
            JSONObject tmp=array.getJSONObject(i);
            String type=tmp.getString("resultType");
            ret+=tmp.getJSONObject("values").getString(type);
        }
        return ret;
    }

}

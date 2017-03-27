import com.pinebud.application.wechat.pet.service.flow.JxFlow
import com.pinebud.application.wechat.pet.service.flow.JxFlowDao
import com.pinebud.application.wechat.pet.service.pet.JxPetDao
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

/**
 * Created by jiangyuesong on 2017/3/20 0020.
 */
// region binding variable define (shouldn't give variable type, it decided by binding!!)
message
flowDao
//endregion
String ret;
WxMpXmlMessage mes=(WxMpXmlMessage) message;
JxFlowDao fd=(JxFlowDao)flowDao;
switch (mes.getContent()){
    case "领养宠物":
        addFlow(mes.getFromUser(),"adoption.groovy",fd)
        ret="flow";
        break;
    default:
        ret=null;
        break;
}
return  ret;


def addFlow(String openid,String flowname,JxFlowDao dao){
    short step=0;
    JxFlow flow=new JxFlow(openid,flowname,step);
    dao.add(flow);
}
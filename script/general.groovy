import com.pinebud.application.wechat.pet.service.flow.Flow
import com.pinebud.application.wechat.pet.service.flow.Flow
import com.pinebud.application.wechat.pet.service.flow.FlowDao
import com.pinebud.application.wechat.pet.service.pet.PetDao
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
String ret
WxMpXmlMessage mes=(WxMpXmlMessage) message
FlowDao fd=(FlowDao)flowDao
switch (mes.getContent()){
    case "领养":
        addFlow(mes.getFromUser(),"adoption.groovy",fd)
        ret="flow"
        break
    default:
        ret=null
        break
}
return  ret


static def addFlow(String openid, String flowname, FlowDao dao){
    short step=0
    Flow flow=new Flow(openid,flowname,step)
    dao.add(flow)
}
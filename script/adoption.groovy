import com.pinebud.application.wechat.pet.service.flow.Flow
import com.pinebud.application.wechat.pet.service.flow.FlowDao
import com.pinebud.application.wechat.pet.service.pet.Pet
import com.pinebud.application.wechat.pet.service.pet.PetDao
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage

/**
 * Created by jiangyuesong on 2017/3/20 0020.
 */
message
flowDao
petDao
//endregion
String ret
WxMpXmlMessage mes=(WxMpXmlMessage) message
String openid=mes.getFromUser()
FlowDao fd=(FlowDao)flowDao
PetDao pd=(PetDao)petDao
Flow flow=fd.get(openid)
Pet pet=pd.get(openid)
switch (flow.getStep()){
    case 0:
        if(pet!=null){
            ret=pet.getNickname()+":"+"你已经有我啦，不要三心二意哟"
            fd.delete(flow.getOpenid())
        }else {
            pet=new Pet()
            pet.setOpenid(openid)
            pet.setPetbirth(new Date())
            pd.add(pet)
            short s=1
            flow.setStep(s)
            fd.set(flow)
            ret="宠物生成系统已经启动，请给我取个可爱的名字吧"
        }
        break
    case 1:
        pet.setPetname(mes.getContent())
        pd.set(pet)
        short s=2
        flow.setStep(s)
        fd.set(flow)
        ret="我有名字啦！那我称呼你什么好呢？主人？请告诉我吧"
        break
    case 2:
        pet.setNickname(mes.getContent())
        pd.set(pet)
        short s=3
        flow.setStep(s)
        fd.set(flow)
        ret=pet.nickname+":你希望我是男宝宝还是女宝宝呢，嘻嘻"
        break
    case 3:
        def genderstr=mes.getContent()
        short gender=0
        if(genderstr.contains("男")||genderstr.contains("公")){
            gender=1
        }
        pet.setPetgender(gender)
        pd.set(pet)
        fd.delete(flow.getOpenid())
        ret=pet.getNickname()+",谢谢你领养我，回复“领养规则”了解我吧"
        break
    default:
        ret=null
        break
}
return  ret
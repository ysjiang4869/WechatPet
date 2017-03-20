package com.pinebud.application.wechatsangzi.service.flow;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jiangyuesong on 2017/3/20 0020.
 */
@Entity
@Table(name = "flow")
public class JxFlow {
    @Id
    private String openid;
    private String flow;
    private short step;

    public JxFlow() {

    }

    public JxFlow(String openid, String flow, short step) {
        this.openid = openid;
        this.flow = flow;
        this.step = step;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public short getStep() {
        return step;
    }

    public void setStep(short step) {
        this.step = step;
    }
}

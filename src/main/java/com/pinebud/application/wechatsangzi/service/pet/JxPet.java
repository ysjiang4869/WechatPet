package com.pinebud.application.wechatsangzi.service.pet;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jiangyuesong on 2017/3/20 0020.
 */
@Entity
@Table(name = "member")
public class JxPet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String openid;
    private String petname;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date petbirth;
    private short petpower;
    private short petmood;
    private String nickname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    public Date getPetbirth() {
        return petbirth;
    }

    public void setPetbirth(Date petbirth) {
        this.petbirth = petbirth;
    }

    public short getPetpower() {
        return petpower;
    }

    public void setPetpower(short petpower) {
        this.petpower = petpower;
    }

    public short getPetmood() {
        return petmood;
    }

    public void setPetmood(short petmood) {
        this.petmood = petmood;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

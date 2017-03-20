package com.pinebud.application.wechatsangzi.service.conversation;

import javax.persistence.*;

/**
 * Created by jiangyuesong on 2017/3/20 0020.
 */
@Entity
@Table(name = "response")
public class JxResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String question;
    private String answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

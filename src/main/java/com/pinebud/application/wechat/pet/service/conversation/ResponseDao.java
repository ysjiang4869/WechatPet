package com.pinebud.application.wechat.pet.service.conversation;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jiangyuesong on 2017/3/20 0020.
 *
 */
@Service
public class ResponseDao {
    @PersistenceContext
    private EntityManager em;

    public List<Response> find() {
        return em.createQuery("from Response").getResultList();
    }


    public void add(Response res) {
        em.persist(res);
    }


    public Response get(String id) {
        return em.find(Response.class,id);
    }


    public void set(Response res) {
        em.merge(res);
    }


    public void delete(String id) {
        Response res=this.get(id);
        if(res!=null) {
            em.remove(res);
        }
    }
}

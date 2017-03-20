package com.pinebud.application.wechatsangzi.service.conversation;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jiangyuesong on 2017/3/20 0020.
 */
@Service
public class JxResDao {
    @PersistenceContext
    private EntityManager em;

    public List<JxResponse> find() {
        return em.createQuery("from JxResponse").getResultList();
    }


    public void add(JxResponse res) {
        em.persist(res);
    }


    public JxResponse get(String id) {
        return em.find(JxResponse.class,id);
    }


    public void set(JxResponse res) {
        em.merge(res);
    }


    public void delete(String id) {
        JxResponse res=this.get(id);
        if(res!=null) {
            em.remove(res);
        }
    }
}

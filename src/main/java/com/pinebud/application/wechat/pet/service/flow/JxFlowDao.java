package com.pinebud.application.wechat.pet.service.flow;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jiangyuesong on 2017/3/20 0020.
 *
 */
@Service
public class JxFlowDao {
    
    @PersistenceContext
    private EntityManager em;

    public List<JxFlow> find() {
        return em.createQuery("from JxFlow").getResultList();
    }


    public void add(JxFlow flow) {
        em.persist(flow);
    }


    public JxFlow get(String id) {
        return em.find(JxFlow.class,id);
    }


    public void set(JxFlow flow) {
        em.merge(flow);
    }


    public void delete(String id) {
        JxFlow flow=this.get(id);
        if(flow!=null) {
            em.remove(flow);
        }
    }
}

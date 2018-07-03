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
public class FlowDao {
    
    @PersistenceContext
    private EntityManager em;

    public List<Flow> find() {
        return em.createQuery("from Flow").getResultList();
    }


    public void add(Flow flow) {
        em.persist(flow);
    }


    public Flow get(String id) {
        return em.find(Flow.class,id);
    }


    public void set(Flow flow) {
        em.merge(flow);
    }


    public void delete(String id) {
        Flow flow=this.get(id);
        if(flow!=null) {
            em.remove(flow);
        }
    }
}

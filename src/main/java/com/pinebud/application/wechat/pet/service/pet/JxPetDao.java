package com.pinebud.application.wechat.pet.service.pet;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by jiangyuesong on 2017/3/20 0020.
 *
 */
@Service
public class JxPetDao {

    @PersistenceContext
    private EntityManager em;

    public List<JxPet> find() {
        return em.createQuery("from JxPet").getResultList();
    }

    public void add(JxPet pet) {
        em.persist(pet);
    }

    
    public JxPet get(String id) {
        Query query=em.createQuery("select p from JxPet p where p.openid=?1");
        query.setParameter(1,id);
        List result=query.getResultList();
        JxPet ret=null;
        if(result.size()==1){
            ret=(JxPet)result.get(0);
        }
        return ret;
    }

    
    public void set(JxPet pet) {
        em.merge(pet);
    }

    
    public void delete(String id) {
        JxPet pet=this.get(id);
        if(pet!=null) {
            em.remove(pet);
        }
    }
}

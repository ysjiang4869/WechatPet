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
public class PetDao {

    @PersistenceContext
    private EntityManager em;

    public List<Pet> find() {
        return em.createQuery("from Pet").getResultList();
    }

    public void add(Pet pet) {
        em.persist(pet);
    }

    
    public Pet get(String id) {
        Query query=em.createQuery("select p from Pet p where p.openid=?1");
        query.setParameter(1,id);
        List result=query.getResultList();
        Pet ret=null;
        if(result.size()==1){
            ret=(Pet)result.get(0);
        }
        return ret;
    }

    
    public void set(Pet pet) {
        em.merge(pet);
    }

    
    public void delete(String id) {
        Pet pet=this.get(id);
        if(pet!=null) {
            em.remove(pet);
        }
    }
}

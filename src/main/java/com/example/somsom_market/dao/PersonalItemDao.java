package com.example.somsom_market.dao;

import com.example.somsom_market.controller.PersonalItem.PersonalItemRequest;
import com.example.somsom_market.domain.ItemStatus;
import com.example.somsom_market.domain.item.Item;
import com.example.somsom_market.domain.item.PersonalItem;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PersonalItemDao {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public PersonalItem insertItem(PersonalItem personalItem) throws DataAccessException {
        em.persist(personalItem);
        return personalItem;
    }

    @Transactional
    public PersonalItem updateItem(PersonalItemRequest itemRegistReq) throws DataAccessException {
        PersonalItem personalItem = em.find(PersonalItem.class, itemRegistReq.getItemId());
        personalItem.setTitle(itemRegistReq.getTitle());
        personalItem.setPrice(itemRegistReq.getPrice());
        personalItem.setDescription(itemRegistReq.getDescription());
        if (itemRegistReq.getStatus().equals("거래가능")) {
            personalItem.setStatus(ItemStatus.INSTOCK);
        } else if (itemRegistReq.getStatus().equals("거래중")) {
            personalItem.setStatus(ItemStatus.ING);
        } else {
            personalItem.setStatus(ItemStatus.SOLDOUT);
        }

        return personalItem;
    }

    @Transactional
    public void updateItemSellerId(String sellerId, String newSellerId) {
        Query query = em.createNativeQuery("UPDATE Item i SET i.seller_id = :newSellerId WHERE i.seller_id = :sellerId");
        query.setParameter("newSellerId", newSellerId);
        query.setParameter("sellerId", sellerId);
        query.executeUpdate();
    }

    @Transactional
    public void deleteItem(PersonalItem personalItem) {
        if (em.contains(personalItem)) {
            em.remove(personalItem);
        } else {
            em.remove(em.merge(personalItem));
        }
    }

    public void updateAddItemWishcount(Long itemId) {
        Query query = em.createNativeQuery("Update Item i SET i.wish_count = i.wish_count+1 Where i.item_id = ?1");

        query.setParameter(1, itemId);
        query.executeUpdate();
    }

    public void updateDeleteItemWishcount(Long itemId) {
        Query query = em.createNativeQuery("Update Item i SET i.wish_count = i.wish_count-1 Where i.item_id = ?1");
        query.setParameter(1, itemId);
        query.executeUpdate();
    }

    public List<Item> searchItem(String word, String type) {
        Query query = em.createQuery("SELECT i FROM Item i WHERE i.dtype = :dtype AND i.title LIKE :word");
        query.setParameter("dtype", type);
        query.setParameter("word", "%"+word+"%");
        return query.getResultList();
    }

    public List<Item> searchAllItem(String word) {
        Query query = em.createQuery("SELECT i FROM Item i WHERE i.title LIKE :word");
        query.setParameter("word", "%"+word+"%");
        return query.getResultList();
    }
}

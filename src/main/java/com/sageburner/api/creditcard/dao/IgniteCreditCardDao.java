package com.sageburner.api.creditcard.dao;

import com.sageburner.api.creditcard.dao.jdbc.CacheJdbcStore;
import com.sageburner.api.creditcard.model.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IgniteCreditCardDao {

    @Autowired
    private CacheJdbcStore cacheJdbcStore;

    public CreditCard createCreditCard(CreditCard creditCard) {
        return cacheJdbcStore.createCreditCard(creditCard);
    }

    public CreditCard getCreditCard(String number) {
        return cacheJdbcStore.getCreditCard(number);
    }

    public List<CreditCard> getCreditCardsExpiring(String preposition, String month, String year)  {
        return cacheJdbcStore.getCreditCardsExpiring(preposition, month, year);
    }

    public List<CreditCard> getCreditCardList()  {
        return cacheJdbcStore.getCreditCardList();
    }

    public Double calculateAverge(List<List<Double>> numLists) {
        return cacheJdbcStore.calculateAverge(numLists);
    }
}

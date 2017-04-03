package com.sageburner.api.creditcard.service;

import com.sageburner.api.creditcard.dao.IgniteCreditCardDao;
import com.sageburner.api.creditcard.model.CreditCard;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IgniteCreditCardServiceImpl implements IgniteCreditCardService {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private IgniteCreditCardDao creditCardDao;

    @Override
    public CreditCard createCreditCard(CreditCard creditCard) {
        return creditCardDao.createCreditCard(creditCard);
    }

    @Override
    public CreditCard getCreditCard(String number) {
        return creditCardDao.getCreditCard(number);
    }

    @Override
    public List<CreditCard> getCreditCardsExpiring(String preposition, String month, String year) {
        return creditCardDao.getCreditCardsExpiring(preposition, month, year);
    }

    @Override
    public List<CreditCard> getCreditCardList() {
        return creditCardDao.getCreditCardList();
    }

    @Override
    public Double calculateAverge(List<List<Double>> numLists) {
        return creditCardDao.calculateAverge(numLists);
    }
}

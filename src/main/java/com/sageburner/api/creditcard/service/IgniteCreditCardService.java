package com.sageburner.api.creditcard.service;

import com.sageburner.api.creditcard.model.CreditCard;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IgniteCreditCardService {
    CreditCard createCreditCard(CreditCard creditCard);
    CreditCard getCreditCard(String number);
    List<CreditCard> getCreditCardsExpiring(String preposition, String month, String year);
    List<CreditCard> getCreditCardList();

    Double calculateAverge(List<List<Double>> numLists);
}

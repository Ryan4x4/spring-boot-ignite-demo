package com.sageburner.api.creditcard.controler;

import com.sageburner.api.creditcard.model.CreditCard;
import com.sageburner.api.creditcard.service.IgniteCreditCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Created by Ryan Holt on 10/23/2015.
 */
@Controller
@RequestMapping("/ignite/creditcard")
@Api(value = "ignitecreditcard", description = "Operations on a Credit Card")
public class IgniteCreditCardController {

    @Autowired
    private IgniteCreditCardService igniteCreditCardService;

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(
            value = "Create a Credit Card", notes = "Returns and stores a Credit Card based on the input JSON String",
            response = CreditCard.class)
    @ApiResponses(value = {@ApiResponse(code = 405, message = "Invalid input")})
    public @ResponseBody
    ResponseEntity<CreditCard> createCreditCard(@RequestBody CreditCard creditCard){
        CreditCard result = igniteCreditCardService.createCreditCard(creditCard);

        ResponseEntity response;

        //do some kind of validation/error handling
        if (result != null) {
            response = new ResponseEntity(result, HttpStatus.CREATED);
            return response;
        } else {
            response = new ResponseEntity(creditCard, HttpStatus.CREATED);
            return response;
        }
    }

    @RequestMapping(method = RequestMethod.GET, params="number")
    @ApiOperation(
            value = "Get Credit Card ", notes = "Returns a credit card in the database based on the given number",
            response = CreditCard.class)
    public @ResponseBody ResponseEntity<CreditCard> findCreditCard(@RequestParam("number")  String number){
        CreditCard creditCard;

        //do some kind of validation/error handling
        if (StringUtils.isEmpty(number)) {
            return null;
        } else {
            creditCard = igniteCreditCardService.getCreditCard(number);
        }

        //do some kind of validation/error handling
        if (creditCard != null) {
            return ResponseEntity.ok(creditCard);
        } else {
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.GET, params={"preposition", "month", "year"})
    @ApiOperation(
            value = "Get Credit Card", notes = "Returns a list of all credit cards expiring before or after a month and year",
            response = CreditCard.class)
    public @ResponseBody
    ResponseEntity<List<CreditCard>> getCreditCardsExpiring(@RequestParam("preposition")  String preposition,
                                                       @RequestParam("month")  String month,
                                                       @RequestParam("year")  String year) {
        return ResponseEntity.ok(igniteCreditCardService.getCreditCardsExpiring(preposition, month, year));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get Credit Card List", notes = "Returns a list of all credit cards stored in the database",
            response = CreditCard.class)
    public @ResponseBody
    ResponseEntity<List<CreditCard>> getCreditCardList(){
        return ResponseEntity.ok(igniteCreditCardService.getCreditCardList());
    }

    @RequestMapping(value = "/compute/average", method = RequestMethod.POST)
    @ApiOperation(
            value = "Calculate average of all numbers", notes = "Returns the average of all the Doubles in the nested lists",
            response = Integer.class)
    public @ResponseBody
    ResponseEntity<String> calculateAverge(@RequestBody List<List<Double>> numLists){
        return ResponseEntity.ok(String.valueOf(igniteCreditCardService.calculateAverge(numLists)));
    }
}

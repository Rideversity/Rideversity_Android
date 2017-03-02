package cab.com.rideversitydriver.Models;

import java.util.ArrayList;

/**
 * Created by Kalidoss on 30/08/16.
 */
public class PaymentModel {

    String message,result;
    public ArrayList<CardModel> cardLists = new ArrayList<CardModel>();

    public ArrayList<CardModel> getCardLists() {
        return cardLists;
    }

    public void setCardLists(ArrayList<CardModel> cardLists) {
        this.cardLists = cardLists;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

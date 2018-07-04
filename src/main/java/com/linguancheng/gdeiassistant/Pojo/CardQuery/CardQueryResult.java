package com.gdeiassistant.gdeiassistant.Pojo.CardQuery;

import com.gdeiassistant.gdeiassistant.Enum.Base.ServiceResultEnum;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.Card;
import com.gdeiassistant.gdeiassistant.Pojo.Entity.CardInfo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class CardQueryResult {

    private CardQuery cardQuery;

    private ServiceResultEnum cardServiceResultEnum;

    private CardInfo cardInfo;

    private List<Card> cardList;

    public CardQuery getCardQuery() {
        return cardQuery;
    }

    public void setCardQuery(CardQuery cardQuery) {
        this.cardQuery = cardQuery;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public ServiceResultEnum getCardServiceResultEnum() {
        return cardServiceResultEnum;
    }

    public void setCardServiceResultEnum(ServiceResultEnum cardServiceResultEnum) {
        this.cardServiceResultEnum = cardServiceResultEnum;
    }

    public List<Card> getCardList() {
        return cardList;
    }

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }
}
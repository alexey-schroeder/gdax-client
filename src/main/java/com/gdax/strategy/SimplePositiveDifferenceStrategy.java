package com.gdax.strategy;

import javafx.util.Pair;
import lombok.Setter;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SimplePositiveDifferenceStrategy {

    @Value("${strategy.simple.minProfit}")
    private double minProfit;

    public List<Pair<UserTrade, BigDecimal>> calculate(Ticker ticker, UserTrades userTrades) {
        final BigDecimal lastPrice = ticker.getLast();
        return userTrades.getUserTrades()
                .stream()
                .filter(userTrade -> userTrade.getType() == Order.OrderType.BID)
                .filter(userTrade -> canBeSold(userTrade, lastPrice))
                .map(userTrade -> new Pair<>(userTrade, getProfit(lastPrice, userTrade)))
                .collect(Collectors.toList());

    }

    private boolean canBeSold(UserTrade userTrade, BigDecimal lastPrice) {
        return getProfit(lastPrice, userTrade).doubleValue() > minProfit;
    }

    private BigDecimal getProfit(BigDecimal lastPrice, UserTrade userTrade) {
        return userTrade.getOriginalAmount().multiply(lastPrice.subtract(userTrade.getPrice()));
    }
}

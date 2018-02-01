package com.gdax.client;

import com.gdax.client.listener.TickerChangeListener;
import com.gdax.client.listener.UserTradesChangeListener;
import lombok.Getter;
import lombok.Setter;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.UserTrades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExchangeDataSource {
    @Autowired
    private ExchangeClient exchangeClient;

    private List<TickerChangeListener> tickerChangeListeners = new ArrayList<>();
    private List<UserTradesChangeListener> userTradesChangeListeners = new ArrayList<>();

    public void addTickerChangeListener(TickerChangeListener tickerChangeListener) {
        tickerChangeListeners.add(tickerChangeListener);
    }

    public void addUserTradesChangeListener(UserTradesChangeListener userTradesChangeListener) {
        userTradesChangeListeners.add(userTradesChangeListener);
    }

    @Scheduled(fixedRate = 5000)
    public void refreshTicker() throws IOException {
        Ticker ticker = exchangeClient.getTicker(CurrencyPair.ETH_EUR);
        tickerChangeListeners
                .stream()
                .forEach(tickerChangeListener -> tickerChangeListener.onTickerChange(ticker));
    }

    @Scheduled(fixedRate = 15000)
    public void refreshUserTrades() throws IOException {
        final UserTrades userTrades = exchangeClient.getUserTrades();
        userTradesChangeListeners
                .stream()
                .forEach(userTradesChangeListener -> userTradesChangeListener.onUserTradesChange(userTrades));
    }
}

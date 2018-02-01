package com.gdax.strategy;

import com.gdax.client.listener.TickerChangeListener;
import com.gdax.client.listener.UserTradesChangeListener;
import javafx.application.Platform;
import javafx.util.Pair;
import org.controlsfx.control.Notifications;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

@Component
public class SimpleStrategyTickerChangeListener implements TickerChangeListener, UserTradesChangeListener {

    private UserTrades userTrades;
    private Ticker ticker;

    @Autowired
    private SimplePositiveDifferenceStrategy differenceStrategy;

    @Override
    public void onTickerChange(Ticker ticker) {
        this.ticker = ticker;
        handleChanges(ticker, userTrades);
    }

    private void showNotificationWindow(Pair<UserTrade, BigDecimal> tradeForSale) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        Platform.runLater(() -> {
            final UserTrade trade = tradeForSale.getKey();
            Notifications.create()
                    .title("Man kann verdienen " + df.format(tradeForSale.getValue().doubleValue()) + "€")
                    .text("altes Price " + df.format(trade.getPrice().doubleValue()) + ", amount " + trade.getOriginalAmount())
                    .showWarning();
        });
    }

    @Override
    public void onUserTradesChange(UserTrades userTrades) {
        this.userTrades = userTrades;
        handleChanges(ticker, userTrades);
    }

    private void handleChanges(Ticker ticker, UserTrades userTrades) {
        if (ticker != null && userTrades != null) {
            final List<Pair<UserTrade, BigDecimal>> userTradesForSale = differenceStrategy.calculate(ticker, userTrades);
            userTradesForSale.stream().forEach(pair -> showNotificationWindow(pair));
        }
    }
}

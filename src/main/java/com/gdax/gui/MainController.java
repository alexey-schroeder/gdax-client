package com.gdax.gui;

import com.gdax.client.ExchangeClient;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import org.controlsfx.control.Notifications;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MainController {
    public TextField sellPriceField;

    public TextField buyPriceField;

    @FXML
    private Label sellPrice;

    @FXML
    private Label spreadPrice;

    @FXML
    private Label buyPrice;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    private ExchangeClient exchangeClient;

    @PostConstruct
    public void init() throws IOException {
        final UserTrades userTrades = exchangeClient.getUserTrades();
        for (UserTrade userTrade : userTrades.getUserTrades()) {
            System.out.println(userTrade);
        }
        scheduler.scheduleAtFixedRate(() -> setPrices(), 0, 10, SECONDS);
    }

    private void setPrices() {
        final Ticker ticker;
        try {
            ticker = exchangeClient.getTicker(CurrencyPair.ETH_EUR);
            setPrices(ticker);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPrices(Ticker ticker) {
        Platform.runLater(() -> {
            String unknownPrice = "unknown";
            String newBuyPrice;
            String newSellPrice;
            if (ticker == null) {
                newBuyPrice = unknownPrice;
                newSellPrice = unknownPrice;
            } else {
                newBuyPrice = String.valueOf(ticker.getAsk().doubleValue());
                newSellPrice = String.valueOf(ticker.getBid().doubleValue());
            }
            buyPrice.setText(newBuyPrice);
            onBuyPriceChanged(newBuyPrice);

            sellPrice.setText(newSellPrice);
            onSellPriceChanged(newSellPrice);

            if (ticker == null) {
                spreadPrice.setText(unknownPrice);
            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.CEILING);
                spreadPrice.setText(df.format(ticker.getAsk().doubleValue() - ticker.getBid().doubleValue()));
            }
        });
    }

    private void onBuyPriceChanged(String newValue) {
        String greatPrice = sellPriceField.getText();
        if (!greatPrice.trim().isEmpty() && newValue != null) {
            Double graitPriceDouble = Double.parseDouble(greatPrice);
            if (Double.parseDouble(newValue) >= graitPriceDouble) {
                Platform.runLater(() -> {
                    Notifications.create()
                            .title("Neuer großer Verkauf Prise!")
                            .text(newValue)
                            .showWarning();
                });
            }
        }
    }

    private void onSellPriceChanged(String newValue) {
        String smallPrice = buyPriceField.getText();
        if (!smallPrice.trim().isEmpty() && newValue != null) {
            Double smallPriceDouble = Double.parseDouble(smallPrice);
            if (Double.parseDouble(newValue) <= smallPriceDouble) {
                Platform.runLater(() -> {
                    Notifications.create()
                            .title("Neuer nidrieger Kauf Prise!")
                            .text(newValue)
                            .showWarning();
                });
            }
        }
    }

    public void stop() {
        scheduler.shutdown();
    }
}

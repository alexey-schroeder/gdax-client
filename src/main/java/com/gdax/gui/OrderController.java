package com.gdax.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.knowm.xchange.dto.trade.UserTrade;

public class OrderController {

    @FXML
    private Label dateLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label amountLabel;

    @FXML
    private Label typeLabel;

    private UserTrade userTrade;

    public OrderController(UserTrade userTrade) {
        this.userTrade = userTrade;
    }

    public void init() {
        dateLabel.setText(userTrade.getTimestamp().toString());
        priceLabel.setText(String.valueOf(userTrade.getPrice().doubleValue()));
        amountLabel.setText(String.valueOf(userTrade.getOriginalAmount()));
        typeLabel.setText(userTrade.getType().toString());
    }
}

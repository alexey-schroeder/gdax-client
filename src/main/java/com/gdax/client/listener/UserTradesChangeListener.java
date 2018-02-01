package com.gdax.client.listener;

import org.knowm.xchange.dto.trade.UserTrades;

public interface UserTradesChangeListener {
    void onUserTradesChange(UserTrades userTrades);
}

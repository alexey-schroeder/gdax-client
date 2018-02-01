package com.gdax.client.listener;

import org.knowm.xchange.dto.marketdata.Ticker;

public interface TickerChangeListener {
    void onTickerChange(Ticker ticker);
}

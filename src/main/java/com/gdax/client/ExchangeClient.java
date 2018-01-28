package com.gdax.client;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.gdax.GDAXExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsAll;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class ExchangeClient

{
	@Value("${api.key}")
	String apiKey;

	@Value("${secret.key}")
	String secretKey;

	@Value("${passphrase}")
	String passphrase;

	Exchange exchange;

	MarketDataService marketDataService;

	@PostConstruct
	private void init(){
		final ExchangeSpecification defaultExchangeSpecification = new GDAXExchange().getDefaultExchangeSpecification();
		defaultExchangeSpecification.setApiKey(apiKey);
		defaultExchangeSpecification
			.setSecretKey(secretKey);
		defaultExchangeSpecification.setExchangeSpecificParametersItem("passphrase", passphrase);
		exchange = ExchangeFactory.INSTANCE.createExchange(defaultExchangeSpecification);
		marketDataService = exchange.getMarketDataService();
	}

	public Ticker getTicker(CurrencyPair currencyPair) throws IOException
	{

		return marketDataService.getTicker(currencyPair, 1);
	}

	public UserTrades getUserTrades() throws IOException
	{
		return exchange.getTradeService().getTradeHistory(new TradeHistoryParamsAll());
	}
}

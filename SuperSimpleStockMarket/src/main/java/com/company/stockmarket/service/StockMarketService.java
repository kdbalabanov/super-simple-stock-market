package main.java.com.company.stockmarket.service;

import main.java.com.company.stockmarket.exceptions.StockMarketError;
import main.java.com.company.stockmarket.model.Stock;
import main.java.com.company.stockmarket.model.TradeLedger;
import main.java.com.company.stockmarket.model.TradeRecord;
import main.java.com.company.stockmarket.model.TradeSimulator;
import main.java.com.company.stockmarket.utils.AnalyticsProvider;
import main.java.com.company.stockmarket.utils.StockType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public class StockMarketService {
    private static StockMarketService instance;
    private TradeLedger tradeLedger;
    private TradeSimulator tradeSimulator;

    public static StockMarketService getInstance() {
        if (instance == null) {
            instance = new StockMarketService();
        }
        return instance;
    }

    public void run() throws StockMarketError {
        init();
        displayLedger();
    }

    private void init() {
        this.tradeLedger = new TradeLedger();
        this.tradeSimulator = new TradeSimulator(tradeLedger, 0.01, 5.00, 1, 100000);

        initialiseSampleStocks();
        tradeSimulator.generateTrades(5);
    }

    private void initialiseSampleStocks() {
        Stock stockTEA = new Stock("TEA", StockType.COMMON, BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(1.00));
        Stock stockPOP = new Stock("POP", StockType.COMMON, BigDecimal.valueOf(0.08), BigDecimal.valueOf(0), BigDecimal.valueOf(1.00));
        Stock stockALE = new Stock("ALE", StockType.COMMON, BigDecimal.valueOf(0.23), BigDecimal.valueOf(0), BigDecimal.valueOf(0.60));
        Stock stockGIN = new Stock("GIN", StockType.PREFERRED, BigDecimal.valueOf(0.08), BigDecimal.valueOf(1.02), BigDecimal.valueOf(1.00));
        Stock stockJOE = new Stock("JOE", StockType.COMMON, BigDecimal.valueOf(0.13), BigDecimal.valueOf(0), BigDecimal.valueOf(1.00));

        tradeLedger.registerStock(stockTEA);
        tradeLedger.registerStock(stockPOP);
        tradeLedger.registerStock(stockALE);
        tradeLedger.registerStock(stockGIN);
        tradeLedger.registerStock(stockJOE);
    }

    private void displayLedger() {
        Map<String, List<TradeRecord>> trades  = tradeLedger.getTrades();
        for (Map.Entry<String, List<TradeRecord>> tradeRecords : trades.entrySet()) {
            for (TradeRecord tradeRecord : tradeRecords.getValue()) {
                String stockSymbol = tradeRecord.getStockSymbol();
                Stock stock = tradeLedger.getRegisteredStocks().get(stockSymbol);
                BigDecimal stockPrice = tradeRecord.getTradePrice();
                BigDecimal dividendYield = AnalyticsProvider.calculateDividendYield(stock, stockPrice);
                BigDecimal priceEarningsRatio = AnalyticsProvider.calculatePriceEarningsRatio(stock, stockPrice);
                BigDecimal volumeWeightedPrice = AnalyticsProvider.calculateVolumeWeighthedStockPrice(tradeLedger.getTradesForStockSymbol(stockSymbol));

                System.out.println("Trade Record for " + stockSymbol + " - Price: " + tradeRecord.getTradePrice() +
                        " Quantity: " + tradeRecord.getNumShares() + " Dividend Yield: " + dividendYield +
                        " Price Earnings Ratio: " + priceEarningsRatio + " Volume Weighted Price: " + volumeWeightedPrice);
            }
        }
        System.out.println("All Shares Index: " + AnalyticsProvider.calculateGeometricMean(tradeLedger.getAllPrices()));
    }
}

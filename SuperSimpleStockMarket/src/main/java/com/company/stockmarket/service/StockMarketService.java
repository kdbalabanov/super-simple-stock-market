package main.java.com.company.stockmarket.service;

import main.java.com.company.stockmarket.core.*;
import main.java.com.company.stockmarket.utils.AnalyticsProvider;
import main.java.com.company.stockmarket.utils.StockType;

import java.math.BigDecimal;

/**
 * The StockMarketService glues everything together and runs it
 * It is a Singleton - initialised only once
 *
 * The following assumptions are made:
 * 1. There is no user input
 * 2. Trades are randomly generated for all specified stocks
 * 3. There is the possibility for calculations with really high values, hence the use of
 * BigDecimal and BigInteger
 */
public class StockMarketService {
    private static StockMarketService instance;
    private final TradeLedger TRADE_LEDGER;
    private final TradeSimulator TRADE_SIMULATOR;

    // The minimum used for random price generation
    private final double MIN_PRICE = 0.01;
    // The maximum used for random price generation
    private final double MAX_PRICE = 25;
    // The minimum used for random number of shares generation
    private final long MIN_NUM_SHARES = 1;
    // The maximum used for random number of shares generation
    private final long MAX_NUM_SHARES = 100000;

    private StockMarketService() {
        this.TRADE_LEDGER = new TradeLedger();
        this.TRADE_SIMULATOR = new TradeSimulator(TRADE_LEDGER, MIN_PRICE, MAX_PRICE, MIN_NUM_SHARES, MAX_NUM_SHARES);
    }

    public static synchronized StockMarketService getInstance() {
        if (instance == null) {
            instance = new StockMarketService();
        }
        return instance;
    }

    public void run() {
        init();
        TRADE_SIMULATOR.generateTrades(5);
        System.out.println("All Shares Index: " + AnalyticsProvider.calculateGeometricMean(TRADE_LEDGER.getAllPrices()));
    }

    /**
     * Initialise the sample stocks mentioned in the requirements
     */
    private void init() {
        Stock stockTEA = new CommonStock("TEA", StockType.COMMON, BigDecimal.valueOf(0), BigDecimal.valueOf(1.00));
        Stock stockPOP = new CommonStock("POP", StockType.COMMON, BigDecimal.valueOf(0.08), BigDecimal.valueOf(1.00));
        Stock stockALE = new CommonStock("ALE", StockType.COMMON, BigDecimal.valueOf(0.23), BigDecimal.valueOf(0.60));
        Stock stockGIN = new PreferredStock("GIN", StockType.PREFERRED, BigDecimal.valueOf(0.08), BigDecimal.valueOf(1.02), BigDecimal.valueOf(1.00));
        Stock stockJOE = new CommonStock("JOE", StockType.COMMON, BigDecimal.valueOf(0.13), BigDecimal.valueOf(1.00));

        TRADE_LEDGER.registerStock(stockTEA);
        TRADE_LEDGER.registerStock(stockPOP);
        TRADE_LEDGER.registerStock(stockALE);
        TRADE_LEDGER.registerStock(stockGIN);
        TRADE_LEDGER.registerStock(stockJOE);
    }

}

package test.java.com.company.stockmarket.service;

import main.java.com.company.stockmarket.core.*;
import main.java.com.company.stockmarket.utils.AnalyticsProvider;
import main.java.com.company.stockmarket.utils.StockType;
import main.java.com.company.stockmarket.utils.TradeType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static org.junit.Assert.*;

public class StockMarketServiceTest {

    private Stock commonStock;
    private final String commonStockSymbol = "CommonStockSymbol";
    private final BigDecimal commonStockLastDividend = BigDecimal.valueOf(5);
    private final BigDecimal commonStockParValue = BigDecimal.valueOf(100);

    private Stock preferredStock;
    private final String preferredStockSymbol = "PreferredStockSymbol";
    private final BigDecimal preferredStockLastDividend = BigDecimal.valueOf(8);
    private final BigDecimal preferredStockFixedDividend = BigDecimal.valueOf(1.02);
    private final BigDecimal preferredStockParValue = BigDecimal.valueOf(150);

    private TradeRecord commonStockTradeRecord;
    private final BigDecimal commonStockTradeRecordPrice = BigDecimal.valueOf(735);
    private final BigInteger commonStockTradeRecordNumShares = BigInteger.valueOf(350);

    private TradeRecord preferredStockTradeRecord;
    private final BigDecimal preferredStockTradeRecordPrice = BigDecimal.valueOf(800);
    private final BigInteger preferredStockTradeRecordNumShares = BigInteger.valueOf(1000);

    private TradeLedger tradeLedger;
    private TradeSimulator tradeSimulator;

    @Before
    public void setup() {
        commonStock = new CommonStock(commonStockSymbol, StockType.COMMON, commonStockLastDividend, commonStockParValue);
        preferredStock = new PreferredStock(preferredStockSymbol, StockType.PREFERRED, preferredStockLastDividend, preferredStockFixedDividend, preferredStockParValue);

        commonStockTradeRecord = new TradeRecord(commonStockSymbol, commonStockTradeRecordPrice, commonStockTradeRecordNumShares, TradeType.BUY);
        preferredStockTradeRecord = new TradeRecord(preferredStockSymbol, preferredStockTradeRecordPrice, preferredStockTradeRecordNumShares, TradeType.SELL);

        tradeLedger = new TradeLedger();
        tradeLedger.registerStock(commonStock);
        tradeLedger.registerStock(preferredStock);

        tradeSimulator = new TradeSimulator(tradeLedger, 0.01, 1000.00, 1, 10000);
    }

    @After
    public void cleanUp() {
        commonStock = null;
        preferredStock = null;

        commonStockTradeRecord = null;
        preferredStockTradeRecord = null;

        tradeLedger.wipeTradeLedger();
        tradeLedger = null;
    }

    @Test
    public void testStockInitialization() {
        assertEquals(commonStockSymbol, commonStock.getStockSymbol());
        assertEquals(preferredStockSymbol, preferredStock.getStockSymbol());

        assertEquals(StockType.COMMON, commonStock.getStockType());
        assertEquals(StockType.PREFERRED, preferredStock.getStockType());

        assertEquals(commonStockLastDividend, commonStock.getLastDividend());
        assertEquals(commonStockParValue, commonStock.getParValue());

        assertEquals(preferredStockLastDividend, preferredStock.getLastDividend());
        assertEquals(preferredStockParValue, preferredStock.getParValue());
    }

    @Test
    public void testCalculateDividendYield() {
        BigDecimal commonStockExpectedResult = commonStockLastDividend.divide(commonStockTradeRecordPrice, AnalyticsProvider.SCALE * 3, RoundingMode.HALF_UP);
        assertEquals(commonStockExpectedResult, commonStock.calculateDividendYield(commonStockTradeRecordPrice));

        BigDecimal preferredStockExpectedResult = (preferredStockFixedDividend.multiply(preferredStockParValue)).divide(preferredStockTradeRecordPrice, AnalyticsProvider.SCALE * 3, RoundingMode.HALF_UP);
        assertEquals(preferredStockExpectedResult, preferredStock.calculateDividendYield(preferredStockTradeRecordPrice));
    }

    @Test
    public void testCalculatePriceEarningsRatio() {
        BigDecimal commonStockExpectedResult = commonStockTradeRecordPrice.divide(commonStockLastDividend, AnalyticsProvider.SCALE * 3, RoundingMode.HALF_UP);
        assertEquals(commonStockExpectedResult, commonStock.calculatePriceEarningsRatio(commonStockTradeRecordPrice));

        BigDecimal preferredStockExpectedResult = preferredStockTradeRecordPrice.divide(preferredStockLastDividend, AnalyticsProvider.SCALE * 3, RoundingMode.HALF_UP);
        assertEquals(preferredStockExpectedResult, preferredStock.calculatePriceEarningsRatio(preferredStockTradeRecordPrice));
    }

    @Test
    public void testTradeRecordInitialization() {
        assertEquals(commonStockSymbol, commonStockTradeRecord.getStockSymbol());
        assertEquals(preferredStockSymbol, preferredStockTradeRecord.getStockSymbol());

        assertEquals(StockType.COMMON, commonStock.getStockType());
        assertEquals(StockType.PREFERRED, preferredStock.getStockType());

        assertEquals(commonStockTradeRecordPrice, commonStockTradeRecord.getTradePrice());
        assertEquals(commonStockTradeRecordNumShares, commonStockTradeRecord.getNumShares());

        assertEquals(preferredStockTradeRecordPrice, preferredStockTradeRecord.getTradePrice());
        assertEquals(preferredStockTradeRecordNumShares, preferredStockTradeRecord.getNumShares());
    }

    @Test
    public void testTradeLedgerInitialization() {
        tradeLedger.addTrade(commonStockTradeRecord);
        assertTrue(tradeLedger.getTrades().containsKey(commonStockSymbol));

        tradeLedger.addTrade(preferredStockTradeRecord);
        assertTrue(tradeLedger.getTrades().containsKey(preferredStockSymbol));

        assertEquals(2, tradeLedger.getTrades().size());
        assertEquals(1, tradeLedger.getTradesForStockSymbol(commonStockSymbol).size());
        assertEquals(1, tradeLedger.getTradesForStockSymbol(preferredStockSymbol).size());
    }

    @Test
    public void testTradeLedgerAddTrade() {
        TradeRecord tradeRecordA = new TradeRecord(commonStockSymbol, BigDecimal.valueOf(500), BigInteger.valueOf(5000), TradeType.BUY);
        tradeLedger.addTrade(tradeRecordA);
        assertEquals(1, tradeLedger.getTradesForStockSymbol(commonStockSymbol).size());

        TradeRecord tradeRecordB = new TradeRecord(preferredStockSymbol, BigDecimal.valueOf(777), BigInteger.valueOf(15000), TradeType.BUY);
        tradeLedger.addTrade(tradeRecordB);
        assertEquals(1, tradeLedger.getTradesForStockSymbol(preferredStockSymbol).size());
    }

    @Test
    public void testTradeLedgerRegisterStock() {
        assertTrue(tradeLedger.isStockRegistered(commonStockSymbol));
        assertTrue(tradeLedger.isStockRegistered(preferredStockSymbol));
        assertFalse(tradeLedger.isStockRegistered("RandomStockSymbol"));
    }

    @Test
    public void testTradeSimulatorGenerateTrades() {
        tradeSimulator.generateTrades(100);
        assertEquals(100, tradeLedger.getTradesForStockSymbol(commonStockSymbol).size());
        assertEquals(100, tradeLedger.getTradesForStockSymbol(preferredStockSymbol).size());
    }

}

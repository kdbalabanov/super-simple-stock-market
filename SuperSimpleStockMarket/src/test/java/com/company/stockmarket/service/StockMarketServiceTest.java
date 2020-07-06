package test.java.com.company.stockmarket.service;

import main.java.com.company.stockmarket.model.*;
import main.java.com.company.stockmarket.utils.StockType;
import main.java.com.company.stockmarket.utils.TradeType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class StockMarketServiceTest {

    private Stock commonStock;
    private final String commonStockSymbol = "CommonStockSymbol";
    private final BigDecimal commonStockLastDividend = BigDecimal.valueOf(5);
    private final BigDecimal commonStockFixedDividend = BigDecimal.valueOf(0);
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

    @Before
    public void setup() {
        commonStock = new Stock(commonStockSymbol, StockType.COMMON, commonStockLastDividend, commonStockFixedDividend, commonStockParValue);
        preferredStock = new Stock(preferredStockSymbol, StockType.PREFERRED, preferredStockLastDividend, preferredStockFixedDividend, preferredStockParValue);

        commonStockTradeRecord = new TradeRecord(commonStockSymbol, commonStockTradeRecordPrice, commonStockTradeRecordNumShares, TradeType.BUY);
        preferredStockTradeRecord = new TradeRecord(preferredStockSymbol, preferredStockTradeRecordPrice, preferredStockTradeRecordNumShares, TradeType.SELL);

        tradeLedger = new TradeLedger();
        tradeLedger.addTrade(commonStockTradeRecord);
        tradeLedger.addTrade(preferredStockTradeRecord);
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
        assertEquals(commonStockFixedDividend, commonStock.getFixedDividend());
        assertEquals(commonStockParValue, commonStock.getParValue());

        assertEquals(preferredStockLastDividend, preferredStock.getLastDividend());
        assertEquals(preferredStockFixedDividend, preferredStock.getFixedDividend());
        assertEquals(preferredStockParValue, preferredStock.getParValue());
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
        assertTrue(tradeLedger.getTrades().containsKey(commonStockSymbol));
        assertTrue(tradeLedger.getTrades().containsKey(preferredStockSymbol));

        assertEquals(2, tradeLedger.getTrades().size());
        assertEquals(1, tradeLedger.getTradesForStockSymbol(commonStockSymbol).size());
        assertEquals(1, tradeLedger.getTradesForStockSymbol(preferredStockSymbol).size());
    }

    @Test
    public void testTradeLedgerAddTrade() {
        TradeRecord tradeRecordA = new TradeRecord(commonStockSymbol, BigDecimal.valueOf(500), BigInteger.valueOf(5000), TradeType.BUY);
        tradeLedger.addTrade(tradeRecordA);
        assertEquals(2, tradeLedger.getTradesForStockSymbol(commonStockSymbol).size());

        Stock stockA = new Stock("MSFT", StockType.COMMON, BigDecimal.valueOf(100), BigDecimal.valueOf(1.15), BigDecimal.valueOf(175));
        TradeRecord tradeRecordB = new TradeRecord(stockA.getStockSymbol(), BigDecimal.valueOf(777), BigInteger.valueOf(15000), TradeType.BUY);
        tradeLedger.addTrade(tradeRecordB);
        assertEquals(3, tradeLedger.getTrades().size());
    }

}

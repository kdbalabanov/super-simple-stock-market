package test.java.com.company.stockmarket.service;

import main.java.com.company.stockmarket.model.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

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
    private final long commonStockTradeRecordNumShares = 340;

    private TradeRecord preferredStockTradeRecord;
    private final BigDecimal preferredStockTradeRecordPrice = BigDecimal.valueOf(800);
    private final long preferredStockTradeRecordNumShares = 500;

    private TradeHistory tradeHistory;

    @Before
    public void setup() {
        commonStock = new Stock(commonStockSymbol, StockType.COMMON, commonStockLastDividend, commonStockFixedDividend, commonStockParValue);
        preferredStock = new Stock(preferredStockSymbol, StockType.PREFERRED, preferredStockLastDividend, preferredStockFixedDividend, preferredStockParValue);

        commonStockTradeRecord = new TradeRecord(commonStock, commonStockTradeRecordPrice, commonStockTradeRecordNumShares, TradeType.BUY);
        preferredStockTradeRecord = new TradeRecord(preferredStock, preferredStockTradeRecordPrice, preferredStockTradeRecordNumShares, TradeType.SELL);

        tradeHistory = new TradeHistory();
        tradeHistory.addTrade(commonStockTradeRecord);
        tradeHistory.addTrade(preferredStockTradeRecord);
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
        assertEquals(commonStockSymbol, commonStockTradeRecord.getStock().getStockSymbol());
        assertEquals(preferredStockSymbol, preferredStockTradeRecord.getStock().getStockSymbol());

        assertEquals(StockType.COMMON, commonStock.getStockType());
        assertEquals(StockType.PREFERRED, preferredStock.getStockType());

        assertEquals(commonStockTradeRecordPrice, commonStockTradeRecord.getTradePrice());
        assertEquals(commonStockTradeRecordNumShares, commonStockTradeRecord.getNumShares());

        assertEquals(preferredStockTradeRecordPrice, preferredStockTradeRecord.getTradePrice());
        assertEquals(preferredStockTradeRecordNumShares, preferredStockTradeRecord.getNumShares());
    }

    @Test
    public void testTradeHistoryInitialization() {
        assertTrue(tradeHistory.getTrades().containsKey(commonStockSymbol));
        assertTrue(tradeHistory.getTrades().containsKey(preferredStockSymbol));

        assertEquals(2, tradeHistory.getTrades().size());
        assertEquals(1, tradeHistory.getTrades().get(commonStockSymbol).size());
        assertEquals(1, tradeHistory.getTrades().get(preferredStockSymbol).size());
    }

}

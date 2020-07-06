package main.java.com.company.stockmarket.model;

import main.java.com.company.stockmarket.utils.TradeType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

public class TradeRecord {

    private String stockSymbol;
    private Instant timestamp;
    private BigDecimal tradePrice;
    private BigInteger numShares;
    private TradeType tradeType;

    public TradeRecord(String stockSymbol, BigDecimal tradePrice, BigInteger numShares, TradeType tradeType) {
        this.stockSymbol = stockSymbol;
        this.timestamp = Instant.now();
        this.tradePrice = tradePrice;
        this.numShares = numShares;
        this.tradeType = tradeType;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public BigInteger getNumShares() {
        return numShares;
    }

    public void setNumShares(BigInteger numShares) {
        this.numShares = numShares;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

}

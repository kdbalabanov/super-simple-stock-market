package main.java.com.company.stockmarket.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;

public class TradeRecord {

    private Stock stock;
    private Instant timestamp;
    private BigDecimal tradePrice;
    private BigInteger numShares;
    private TradeType tradeType;

    public TradeRecord(Stock stock, BigDecimal tradePrice, BigInteger numShares, TradeType tradeType) {
        this.stock = stock;
        this.timestamp = Instant.now();
        this.tradePrice = tradePrice;
        this.numShares = numShares;
        this.tradeType = tradeType;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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

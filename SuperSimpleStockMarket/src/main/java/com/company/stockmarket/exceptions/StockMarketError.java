package main.java.com.company.stockmarket.exceptions;

public class StockMarketError extends RuntimeException {

    public StockMarketError(String message, Throwable e) {
        super(message, e);
    }

    public StockMarketError(String message) {
        super(message);
    }
}

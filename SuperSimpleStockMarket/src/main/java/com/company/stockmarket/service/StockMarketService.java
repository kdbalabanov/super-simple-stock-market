package main.java.com.company.stockmarket.service;

import main.java.com.company.stockmarket.exceptions.StockMarketError;
import main.java.com.company.stockmarket.model.TradeLedger;

import java.util.Scanner;

public class StockMarketService {
    private static StockMarketService instance;
    private TradeLedger tradeLedger;

    public static StockMarketService getInstance() {
        if (instance == null) {
            instance = new StockMarketService();
        }
        return instance;
    }

    public void run() throws StockMarketError {
        System.out.println("Hello from StockMarketService");

        Scanner scanner = new Scanner(System.in);

        while(true) {
            String input = scanner.nextLine();
            System.out.println("You typed: " + input);
        }
    }

    private void init() {
        this.tradeLedger = new TradeLedger();

    }
}

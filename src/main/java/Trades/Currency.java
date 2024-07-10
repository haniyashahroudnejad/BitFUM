package Trades;

import java.io.Serializable;

public class Currency implements Serializable {
    public String CurrencyName;
    public Double CurrencyAmount;
    public Currency(String name,Double Amount){
        setCurrencyName(name);
        setCurrencyAmount(Amount);
    }

    public String getCurrencyName() {
        return CurrencyName;
    }

    private void setCurrencyName(String currencyName) {
        CurrencyName = currencyName;
    }

    public Double getCurrencyAmount() {
        return CurrencyAmount;
    }

    public void setCurrencyAmount(Double currencyAmount) {
        CurrencyAmount = currencyAmount;
    }
}

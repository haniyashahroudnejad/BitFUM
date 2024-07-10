package TableDataInformation;
import javafx.beans.property.*;

public class CurrencyTableData {
    public CurrencyTableData(String CurrencyName,Double CurrencyPrice,String CurrencyChanges,Double CurrencyMaxPrice,Double CurrencyMinPrice ){
        setCurrencyName(CurrencyName);
        setCurrencyPrice(CurrencyPrice);
        setCurrencyChanges(CurrencyChanges);
        setCurrencyMinPrice(CurrencyMinPrice);
        setCurrencyMaxPrice(CurrencyMaxPrice);
    }
    private StringProperty CurrencyName = new SimpleStringProperty(this, "CurrencyName", "");
    public String getCurrencyName() {
        return CurrencyName.get();
    }
    public StringProperty CurrencyNameProperty() {return CurrencyName;}
    public void setCurrencyName(String currencyName) {this.CurrencyName.set(currencyName);}

    private DoubleProperty CurrencyPrice = new SimpleDoubleProperty(this, "CurrencyPrice", 0.0);
    public double getCurrencyPrice() {
        return CurrencyPrice.get();
    }
    public DoubleProperty CurrencyPriceProperty() {return CurrencyPrice;}
    public void setCurrencyPrice(double CurrencyPrice) {this.CurrencyPrice.set(CurrencyPrice);}

    private DoubleProperty CurrencyMaxPrice = new SimpleDoubleProperty(this, "CurrencyMaxPrice", 0.0);
    public double getCurrencyMaxPrice() {
        return CurrencyMaxPrice.get();
    }
    public DoubleProperty CurrencyMaxPriceProperty() {return CurrencyMaxPrice;}
    public void setCurrencyMaxPrice(double CurrencyMaxPrice) {this.CurrencyMaxPrice.set(CurrencyMaxPrice);}

    private DoubleProperty CurrencyMinPrice = new SimpleDoubleProperty(this, "CurrencyMinPrice", 0.0);
    public double getCurrencyMinPrice() {
        return CurrencyMinPrice.get();
    }
    public DoubleProperty CurrencyMinPriceProperty() {return CurrencyMinPrice;}
    public void setCurrencyMinPrice(double CurrencyMinPrice) {this.CurrencyMinPrice.set(CurrencyMinPrice);}

    private StringProperty CurrencyChanges = new SimpleStringProperty(this, "CurrencyChanges", "");
    public String getCurrencyChanges() {
        return CurrencyChanges.get();
    }
    public StringProperty CurrencyChangesProperty() {return CurrencyChanges;}
    public void setCurrencyChanges(String CurrencyChanges) {this.CurrencyChanges.set(CurrencyChanges);}


}
package TableDataInformation;

import javafx.beans.property.*;

import java.sql.Date;
import java.sql.Time;

public class SwapTableData {
    public SwapTableData(Date Date, Time Time, String SelectedCurrencyName, String ConvertedCurrencyName, Double CurrencyAmount, Double EquivalentCurrencyAmount ){
        setDate(Date);
        setTime(Time);
        setSelectedCurrencyName(SelectedCurrencyName);
        setConvertedCurrencyName(ConvertedCurrencyName);
        setCurrencyAmount(CurrencyAmount);
        setEquivalentCurrencyAmount(EquivalentCurrencyAmount);

    }
    public StringProperty CurrencyName = new SimpleStringProperty(this, "CurrencyName", "");
    public String getCurrencyName() {
        return CurrencyName.get();
    }
    public StringProperty CurrencyNameProperty() {return CurrencyName;}
    public void setCurrencyName(String currencyName) {this.CurrencyName.set(currencyName);}

    private Date date;
    public void setDate(Date date) { this.date = date;}
    public Date getDate() {return date;}

    private Time time;
    public Time getTime() {return time;}
    public void setTime(Time time) { this.time = time;}

    private DoubleProperty CurrencyAmount = new SimpleDoubleProperty(this, "CurrencyAmount", 0.0);
    public double getCurrencyAmount() {
        return CurrencyAmount.get();
    }
    public DoubleProperty CurrencyAmountProperty() {return CurrencyAmount;}
    public void setCurrencyAmount(double CurrencyAmount) {this.CurrencyAmount.set(CurrencyAmount);}

    private DoubleProperty EquivalentCurrencyAmount = new SimpleDoubleProperty(this, "EquivalentCurrencyAmount", 0.0);
    public double getEquivalentCurrencyAmount() {
        return EquivalentCurrencyAmount.get();
    }
    public DoubleProperty EquivalentCurrencyAmountProperty() {return EquivalentCurrencyAmount;}
    public void setEquivalentCurrencyAmount(double EquivalentCurrencyAmount) {this.EquivalentCurrencyAmount.set(EquivalentCurrencyAmount);}


    private StringProperty SelectedCurrencyName = new SimpleStringProperty(this, "SelectedCurrencyName", "");
    public String getSelectedCurrencyName() {
        return SelectedCurrencyName.get();
    }
    public StringProperty SelectedCurrencyNameProperty() {return SelectedCurrencyName;}
    public void setSelectedCurrencyName(String SelectedCurrencyName) {this.SelectedCurrencyName.set(SelectedCurrencyName);}

    private StringProperty ConvertedCurrencyName = new SimpleStringProperty(this, "ConvertedCurrencyName", "");
    public String getConvertedCurrencyName() {
        return ConvertedCurrencyName.get();
    }
    public StringProperty ConvertedCurrencyNameProperty() {return ConvertedCurrencyName;}
    public void setConvertedCurrencyName(String ConvertedCurrencyName) {this.ConvertedCurrencyName.set(ConvertedCurrencyName);}
}

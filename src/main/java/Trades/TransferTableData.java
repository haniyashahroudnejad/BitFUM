package Trades;
import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransferTableData {
    public TransferTableData(LocalDate Date, LocalTime Time, String DestinationWalletID, String CurrencyName, Double CurrencyAmount, int IssueTracking ){
        setDate();
        setTime();
        setDestinationWalletID(DestinationWalletID);
        setCurrencyName(CurrencyName);
        setCurrencyAmount(CurrencyAmount);
        setIssueTracking(IssueTracking);

    }
    private StringProperty CurrencyName = new SimpleStringProperty(this, "CurrencyName", "");
    public String getCurrencyName() {
        return CurrencyName.get();
    }
    public StringProperty CurrencyNameProperty() {return CurrencyName;}
    public void setCurrencyName(String currencyName) {this.CurrencyName.set(currencyName);}

    private LocalDate localDate;
    public LocalDate getDate() {return localDate;}
    public void setDate() { localDate = LocalDate.now();}

    private LocalTime localtime;
    public LocalTime getTime() {return localtime;}
    public void setTime() { localtime = LocalTime.now();}

    private DoubleProperty CurrencyAmount = new SimpleDoubleProperty(this, "CurrencyAmount", 0.0);
    public double getCurrencyAmount() {
        return CurrencyAmount.get();
    }
    public DoubleProperty setCurrencyAmountProperty() {return CurrencyAmount;}
    public void setCurrencyAmount(double CurrencyAmount) {this.CurrencyAmount.set(CurrencyAmount);}

    private IntegerProperty IssueTracking = new SimpleIntegerProperty(this, "IssueTracking",0);
    public int getIssueTracking() {
        return IssueTracking.get();
    }
    public IntegerProperty IssueTrackingProperty() {return IssueTracking;}
    public void setIssueTracking(int IssueTracking) {this.IssueTracking.set(IssueTracking);}

    private StringProperty DestinationWalletID = new SimpleStringProperty(this, "DestinationWalletID", "");
    public String getDestinationWalletID() {
        return DestinationWalletID.get();
    }
    public StringProperty DestinationWalletIDProperty() {return DestinationWalletID;}
    public void setDestinationWalletID(String DestinationWalletID) {this.DestinationWalletID.set(DestinationWalletID);}


}
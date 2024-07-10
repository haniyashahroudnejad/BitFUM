package TableDataInformation;
import javafx.beans.property.*;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransferTableData implements Serializable {

    public TransferTableData(Date Date, Time Time,String SourceWalletID, String DestinationWalletID, String CurrencyName, Double CurrencyAmount, int IssueTracking ){
        setDate(Date);
        setTime(Time);
        setDestinationWalletID(DestinationWalletID);
        setSourceWalletID(SourceWalletID);
        setCurrencyName(CurrencyName);
        setCurrencyAmount(CurrencyAmount);
        setIssueTracking(IssueTracking);
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

    private StringProperty SourceWalletID = new SimpleStringProperty(this, "SourceWalletID", "");
    public String getSourceWalletID() {
        return SourceWalletID.get();
    }
    public StringProperty SourceWalletIDProperty() {return SourceWalletID;}
    public void setSourceWalletID(String SourceWalletID) {this.SourceWalletID.set(SourceWalletID);}


}
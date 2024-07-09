package Trades;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransferTableData2 implements Serializable {
    public String SourceWalletID1;
    public String DestinationWalletID1;
    public String CurrencyName1;
    public Double CurrencyAmount1;
    public int issueTracking1;
    public String userName;

    public TransferTableData2(Date Date, Time Time, String SourceWalletID, String DestinationWalletID, String CurrencyName, Double CurrencyAmount, int IssueTracking, String userName){
        setDate(Date);
        setTime(Time);
        this.SourceWalletID1 = SourceWalletID;
        this.DestinationWalletID1 = DestinationWalletID;
        this.CurrencyName1 = CurrencyName;
        this.CurrencyAmount1 = CurrencyAmount;
        this.issueTracking1 = IssueTracking;
        this.userName = userName;
    }
    private Date date;
    public void setDate(Date date) { this.date = date;}
    public Date getDate() {return date;}

    private Time time;
    public Time getTime() {return time;}
    public void setTime(Time time) { this.time = time;}
}

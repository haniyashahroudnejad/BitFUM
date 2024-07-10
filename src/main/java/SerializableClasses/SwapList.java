package SerializableClasses;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class SwapList implements Serializable {
    public String SelectedCurrencyName;
    public String ConvertedCurrencyName;
    public Double CurrencyAmount;
    public Double EquivalentCurrencyAmount;
    public String userName;
    public Date date;
    public Time time;

    public SwapList(Date Date, Time Time, String SelectedCurrencyName, String ConvertedCurrencyName, Double CurrencyAmount, Double EquivalentCurrencyAmount , String userName){
        setDate(Date);
        setTime(Time);
        this.SelectedCurrencyName = SelectedCurrencyName;
        this.ConvertedCurrencyName = ConvertedCurrencyName;
        this.CurrencyAmount = CurrencyAmount;
        this.EquivalentCurrencyAmount = EquivalentCurrencyAmount;
        this.userName = userName;
    }

    public void setDate(Date date) { this.date = date;}
    public void setTime(Time time) { this.time = time;}
}

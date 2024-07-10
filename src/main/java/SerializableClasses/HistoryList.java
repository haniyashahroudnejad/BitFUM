package SerializableClasses;

import java.io.Serializable;

public class HistoryList implements Serializable {
    public int operationType;
    public String currencyName;
    public double Count;
    public double Rate;
    public float percentageOfCompletion;
    public String date;

    public HistoryList(int i, String name, double count, double rate, float percentageOfCompletion, String date){
        this.operationType = i;
        this.currencyName = name;
        this.Count = count;
        this.Rate = rate;
        this.percentageOfCompletion = percentageOfCompletion;
        this.date = date;
    }
}

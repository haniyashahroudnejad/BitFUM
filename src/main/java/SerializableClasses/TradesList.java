package SerializableClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class TradesList implements Serializable {
    public int operationType;
    public String currencyName;
    public double Count;
    public double Rate;
    public float percentageOfCompletion;

    public TradesList(int i, String name, double count, double rate, float percentageOfCompletion){
        this.operationType = i;
        this.currencyName = name;
        this.Count = count;
        this.Rate = rate;
        this.percentageOfCompletion = percentageOfCompletion;
    }
}

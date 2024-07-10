package SerializableClasses;

import TableDataInformation.TransferTableData;
import SerializableClasses.TransferTableData2;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;

public class TransferLists implements Serializable {
    public ArrayList<TransferTableData2>  transfers;
    public ArrayList<String> MyDestinationAddressList;
    public TransferLists(ArrayList<TransferTableData2> transfers, ArrayList<String> MyDestinationAddressList){
        this.transfers = transfers;
        this.MyDestinationAddressList = MyDestinationAddressList;
    }
}

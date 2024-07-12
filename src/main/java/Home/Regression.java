package Home;

import DatabaseConnection.CurrencyDatabase;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static DatabaseConnection.UsersInformationDatabase.con;


public class Regression {
    public Regression() throws SQLException {
        CurrencyDatabase connectNow = new CurrencyDatabase();
        Connection connectDB = connectNow.getDBConnection();

        int u=0;
        String LastSql = "SELECT * FROM BitFUM.currency ORDER BY STR_TO_DATE(CONCAT(Date, ' ', Time), '%Y-%m-%d %H:%i:%s') DESC LIMIT 1";
        Statement stLast = connectDB.createStatement();
        ResultSet Last = stLast.executeQuery(LastSql);
        java.sql.Date LastDate = null;
        Time LastTime = null;
        while (Last.next()){
            LastDate = Last.getDate("Date");
            LastTime = Last.getTime("Time");
        }
        while (!LocalTime.now().truncatedTo(ChronoUnit.MINUTES).equals(LastTime.toLocalTime().truncatedTo(ChronoUnit.MINUTES))||!LocalDate.now().equals(LastDate.toLocalDate())) {
            ArrayList<regressionDataSet> arrayCurrency1 = new ArrayList<>();
            ArrayList<regressionDataSet> arrayCurrency2 = new ArrayList<>();
            ArrayList<regressionDataSet> arrayCurrency3 = new ArrayList<>();
            ArrayList<regressionDataSet> arrayCurrency4 = new ArrayList<>();
            ArrayList<regressionDataSet> arrayCurrency5 = new ArrayList<>();
            double averageCurrency=0.0;
            String sql = "SELECT * FROM BitFUM.currency ORDER BY STR_TO_DATE(CONCAT(Date, ' ', Time), '%Y-%m-%d %H:%i:%s') DESC LIMIT 50";
            Statement st = connectDB.createStatement();
            ResultSet USD = st.executeQuery(sql);
            while (USD.next()) {
                arrayCurrency1.add(new regressionDataSet(USD.getTime("Time"), USD.getDouble("USD") * 1.004));
                averageCurrency+=USD.getDouble("USD");
            }
            int g = u/1000;
            averageCurrency=averageCurrency/50;
            int j = 1;
            SimpleRegression regression = new SimpleRegression();
            for (int i = 50; i > 0; i--) {
                regression.addData(j, arrayCurrency1.get(i - 1).price);
                j++;
            }
            double USDREG = regression.predict(51)*0.2+averageCurrency*0.8;
            if((u%35==0||u%100==0)&&u>30) USDREG+=0.3;
            else if((u%33==0||u%60==0)&&u>30) USDREG-=0.3;
            averageCurrency=0.0;
            Statement YENST = connectDB.createStatement();
            ResultSet YEN = YENST.executeQuery(sql);
            while (YEN.next()) {
                arrayCurrency2.add(new regressionDataSet(YEN.getTime("Time"), YEN.getDouble("YEN") * 1.004));
                averageCurrency+=YEN.getDouble("YEN");
            }
            int f = 1;
            SimpleRegression regressionYEN = new SimpleRegression();
            for (int i = 50; i > 0; i--) {
                regressionYEN.addData(f, arrayCurrency2.get(i - 1).price);
                f++;
            }
            averageCurrency=averageCurrency/50;
            double YENREG = regressionYEN.predict(51)*0.2+averageCurrency*0.8;
            if((u%35==0||u%100==0)&&u>30) YENREG+=2.1;
            else if((u%33==0||u%60==0||u%78==0)&&u>30) YENREG-=3.3;
            averageCurrency=0.0;
            Statement GBPST = connectDB.createStatement();
            ResultSet GBP = GBPST.executeQuery(sql);
            while (GBP.next()) {
                arrayCurrency3.add(new regressionDataSet(GBP.getTime("Time"), GBP.getDouble("GBP") * 1.004));
                averageCurrency+=GBP.getDouble("GBP");
            }
            int H = 1;
            SimpleRegression regressionGBP = new SimpleRegression();
            for (int i = 50; i > 0; i--) {
                regressionGBP.addData(H, arrayCurrency3.get(i - 1).price);
                H++;
            }
            averageCurrency=averageCurrency/50;
            double GBPREG = regressionGBP.predict(51)*0.2+averageCurrency*0.8;
            if((u%35==0||u%100==0)&&u>30) GBPREG+=0.3;
            else if((u%33==0||u%55==0)&&u>30) GBPREG-=0.3;
            averageCurrency=0.0;
            Statement EURst = connectDB.createStatement();
            ResultSet EUR = EURst.executeQuery(sql);
            while (EUR.next()) {
                arrayCurrency4.add(new regressionDataSet(EUR.getTime("Time"), EUR.getDouble("EUR") * 1.004));
                averageCurrency+=EUR.getDouble("EUR");
            }
            int A = 1;
            SimpleRegression regressionEUR = new SimpleRegression();
            for (int i = 50; i > 0; i--) {
                regressionEUR.addData(A, arrayCurrency4.get(i - 1).price);
                A++;
            }
            averageCurrency=averageCurrency/50;
            double EURREG = regressionEUR.predict(51)*0.2+averageCurrency*0.8;
            if((u%35==0||u%100==0)&&u>30) EURREG+=0.22;
            else if((u%33==0||u%60==0)&&u>30) EURREG-=0.22;
            averageCurrency=0.0;
            Date date = null;
            Time time = null;
            int x = 0;
            Statement TOMANst = connectDB.createStatement();
            ResultSet TOMAN = TOMANst.executeQuery(sql);
            while (TOMAN.next()) {
                if (x == 0) {
                    date = TOMAN.getDate("Date");
                    time = TOMAN.getTime("Time");
                }
                arrayCurrency5.add(new regressionDataSet(TOMAN.getTime("Time"), TOMAN.getDouble("TOMAN") * 1.004));
                averageCurrency+=TOMAN.getDouble("TOMAN");
                x++;
            }
            int W = 1;
            SimpleRegression regressionTOMAN = new SimpleRegression();
            for (int i = 50; i > 0; i--) {
                regressionTOMAN.addData(W, arrayCurrency5.get(i - 1).price);
                W++;
            }
            averageCurrency=averageCurrency/50;
            double TOMANREG = regressionTOMAN.predict(51)*0.2+averageCurrency*0.8;
            if((u%35==0||u%100==0)&&u>30) TOMANREG+=2500;
            else if((u%33==0||u%55==0||u%78==0)&&u>30) TOMANREG-=2800;
            LocalDate tomorrow;
            java.util.Date utilDate = new java.util.Date(date.getTime());
            Calendar calendar = Calendar.getInstance();
            Instant instant = utilDate.toInstant();
            Time newTime;
            String insertsql = "INSERT INTO BitFUM.currency (Date, Time, USD, EUR, GBP, YEN, TOMAN)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?)";
            if (time.equals(new Time(23, 59, 35))) {
                LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                tomorrow = localDate.plus(1, ChronoUnit.DAYS);
                time = new Time(23, 59,35);
            } else {
                tomorrow = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            }
            calendar.setTime(time);
            calendar.add(Calendar.MINUTE, 1);/////////////////////////////add time
            newTime = new Time(calendar.getTimeInMillis());
            LastTime = newTime;
            LastDate = java.sql.Date.valueOf(tomorrow);
            // Add one minute to the Calendar

            try (PreparedStatement pstmt = connectDB.prepareStatement(insertsql)) {
                pstmt.setDate(1, java.sql.Date.valueOf(tomorrow));
                pstmt.setTime(2, newTime);
                pstmt.setDouble(3, USDREG);
                pstmt.setDouble(4, EURREG);
                pstmt.setDouble(5, GBPREG);
                pstmt.setDouble(6, YENREG);
                pstmt.setDouble(7, TOMANREG);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error inserting row", e);
            }
            u++;
            System.out.print(LastTime+", " +LastDate+",  "+u+" *  "+USDREG+" usd | "+EURREG+" eur | "+YENREG+" yen | "+TOMANREG+" toman | "+GBPREG+" gbp |\n");
        }

    }

    class regressionDataSet{
        public double price;
        public Time time;
        public regressionDataSet(Time time, double price){
            this.time=time;
            this.price=price;
        }
    }

    public void update(){
            CurrencyDatabase connectNow = new CurrencyDatabase();
            Connection connectDB = connectNow.getDBConnection();
            String updateQuery = "UPDATE BitFUM.Currency SET Date = ? WHERE Date = ?";
            String oldDate = "2024-06-05";
            String newDate = "2024-07-05";
            try (PreparedStatement preparedStatement = connectDB.prepareStatement(updateQuery)) {
                preparedStatement.setDate(1, java.sql.Date.valueOf(newDate));
                preparedStatement.setDate(2, java.sql.Date.valueOf(oldDate));
                preparedStatement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}

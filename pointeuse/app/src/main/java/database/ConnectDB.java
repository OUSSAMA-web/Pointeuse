package database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Model.*;

public class ConnectDB  extends AsyncTask<String, Void, String> {

    private static final String url = "jdbc:mysql://192.168.43.253:3306/PointeuseDb";
    private static final String user = "laaraj";
    private static final String pass = "oussama";
    ArrayList<Etudiant> etudiants = new ArrayList<>();
    ArrayList<Classe> classes = new ArrayList<>();
    ArrayList<Prof> profs = new ArrayList<>();

    Context c ;

    public ConnectDB(Context c){
        this.c = c;
    }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                DBManager manager = new DBManager(this.c);
                DatabaseHelper dh = new DatabaseHelper(this.c);
                Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(url, user, pass);
                    System.out.println("Databaseection success");

                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("select * from Etudiant");
                    ResultSetMetaData rsmd = rs.getMetaData();

                    Statement st1 = con.createStatement();
                    ResultSet rs1 = st1.executeQuery("select * from Professeur");
                    ResultSetMetaData rsmd1 = rs1.getMetaData();

                    Statement st2 = con.createStatement();
                    ResultSet rs2 = st2.executeQuery("select * from Classe");
                    ResultSetMetaData rsmd2 = rs2.getMetaData();

                    Statement st3 = con.createStatement();
                    ResultSet rs3 = st3.executeQuery("select * from Absence");
                    ResultSetMetaData rsmd3 = rs3.getMetaData();


                while (rs.next()) {
                    dh.createEtudiant(
                            new Etudiant(
                            rs.getString(1).toString(),
                            rs.getString(2).toString(),
                            rs.getString(3).toString(),
                            rs.getInt(4)));

                }
                rs.close();


                while (rs1.next()) {
                    dh.createProfs(new Prof(
                            rs1.getString(1).toString(),
                            rs1.getString(2).toString(),
                            rs1.getString(3).toString()));



                }
                rs1.close();

                while (rs2.next()) {
                    dh.createClasse(new Classe(rs2.getInt(1),
                            rs2.getString(2).toString(),
                            rs2.getString(3).toString(),
                            rs2.getString(4).toString()));
                }



                rs2.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String pass) {

        }


    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { }
        return "";
    }

}

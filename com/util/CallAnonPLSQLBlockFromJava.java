package com.util;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import oracle.jdbc.OracleTypes;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CallAnonPLSQLBlockFromJava
{
  private static java.io.FileWriter outfile;
  private static java.io.PrintWriter pw;

  public static void main(String[] args) throws Exception
  {
    String driverName = "oracle.jdbc.driver.OracleDriver";
    Connection connection = null;
    CallableStatement stmt = null;
    ResultSet rset = null;
 	Class.forName(driverName);
    String serverName = "127.0.0.2";
    String portNumber = "<Your Port>";
    String sid = "<Your SID>";
    String url = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;
    String username = "<Your Username>";
    String password = "<Your Password>";
    connection = DriverManager.getConnection(url, username, password);
    System.out.println("Connection successful!");
	String outFile = "report.txt";
	String inFile = "input.dat";
    System.out.println("outfile:" + outFile);
    System.out.println("in file:" + inFile);
    String outputFileName = outFile;
    String inputFileName = inFile;
    outfile = new java.io.FileWriter(outputFileName);
    pw = new java.io.PrintWriter(outfile);

    FileInputStream infstream = new FileInputStream(inFile);
    System.out.println("Reading file:" + inFile);
    DataInputStream inds = new DataInputStream(infstream);
    BufferedReader brin = new BufferedReader(new InputStreamReader(inds));
    StringBuffer sbSql = new StringBuffer();
    String strLine = "";
    while ((strLine = brin.readLine()) != null)
    {
    	sbSql.append(strLine);
    }
    inds.close();

    String plsql = "" +
          " declare " +
          "    " +
          "    input_val varchar2(20) := null; " +
          "    v_stmt_str varchar2(200); " +
          "    my_cursor sys_refcursor;" +
          "    " +
          " begin " +
          "    " +
          "    input_val := ?; " +
          "    ? := 'input parameter was = ' || input_val;" +
          "    " +
          "    open my_cursor for Select column from DatabaseName.Table where column = '" + 1021 + "';" +
          "    ? := my_cursor;" +
          " end;";
    System.out.println("Sql: " + plsql);
    stmt = connection.prepareCall(plsql);

    stmt.setString(1, "1021");
    stmt.registerOutParameter(2, Types.VARCHAR);
    stmt.registerOutParameter(3, OracleTypes.CURSOR);

    stmt.execute();

    System.out.println(stmt.getObject(2));  // this just echo's the input parameter line
    rset = (ResultSet) stmt.getObject(3);

    System.out.println(" ---------- Looping through resultSet -------------- ");
    System.out.println(" ");
    while (rset.next ())
    {
       System.out.println ("description = " + rset.getString(1));
    }
    stmt.close();
    connection.close();
    System.out.println(" ");
    System.out.println(" ----------        complete           -------------- ");

   }
}


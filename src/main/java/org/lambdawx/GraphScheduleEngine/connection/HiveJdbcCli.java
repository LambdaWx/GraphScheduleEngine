package org.lambdawx.GraphScheduleEngine.connection;

import com.google.common.io.Resources;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Hive的JavaApi
 *
 * 启动hive的远程服务接口命令行执行：hive --service hiveserver >/dev/null 2>/dev/null &
 *
 */
public class HiveJdbcCli {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";
    private static String url = null;
    private static String user = null;
    private static String password = null;
    private static ResultSet res;
    private static final Logger log = Logger.getLogger(HiveJdbcCli.class);

    private static Connection connection = null;
    private static Statement stmt = null;
    static{
        init();
    }
    private static void init(){
        Properties prop = new Properties();
        InputStream is = null;
        try {
            URL url = Resources.getResource("hiveJdbc.properties");
            is = url.openStream();
            prop.load(is);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            log.warn("no hiveJdbc.properties found, using default settings");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        url = prop.getProperty("url","jdbc:hive2://172.16.*.*:10000/default");
        user = prop.getProperty("user","*");
        password = prop.getProperty("password","*");
        try {
            Class.forName(driverName);
            Connection conn = DriverManager.getConnection(url, user, password);
            connection = conn;
        }catch (Exception e){
            log.error(e);
            e.printStackTrace();
        }
        try {
            stmt = connection.createStatement();
        }catch (Exception e){
            log.error(e);
            e.printStackTrace();
        }
    }

    public static void executeSql(String sql) throws Exception{
        synchronized (connection) {
            Statement stmt_execute = connection.createStatement();
            stmt_execute.execute(sql);
            try {
                stmt_execute.close();
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
}

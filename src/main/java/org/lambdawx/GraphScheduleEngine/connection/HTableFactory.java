package org.lambdawx.GraphScheduleEngine.connection;

import com.google.common.io.Resources;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.log4j.Logger;
//import org.apache.hadoop.conf.Configuration.addDeprecation;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;


public class HTableFactory {
	 
	    private static Logger LOG = Logger.getLogger(HTableFactory.class);

	    public static final String HBASE_CONFIF_FILENAME = "hbase.properties";

	    public static final String HBASE_ZOOKEEPER_KEY = "hbase.zookeeper";
	    public static final String HBASE_ZOOKEEPER_DEFAULT = "localhost";
	    public static final String HBASE_ZOOKEEPER_PORT_KEY = "hbase.zookeeper.port";
	    public static final String HBASE_ZOOKEEPER_DEFAULT_PORT = "2181";

	    private static HConnection conn;
	    private static Configuration conf;

	    static {
	        conf = initConfig();
	    }

	    public static HTableInterface getTable(String tableName) throws IOException {
	        if (conn == null) {
	            initConn();
	        }
	        return conn.getTable(tableName);
	    }

	    public static Configuration getConf() {
	        return conf;
	    }

	    private static Configuration initConfig() {
	        Configuration conf = new Configuration();
	        Properties prop = new Properties();
	        InputStream is = null;
	        try {
	            URL url = Resources.getResource(HBASE_CONFIF_FILENAME);
	            is = url.openStream();
	            prop.load(is);
	        } catch (IOException e) {
	            LOG.error(e.getMessage(), e);
	        } catch (IllegalArgumentException e) {
	            LOG.warn("no hbase.properties found, using default settings");
	        } finally {
	            try {
	                if (is != null) {
	                    is.close();
	                }
	            } catch (IOException e) {
	                LOG.error(e.getMessage(), e);
	            }
	        }

	        conf.set(HConstants.ZOOKEEPER_QUORUM, prop.getProperty(HBASE_ZOOKEEPER_KEY, HBASE_ZOOKEEPER_DEFAULT));
	        conf.set(HConstants.ZOOKEEPER_CLIENT_PORT,
	                prop.getProperty(HBASE_ZOOKEEPER_PORT_KEY, HBASE_ZOOKEEPER_DEFAULT_PORT));
	        conf.set(HConstants.HBASE_CLIENT_RETRIES_NUMBER, Integer.toString(3));
	        conf.set("zookeeper.recovery.retry", Integer.toString(2));
	        conf.set("hbase.client.scanner.timeout.period", Integer.toString(1000000));
	        return conf;
	    }

	    public static void initConn() throws IOException {
	        if (conn != null) {
	            conn.close();
	            conn = null;
	        }
	        conn = HConnectionManager.createConnection(conf);
	    }

	    public static void close() {
	        try {
	            conn.close();
	        } catch (IOException e) {
	            LOG.error(e.getMessage(), e);
	        }
	    }
	}

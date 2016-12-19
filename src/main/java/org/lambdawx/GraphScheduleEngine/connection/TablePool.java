package org.lambdawx.GraphScheduleEngine.connection;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.log4j.Logger;

import java.io.IOException;


public class TablePool {
    private static Logger LOG = Logger.getLogger(TablePool.class);

	public static HTableInterface getHTable(String tableName) {
		try {
            return HTableFactory.getTable(tableName);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
	}

}

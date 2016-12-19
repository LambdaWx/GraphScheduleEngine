package org.lambdawx.GraphScheduleEngine.util;

import com.google.common.io.Resources;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class LoadProperties {
    private static final Logger LOG = Logger.getLogger(LoadProperties.class);

    private static Properties property;
    
    public static final String AUTHORITY = "authority.properties";
    
    public LoadProperties(String configFile){
    	InputStream inputStream = null;
        try {
        	//加载配置文件
            inputStream = findResources(configFile);
            property = new Properties();
            property.load(inputStream);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }
    
    public static String getProperty(String key) {
    	if(null == property)
    		return null;
        return property.getProperty(key);
    }

    private static InputStream findResources(String fileName) throws IOException{
        URL url = null;
        try {
            url = Resources.getResource(File.pathSeparator + fileName);
            LOG.warn("find " + fileName + " in the root of classpath. use it to override the build-in settings");
        } catch (IllegalArgumentException e) {
            LOG.warn("can't not find " + fileName + " in the root of classpath. try built-in settings");
            try {
                url = Resources.getResource(fileName);
            } catch (IllegalArgumentException e1) {
                throw new IOException("can't find " + fileName + " in the classpath");
            }
        }
        if (url != null) {
            return url.openStream();
        } else {
            throw new IOException("something is wrong when loading the file " + fileName);
        }
    }
}

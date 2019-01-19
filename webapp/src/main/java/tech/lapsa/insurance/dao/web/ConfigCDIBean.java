package tech.lapsa.insurance.dao.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("config")
@ApplicationScoped
public class ConfigCDIBean {

    private static final String VERSION_RESOURCE_PATH = "/version.properties";

    private static final String PARAM_APP_NAME = "app.name";
    private static final String PARAM_APP_VERSION = "app.version";

    private static final String APP_NAME_DEFAULT = "UNKNOWN";
    private static final String APP_VERSION_DEFAULT = "UNKNOWN";

    private String appName = APP_NAME_DEFAULT;
    private String appVersion = APP_VERSION_DEFAULT;

    @PostConstruct
    public void fetchAppProperties() {
	try (InputStream stream = this.getClass().getResourceAsStream(VERSION_RESOURCE_PATH)) {
	    if (stream == null)
		return;
	    Properties props = new Properties();
	    props.load(stream);
	    appName = props.getProperty(PARAM_APP_NAME, APP_NAME_DEFAULT);
	    appVersion = props.getProperty(PARAM_APP_VERSION, APP_VERSION_DEFAULT);
	} catch (final IOException ignored) {
	}
    }

    public String getAppVersion() {
	return appVersion;
    }

    public String getAppName() {
	return appName;
    }
}

package nl.sogyo.jesper.webserver;

import java.util.List;

/**
 * Created by jvdberg on 06/06/2014.
 */
public interface Requestable {
    String getHTTPMethod();
    String getResourcePath();
    List<Cookie> getCookies();


    public enum HTTPMethod {
        GET, POST
    }
}

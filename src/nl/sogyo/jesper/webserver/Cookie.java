package nl.sogyo.jesper.webserver;

/**
 * Created by jvdberg on 18/06/2014.
 */
public interface Cookie {
    String getName();
    void setName(String name);
    String getValue();
    void setValue(String value);
}

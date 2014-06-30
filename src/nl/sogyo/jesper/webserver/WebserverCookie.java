package nl.sogyo.jesper.webserver;

import java.util.ArrayList;

/**
 * Created by jvdberg on 18/06/2014.
 */
public class WebserverCookie implements Cookie {

    private String name, value;

    public WebserverCookie(String name, String value) {
        setName(name);
        setValue(value);
    }

    @Override
    public String getName() {
        return name;
    }
    @Override


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

}

package nl.sogyo.jesper.webserver;

import java.util.ArrayList;

/**
 * Created by jvdberg on 20/06/2014.
 */
public class Session {
    private WebserverCookie cookie;
    private ArrayList<String> isbnList = new ArrayList<>();

    public Session(WebserverCookie cookie) {
        this.cookie = cookie;
    }

    public WebserverCookie getCookie() {
        return cookie;
    }

    public void addIsbn(String ISBN) {
        isbnList.add(ISBN);
    }

    public ArrayList<String> getIsbnList() {
        return isbnList;
    }


}

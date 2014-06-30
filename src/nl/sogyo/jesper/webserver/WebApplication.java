package nl.sogyo.jesper.webserver;

import java.io.IOException;

/**
 * Created by jvdberg on 13/06/2014.
 */
public interface WebApplication {
    void process(Request request, Response response);
}
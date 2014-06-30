package nl.sogyo.jesper.webserver;

import java.util.Calendar;

/**
 * Created by jvdberg on 06/06/2014.
 */
public interface Responsable {
    HTTPStatusCode getStatus();
    void setStatus(HTTPStatusCode status);
    Calendar getDate();
    String getContent();
    void setContent(String content);
    String toString();
    void addCookie(Cookie cookie);


    public enum HTTPStatusCode {
        OK(200, "OK"), NotFound(404, "Not Found"), ServerError(500, "ServerError");

        private int code;
        private String description;

        private HTTPStatusCode(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }
}

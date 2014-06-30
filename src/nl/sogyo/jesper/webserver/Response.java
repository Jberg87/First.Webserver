package nl.sogyo.jesper.webserver;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jvdberg on 06/06/2014.
 */
public class Response implements Responsable {

    private HTTPStatusCode status;
    private String httpStatus, content, server, connection, body, contentLength, bodyInProcess;

    public ArrayList<Cookie> getCookieList() {
        return cookieList;
    }

    private ArrayList<Cookie> cookieList = new ArrayList<>();

    public Response(){
        setStatus(HTTPStatusCode.OK);
        setServer("Server: Apache/2.0.63 (Unix) mod_ssl/2.0.63 OpenSSL/0.9.7e PHP/5.2.8");
        setConnection("Connection: close");
        setContent("Content-Type: text/html; charset=UTF-8");
    }

    @Override
    public String toString() {
        setContentLength();
        return getHttpStatus() + "\r\n" +
                calendarToString(getDate()) + "\r\n" +
                getServer() + "\r\n" +
                getConnection() + "\r\n" +
                getContent() + "\r\n" +
                getCookiesToString() + "\r\n" +
                getContentLength() + "\r\n\r\n" +
                getBody();
    }


    // Getters en Setters allemaal hieronder:
    @Override
    public HTTPStatusCode getStatus() {
        return HTTPStatusCode.OK;
    }
    @Override
    public void setStatus(HTTPStatusCode status) {
        this.status = status;
        this.httpStatus = "HTTP/1.1 " + status.getCode() + " " + status.getDescription();
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    @Override
    public Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }
    private String calendarToString(Calendar calendarA) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd.MM.yyyy HH:mm:ss zzz");
        String calendar = dateFormat.format(calendarA.getTime());
        return calendar;
    }


    public String getServer() {
        return this.server;
    }
    public void setServer(String server) {
        this.server = server;
    }


    public String getConnection() {
        return this.connection;
    }
    public void setConnection(String connection) {
        this.connection = connection;
    }


    @Override
    public String getContent() {
        return this.content;
    }
    @Override
    public void setContent(String content) {
        this.content = content;
    }


    public String getBody() {
        return ((this.body != null) ? body : "");
    }
    public String getBodyInProcess() {
        return bodyInProcess;
    }
    public void setBody(String body) {
        this.body = "<html>\r\n<body>\r\n" + body + "\r\n</body>\r\n</html>";
        this.bodyInProcess = body;
    }

    public String getContentLength() {
        return contentLength;
    }
    public void setContentLength() {
        int sizeInBytes = 0;
        try {
            sizeInBytes = this.body.getBytes("UTF-8").length;
            this.contentLength = "Content-Length: " + sizeInBytes;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            this.contentLength = "Content-Length: " + 0;
        }
    }

    public String getCookiesToString() {
        String output = "";
        if (cookieList.size() != 0) {
            for (Cookie c : cookieList) {
                output = output + c.getName() + "=" + c.getValue() + "; ";
            }
//            output = output.substring(0,output.length()-2);
        }
        return "Set-Cookie: " + output;
    }
    @Override
    public void addCookie(Cookie cookie) {
        cookieList.add(cookie);
    }


}

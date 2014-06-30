package nl.sogyo.jesper.webserver;


/**
 * Created by jvdberg on 18/06/2014.
 */
public class CopyCookieWebApp implements WebApplication{

    private Response response;
    private  static int cookieNameNr = 1, cookieValueNr = 1;
    private WebserverCookie lastCookie;

    public WebserverCookie getLastCookie() {
        return lastCookie;
    }

    @Override
    public void process(Request request, Response response) {
        this.response = response;
        addNewCookieToResponse();
//        response.setBody(request.giveStandardBody() + "<br><b>The following cookies are given to the response:<br>" + response.getCookiesToString() + "</b>");
    }

    private void addNewCookieToResponse() {
        response.addCookie(lastCookie= new WebserverCookie("CCWA" + CopyCookieWebApp.cookieNameNr, "" + CopyCookieWebApp.cookieValueNr));
        CopyCookieWebApp.cookieValueNr++;
        CopyCookieWebApp.cookieNameNr++;
    }

    public Response getResponse() {
        return response;
    }

}

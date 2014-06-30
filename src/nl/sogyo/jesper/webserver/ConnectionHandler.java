package nl.sogyo.jesper.webserver;

import java.io.*;
import java.net.Socket;

/**
 * Created by jvdberg on 06/06/2014.
 */
public class ConnectionHandler implements Runnable{
    private Socket socket;

    public ConnectionHandler(Socket toHandle) {
        this.socket = toHandle;

    }

    @Override
    public void run() {
        try {
            System.out.println();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = reader.readLine();
            Request request = new Request(line);
            while (!line.isEmpty()) {
//                System.out.println(line);
                if (line.contains(":")) request.addHeaderParameter(new HeaderParameter(line));
                line = reader.readLine();
            }
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            executeCCWA(writer, request);
            executeLibrary(writer, request);
            writer.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void executeCCWA(BufferedWriter writer, Request request) throws IOException{
        CopyCookieWebApp ccwa = new CopyCookieWebApp();
        ccwa.process(request, new Response());
        writer.write(ccwa.getResponse().toString());
//        System.out.println();
//        System.out.println(ccwa.getResponse().toString());
    }

    private void executeLibrary(BufferedWriter writer, Request request) throws IOException{
        Library library = new Library();
        library.process(request, new Response());
        writer.write(library.getResponse().toString());
//        System.out.println();
//        System.out.println(library.getResponse().toString());
    }
}

package org.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class ServerLauncher {

    public static void main(String[] args) throws Exception {

        // Jetty server — 8080 portu
        Server server = new Server(8080);

        // WebAppContext → web.xml'i yüklemek için
        WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        context.setResourceBase("src/main/webapp");
        context.setParentLoaderPriority(true);

        server.setHandler(context);

        System.out.println("Jetty başlatılıyor... http://localhost:8080/ws/paymentWsdl.wsdl");

        server.start();
        server.join();
    }
}

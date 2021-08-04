package server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StartServer {
    private static int defaultPort=55555;

    public static void main(String[] args) {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-server.xml");
            System.out.println("Serverul a pornit");

    }
}

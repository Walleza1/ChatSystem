package Server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Server extends HttpServlet {
    private String mymsg;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Setting up the content type of web page
        response.setContentType("text/html");
        String param=request.getParameter("emma");

        // Writing the message on the web page
        PrintWriter out = response.getWriter();
        out.println("<p>" + "Hello Friends!" + "</p>");
        out.println("<p>"+param+"</p>");
    }
    public void destroy()
    {
        // Leaving empty. Use this if you want to perform
        //something at the end of Servlet life cycle.
    }
}

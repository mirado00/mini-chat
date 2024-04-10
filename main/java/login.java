

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public login() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String user = request.getParameter("user");
		String password = request.getParameter("password");

       response.setContentType("text/html");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h2>Informations user:</h2>");
        response.getWriter().println("<p>user: " + user + "</p>");
        response.getWriter().println("<p>password: " + password + "</p>");
        response.getWriter().println("</body></html>");
	}

}

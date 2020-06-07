import core.commands.BaseCmd;
import core.commands.factory.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private static Logger LOGGER = Logger.getLogger(AuthServlet.class.getName());
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.log(Level.INFO,
                String.format("Authentication request login({}) or password({})",
                        req.getParameter("login"), req.getParameter("pass")));
        if (req.getParameter("login").trim().length()==0||
                req.getParameter("pass").trim().length()==0){
            LOGGER.log(Level.WARNING, "Wrong format of login or password, forwarding to errorPage.jsp");

            req.setAttribute("Error","Wrong format of login or password");
            req.getRequestDispatcher("/errorPage.jsp").forward(req,resp);
        }
        else {
            String sign = req.getParameter("sign");
            if(sign == null){
                LOGGER.log(Level.WARNING, "Wrong authentication type, forwarding to errorPage.jsp");
                req.setAttribute("Error","Wrong authentication type");
                req.getRequestDispatcher("/errorPage.jsp").forward(req,resp);
                return;
            }

            CommandFactory factory= CommandFactoryImpl.getFactory();
            BaseCmd command;
            if(sign.equals("in")){
                LOGGER.log(Level.INFO, "Sign in request, getting Authorization cmd");

                command = factory.getCommand("Authorization", req, resp);
            }
            else{
                LOGGER.log(Level.INFO, "Sign in request, getting Registration cmd");

                command = factory.getCommand("Registration", req, resp);
            }
            command.execute();
        }
    }
}
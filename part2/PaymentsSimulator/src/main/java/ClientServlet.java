import core.commands.*;
import core.commands.factory.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/client")
public class ClientServlet extends HttpServlet{
    private static Logger LOGGER = Logger.getLogger(ClientServlet.class.getName());
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CommandFactory factory= CommandFactoryImpl.getFactory();
        BaseCmd command=factory.getCommand(req.getParameter("command"),req,resp);

        LOGGER.log(Level.INFO, String.format("doPost with {} cmd, preparing to execute", req.getParameter("command")));
        command.execute();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName=req.getParameter("command");
        if (commandName==null){
            LOGGER.log(Level.WARNING, "doGet with NULL cmd, forwarding to errorPage.jsp");
            req.setAttribute("Error","Wrong command!");
            req.getRequestDispatcher("/errorPage.jsp").forward(req,resp);
            return;
        }
        CommandFactory factory= CommandFactoryImpl.getFactory();
        BaseCmd command=factory.getCommand(commandName,req,resp);
        LOGGER.log(Level.INFO, String.format("doGet with {} cmd, preparing to execute", req.getParameter("command")));
        command.execute();
    }
}

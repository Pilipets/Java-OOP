package core.commands;

import bank_schema.User;

import javax.servlet.ServletException;
import java.io.IOException;

public class RegistrationCmd extends BaseCmd {
    @Override
    public void execute() throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("pass");
        String name = req.getParameter("name");
        if(db.checkIsUserExists(login)){
            req.setAttribute("Error","user with " + login + " login already exists");
            req.getRequestDispatcher("/errorPage.jsp").forward(req,resp);
        }
        else {
            User user = new User();
            user.setLogin(login);
            user.setName(name);
            user.setPassword(password);
            int id = db.registrateUser(user);
            user.setIsSuperUser(false);
            user.setId(id);
            req.getSession().setAttribute("User", user);
            req.getSession().setAttribute("registrated", true);
            resp.sendRedirect("/MyApp/client?command=Authorization");
        }
    }
}

package core.commands;

import bank_schema.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaymentsCommand extends BaseCmd{
    @Override
    public void execute() throws ServletException, IOException {
        HttpSession session = req.getSession();
        List<String> payments = new ArrayList<>();
        if(session.getAttribute("Payments") != null) {
            payments = (List<String>) session.getAttribute("Payments");
        }
        else{
            //get from bd
            User user = (User)session.getAttribute("User");
            if(user == null){
                throw new  ServletException();
            }
            List<Payment> allPayments = db.getPayments(user);
            for(Payment item : allPayments){
                payments.add("ID: " + item.getId() + ". Info: " + item.getInfo() +
                        ". Spended money: " + item.getMoney() + ". Card: " + item.getCard());
            }
        }
        if((boolean)session.getAttribute("block")){
            refreshBlocked();
        }
        session.setAttribute("Payments", payments);
        req.getRequestDispatcher("client.jsp").forward(req, resp);
        req.getSession().setAttribute("warning", "");
        req.getSession().setAttribute("topup", "");
    }
}

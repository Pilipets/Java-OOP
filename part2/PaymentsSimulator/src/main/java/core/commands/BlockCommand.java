package core.commands;

import bank_schema.*;
import java.io.IOException;

public class BlockCommand extends BaseCmd{
    @Override
    public void execute() throws IOException {
        String cardNumber = req.getParameter("card");
        Account account = getAccount(cardNumber);
        if(account == null){
            return;
        }
        account.setIsBlocked(true);
        db.blockAccount(account, true);
        req.getSession().setAttribute("warning", "Warning: blocked");

        req.getSession().setAttribute("block", true);
        resp.sendRedirect("/MyApp/client?command=Payments");
    }
}

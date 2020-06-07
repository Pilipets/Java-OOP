package core.commands;

import bank_schema.*;
import java.io.IOException;

public class TopUpCommand  extends BaseCmd {
    @Override
    public void execute() throws IOException {
        int money = Integer.valueOf(req.getParameter("money"));
        String cardNumber = req.getParameter("card");

        Account account = getAccount(cardNumber);

        if(account == null){
            return;
        }
        if(account.getIsBlocked()){
            req.getSession().setAttribute("warning", "Warning: Account is blocked");
        }
        else {
            account.setMoneyAmount(account.getMoneyAmount() + money);
            db.updateAccount(account);
            req.getSession().setAttribute("topup", "+" + money + "$ to account.");
        }
        resp.sendRedirect("/MyApp/client?command=Payments");
    }
}

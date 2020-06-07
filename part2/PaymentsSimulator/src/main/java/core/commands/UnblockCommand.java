package core.commands;

import bank_schema.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class UnblockCommand extends BaseCmd {
    @Override
    public void execute() throws IOException {
        String cardNumber = req.getParameter("card");
        Account account = db.getAccount(db.getCard(cardNumber));
        if(account == null){
            return;
        }
        db.blockAccount(account, false);
        List<Card> blockedCards = (List<Card>)req.getSession().getAttribute("blockedCards");
        for(int i = 0; i < blockedCards.size(); i++){
            if(blockedCards.get(i).getCardNumber().equals(cardNumber)){
                blockedCards.remove(i);
                break;
            }
        }
        try {
            req.getRequestDispatcher("admin.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}

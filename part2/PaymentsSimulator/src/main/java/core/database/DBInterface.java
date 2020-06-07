package core.database;

import bank_schema.*;

import java.util.List;

public interface DBInterface {

    User getUser(String username, String password);
    List<Card> getUsersCards(User user);
    List<Payment> getPayments(User user);
    int addPayment(Payment payment);
    Account getAccount(Card card);
    Card getCard(String cardNum);
    void updateAccount(Account account);
    List<Account> getAccounts(List<Card> cards);
    void blockAccount(Account account, boolean isBlocked);
    List<Card> getBlockedCards(List<Integer> accounts);
    int registrateUser(User user);
    Bank getBank(User user);
    boolean checkIsUserExists(String login);
}

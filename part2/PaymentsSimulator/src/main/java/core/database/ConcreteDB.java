package core.database;

import bank_schema.*;
import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConcreteDB implements DBInterface {
    private static Logger LOGGER = Logger.getLogger(ConcreteDB.class.getName());
    private static volatile ConcreteDB instance;
    private static DataSource dataSource;
    Connection connection;

    public void DBConnection(){
        LOGGER.log(Level.INFO, "Establishing DB connection");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            MysqlDataSource mysqlDS = new MysqlDataSource();
            mysqlDS.setURL("jdbc:mysql://localhost:3306/payments?serverTimezone=UTC");
            mysqlDS.setUser("root");
            mysqlDS.setPassword("root");

            LOGGER.log(Level.INFO, String.format("Connection params: {url:{}, \n, user:{}, password:{}",
                    mysqlDS.getUrl(), mysqlDS.getUser(), mysqlDS.getPassword()));
            dataSource = mysqlDS;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ConcreteDB(){
        DBConnection();
        connection = getConnection();
    }

    public static ConcreteDB getInstance() {
        if(instance == null){
            synchronized (ConcreteDB.class){
                LOGGER.log(Level.INFO, "Request for getting DB instance");
                if(instance == null){
                    LOGGER.log(Level.INFO, "DB instance not created yet, creating one");
                    instance = new ConcreteDB();
                }
            }
        }
        return instance;
    }

    private static synchronized Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "DB connection can't be established");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUser(String login, String password) {
        LOGGER.log(Level.INFO, String.format("DB getUser request with params {{}, {}}",
                login, password));
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from clients where login=? and password=?");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setIsSuperUser(resultSet.getBoolean("isSuperUser"));
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setLogin(login);
                user.setPassword(password);
                LOGGER.log(Level.INFO, String.format("Found result {}",
                        user.toString()));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkIsUserExists(String login) {
        LOGGER.log(Level.INFO, String.format("DB check if user exists, login={}",
                login));
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from clients where login=?");
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                LOGGER.log(Level.INFO, "User found");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public List<Card> getUsersCards(User user) {
        try {
            LOGGER.log(Level.INFO, String.format("DB getUsersCards request, user={}",
                    user.toString()));
            int userId = user.getId();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from cards where clientId = ?");
            preparedStatement.setString(1, String.valueOf(userId));
            List<Card> cards = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Card card = new Card();
                card.setCardNumber(String.valueOf(resultSet.getInt("cardNumber")));
                card.setPin(resultSet.getInt("pin"));
                card.setClientId(resultSet.getInt("clientId"));
                card.setAccountId(resultSet.getInt("accId"));
                cards.add(card);
            }
            LOGGER.log(Level.INFO, String.format("Found result {}",
                    cards.toString()));
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payment> getPayments(User user) {
        try {
            LOGGER.log(Level.INFO, String.format("DB getPayments request, user={}",
                    user.toString()));
            int userId = user.getId();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from payments where clientId = ?");
            preparedStatement.setString(1, String.valueOf(userId));
            List<Payment> payments = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Payment payment = new Payment();
                payment.setId(resultSet.getInt("id"));
                payment.setCard(resultSet.getInt("cardNumber"));
                payment.setMoney(resultSet.getInt("money"));
                payment.setInfo(resultSet.getString("info"));
                payment.setClient(resultSet.getInt("clientId"));
                payments.add(payment);
            }
            LOGGER.log(Level.INFO, String.format("Found result {}",
                    payments.toString()));
            return payments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int addPayment(Payment payment) {
        try {
            LOGGER.log(Level.INFO, String.format("DB addPayment request, payment={}",
                    payment.toString()));
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into payments(money, clientId, cardNumber, info)" +
                            " values(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, payment.getMoney());
            preparedStatement.setInt(2, payment.getClient());
            preparedStatement.setInt(3, payment.getCard());
            preparedStatement.setString(4, payment.getInfo());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                LOGGER.log(Level.INFO, String.format("Payment added with id={}",
                        rs.getLong(1)));
                return (int)rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Account getAccount(Card card) {
        try {
            LOGGER.log(Level.INFO, String.format("DB getAccount request, card={}",
                    card.toString()));
            int accId = card.getAccountId();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from accounts where id = ?");
            preparedStatement.setString(1, String.valueOf(accId));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getInt("id"));
                account.setMoneyAmount(resultSet.getInt("moneyAmount"));
                account.setIsBlocked(resultSet.getBoolean("isBlocked"));
                LOGGER.log(Level.INFO, String.format("Found result {}",
                        account.toString()));
                return account;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Card getCard(String cardNum) {
        try {
            LOGGER.log(Level.INFO, String.format("DB getCard request, cardNum={}",
                    cardNum));
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from cards where cardNumber = ?");
            preparedStatement.setString(1, String.valueOf(cardNum));
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Card card = new Card();
                card.setCardNumber(String.valueOf(resultSet.getInt("cardNumber")));
                card.setPin(resultSet.getInt("pin"));
                card.setClientId(resultSet.getInt("clientId"));
                card.setAccountId(resultSet.getInt("accId"));
                LOGGER.log(Level.INFO, String.format("Found result {}",
                        card.toString()));
                return card;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateAccount(Account account) {
        try {
            LOGGER.log(Level.INFO, String.format("DB updateAccount request, account={}",
                    account.toString()));
            PreparedStatement preparedStatement =
                    connection.prepareStatement("update accounts set moneyAmount = ? where id = ?;");
            preparedStatement.setString(1, String.valueOf(account.getMoneyAmount()));
            preparedStatement.setString(2, String.valueOf(account.getId()));
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Update executed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void blockAccount(Account account, boolean isBlock) {
        try {
            LOGGER.log(Level.INFO, String.format("DB blockAccount request, account={}, isBlock={}",
                    account.toString(), isBlock));
            PreparedStatement preparedStatement =
                    connection.prepareStatement("update accounts set isBlocked = ? where id = ?;");
            preparedStatement.setBoolean(1, isBlock);
            preparedStatement.setString(2, String.valueOf(account.getId()));
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Update executed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Card> getBlockedCards(List<Integer> accounts) {
        try {
            LOGGER.log(Level.INFO, String.format("DB getBlockedCards for accounts={}",
                    accounts.toString()));
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from cards join accounts on accId = id where isBlocked = 1;");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Card> cards = new ArrayList<>();
            while (resultSet.next()) {
                Integer accId = resultSet.getInt("accId");
                if(accounts.contains(accId)) {
                    Card card = new Card();
                    card.setCardNumber(String.valueOf(resultSet.getInt("cardNumber")));
                    card.setPin(resultSet.getInt("pin"));
                    card.setClientId(resultSet.getInt("clientId"));
                    card.setAccountId(accId);
                    cards.add(card);
                }
            }
            LOGGER.log(Level.INFO, String.format("Found result cards={}",
                    cards.toString()));
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int registrateUser(User user) {
        try {
            LOGGER.log(Level.INFO, String.format("DB registrateUser request, user={}",
                    user.toString()));
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into clients(name, login, password, isSuperUser)" +
                            " values(?, ?, ?, 0)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                LOGGER.log(Level.INFO, String.format("Registrated user with id={}",
                        rs.getLong(1)));
                return (int)rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Bank getBank(User user) {
        try {
            LOGGER.log(Level.INFO, String.format("DB getBank request, user={}",
                    user.toString()));
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select * from banks where admins like ?");
            preparedStatement.setString(1, "%" + user.getId() + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Bank bank = new Bank();
                bank.setId(resultSet.getInt("id"));
                bank.setName(resultSet.getString("name"));
                String[] accounts = resultSet.getString("accounts").split(",");
                String[] admins = resultSet.getString("admins").split(",");

                for(String item : accounts){
                    bank.setAccounts(Integer.valueOf(item));
                }
                for(String item : admins){
                    bank.setAdmins(Integer.valueOf(item));
                }
                LOGGER.log(Level.INFO, String.format("Found result {}",
                        bank.toString()));
                return bank;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Account> getAccounts(List<Card> cards) {
        try {
            LOGGER.log(Level.INFO, String.format("DB getAccounts request, cards={}",
                    cards.toString()));
            List<Integer> accIds = new ArrayList<>();
            for(Card item : cards){
                accIds.add(item.getAccountId());
            }
            String sql = "select * from accounts where ";
            for(Integer id :  accIds){
                sql += "id = " + id + " or ";
            }
            sql = sql.substring(0, sql.length() - 3);
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getInt("id"));
                account.setMoneyAmount(resultSet.getInt("moneyAmount"));
                account.setIsBlocked(resultSet.getBoolean("isBlocked"));
                accounts.add(account);
                LOGGER.log(Level.INFO, String.format("Found result {}",
                        account.toString()));
            }
            return accounts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}

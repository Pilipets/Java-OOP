package com.webbank.service;

import com.webbank.dao.*;
import com.webbank.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;

@Service
@Transactional
public class BusinessServiceImpl {
    @Resource
    private AccountDao accountDao;
    @Resource
    private BankDao bankDao;
    @Resource
    private PaymentDao paymentDao;
    @Resource
    private CardDao cardDao;
    @Resource
    private TopupDao topupDao;
    @Resource
    private IntervalsDao intervalsDao;
    @Resource
    private BankStatsDao bankStatsDao;

    @Resource
    private PeriodStatsDao periodStatsDao;

    public BankStats getBankStats(Bank bank) {
        return bankStatsDao.getBankStatsByBank(bank);
    }
    public List<Card> getUsersCards(User user) {
        return cardDao.getUsersCards(user.getId());
    }
    public List<Payment> getPayments(User user) {
        return paymentDao.getPayments(user.getId());
    }
    public int addPayment(Payment payment) {
         paymentDao.save(payment);
         return payment.getId();
    }
    public Account getAccount(Card card) {
        return accountDao.getAccount(card.getAccountId());
    }
    public Card getCard(int cardNum) {
        return cardDao.getCard(cardNum);
    }
    public void updateAccount(Account account) {
        Account accountNew = accountDao.getAccount(account.getId());
        accountNew.setMoneyAmount(account.getMoneyAmount());
        accountNew.setIsBlocked(account.getIsBlocked());
        accountDao.save(accountNew);
    }
    public List<Account> getAccounts(List<Card> cards) {
        List<Account> accounts = new ArrayList<>();
        for(Card card : cards){
            accounts.add(accountDao.getAccount(card.getAccountId()));
        }
        return accounts;
    }
    public void blockAccount(Account account, boolean isBlocked) {
        account.setIsBlocked(isBlocked);
        updateAccount(account);
    }
    public List<Card> getBlockedCards(List<Integer> accounts) {
        return cardDao.getBlockedCards(accounts);
    }
    public List<Card> getAllCards(List<Integer> accounts) {
        return cardDao.getAllCards(accounts);
    }
    public Bank getBank(User user) {
        return bankDao.getBank(user.getId());
    }

    public void payCommand(Map<Card, Account> cardAccountMap, List<String> payments, User user, int money, String info, int cardNumber, ModelMap model) throws IOException {

        Account account = getAccountCommand(cardAccountMap, model, cardNumber);

        if(account == null){
            return;
        }
        Bank bank = bankDao.getBankByAccount(account.getId());

        int accountMoney = account.getMoneyAmount();
        int profit_val = (int)(money*bank.getTransferFee());
        if(account.getIsBlocked()){
            model.addAttribute("warning", "Warning: Account is blocked");
        }
        else if(accountMoney < money + profit_val ){
            model.addAttribute("warning", "Warning: not enough money");
        }
        else {
            BankStats stats = bankStatsDao.getBankStatsByBank(bank);
            stats.setTotalTransfer(stats.getTotalTransfer().add(BigInteger.valueOf(money)));
            stats.setTotalProfit(stats.getTotalProfit().add(BigInteger.valueOf(profit_val)));
            bankStatsDao.save(stats);

            money += profit_val;
            account.setMoneyAmount(accountMoney - money);
            updateAccount(account);

            if(payments == null){
                payments = new ArrayList<>();
            }

            Payment payment = new Payment();
            payment.setMoney(money);
            payment.setInfo(info);
            payment.setCard(cardNumber);
            Date now = new Date();
            payment.setDate(now);
            payment.setClient(user.getId());
            int paymentId = addPayment(payment);
            payments.add("ID: " + paymentId + ". Info: " + info +
                    ". Spended money: " + money + ". Card: " + cardNumber + ". Date: " + now);
            model.addAttribute("payments", payments);
            model.addAttribute("testPayment", info + " " + money + " " + (accountMoney - money)
                    + " " +  cardNumber);//for test
        }
    }

    public void topUpCommand(Map<Card, Account> cardAccountMap, int money, int cardNumber, ModelMap model) throws IOException {
        Account account = getAccountCommand(cardAccountMap, model, cardNumber);

        if(account == null){
            return;
        }

        if(account.getIsBlocked()){
            model.addAttribute("warning", "Warning: Account is blocked");
        }
        else {
            Bank bank = bankDao.getBankByAccount(account.getId());
            int profit_val = (int)(money*bank.getTopUpFee());

            BankStats stats = bankStatsDao.getBankStatsByBank(bank);
            stats.setTotalReplenish(stats.getTotalReplenish().add(BigInteger.valueOf(money)));
            stats.setTotalProfit(stats.getTotalProfit().add(BigInteger.valueOf(profit_val)));
            bankStatsDao.save(stats);

            money -= profit_val;
            account.setMoneyAmount(account.getMoneyAmount() + money);
            updateAccount(account);

            Topup topUp = new Topup();
            topUp.setCardNumber(cardNumber);
            topUp.setMoney(money);
            Date now = new Date();
            topUp.setDate(now);
            topupDao.save(topUp);

            model.addAttribute("topup", "+" + money + "$ to account.");
        }
    }

    public void blockCommand(Map<Card, Account> cardAccountMap, int cardNumber, ModelMap model) throws IOException {
        Account account = getAccountCommand(cardAccountMap, model, cardNumber);
        if(account == null){
            return;
        }
        account.setIsBlocked(true);
        blockAccount(account, true);

        refreshBlocked(cardAccountMap, model);
        model.addAttribute("warning", "Warning: blocked");
        model.addAttribute("block", true);
    }

    public void unblockCommand(int cardNumber, List<Card> blockedCards) {

        Account account = getAccount(getCard(cardNumber));
        if(account == null){
            return;
        }
        blockAccount(account, false);
       // List<Card> blockedCards = (List<Card>)req.getSession().getAttribute("blockedCards");
        for(int i = 0; i < blockedCards.size(); i++){
            if(blockedCards.get(i).getCardNumber() == cardNumber){
                blockedCards.remove(i);
                break;
            }
        }
    }

    public void dateCommand(Date dateFrom, Date dateTo, int cardNumber, ModelMap model) {
        String candles = "";
        Calendar cal = Calendar.getInstance();


        Date leftDate = null, rightDate = null;
        int maxValue = 0;
        /*List<Intervals> intervals = intervalsDao.getIntervals(dateFrom, dateTo);
        if(intervals != null && intervals.size() > 0){
            candles = intervals.get(0).getMoney();
            rightDate = intervals.get(0).getRightDate();
            leftDate = intervals.get(0).getLeftDate();
            maxValue = intervals.get(0).getMaxValue();
        }
        else {*/
            List<Payment> paymentsDate = paymentDao.getPaymentsByDate(cardNumber, dateFrom, dateTo);
            List<Topup> topupDate = topupDao.getTopupByDate(cardNumber, dateFrom, dateTo);

            int paymentIter = 0, topupIter = 0;
            while (paymentIter < paymentsDate.size() && topupIter < topupDate.size()) {
                if (paymentsDate.get(paymentIter).getDate().compareTo(topupDate.get(topupIter).getDate()) < 0) {
                    int money = paymentsDate.get(paymentIter).getMoney();
                    candles += ("-" + money + ",");
                    if (maxValue < money) {
                        maxValue = money;
                    }
                    if (leftDate == null) {
                        leftDate = paymentsDate.get(paymentIter).getDate();
                    }
                    rightDate = paymentsDate.get(paymentIter).getDate();
                    paymentIter++;
                } else {
                    int money = topupDate.get(topupIter).getMoney();
                    candles += (money + ",");
                    if (maxValue < money) {
                        maxValue = money;
                    }
                    if (leftDate == null) {
                        leftDate = topupDate.get(topupIter).getDate();
                    }
                    rightDate = topupDate.get(topupIter).getDate();
                    topupIter++;
                }
            }
            while (paymentIter < paymentsDate.size()) {
                int money = paymentsDate.get(paymentIter).getMoney();
                candles += ("-" + money + ",");
                if (maxValue < money) {
                    maxValue = money;
                }
                if (leftDate == null) {
                    leftDate = paymentsDate.get(paymentIter).getDate();
                }
                rightDate = paymentsDate.get(paymentIter).getDate();
                paymentIter++;
            }
            while (topupIter < topupDate.size()) {
                int money = topupDate.get(topupIter).getMoney();
                candles += (money + ",");
                if (maxValue < money) {
                    maxValue = money;
                }
                if (leftDate == null) {
                    leftDate = topupDate.get(topupIter).getDate();
                }
                rightDate = topupDate.get(topupIter).getDate();
                topupIter++;
            }
            candles = candles.substring(0, candles.length() - 1);


            cal.setTime(dateFrom);
            cal.add(Calendar.HOUR, -12);
            dateFrom = cal.getTime();

            cal.setTime(dateTo);
            cal.add(Calendar.HOUR, 48);
            dateTo = cal.getTime();
            cal.setTime(rightDate);
            cal.add(Calendar.HOUR, 12);
            rightDate = cal.getTime();
            cal.setTime(leftDate);
            cal.add(Calendar.HOUR, 12);
            leftDate = cal.getTime();
            Intervals interval = new Intervals();
            interval.setDateFrom(dateFrom);
            interval.setDateTo(dateTo);
            interval.setLeftDate(leftDate);
            interval.setRightDate(rightDate);
            interval.setMaxValue(maxValue);
            interval.setMoney(candles);
            intervalsDao.save(interval);
        //}


       //-candles += ("\'");
        model.addAttribute("candles", candles);
        model.addAttribute("maxValue", maxValue);
        if(leftDate != null) {
            model.addAttribute("leftDate", leftDate.toString());
        }
        if(rightDate != null) {
            model.addAttribute("rightDate", rightDate.toString());
        }
    }

    public void dateStatsCommand(Date dateFrom, Date dateTo, String bankName, ModelMap model) {
        Bank bank = bankDao.findBankByName(bankName);

        List<Account> accounts = bank.getAccounts();
        List<Integer> accountsId = new ArrayList<>();
        for(Account account : accounts){
            accountsId.add(account.getId());
        }

        List<Card> cards = cardDao.getAllCards(accountsId);
        List<Integer> cardsNum = new ArrayList<>();
        for(Card card : cards){
            cardsNum.add(card.getCardNumber());
        }
        Integer paymentsSum = paymentDao.getSumByDate(cardsNum, dateFrom, dateTo);
        Integer topUpSum = topupDao.getSumByDate(cardsNum, dateFrom, dateTo);
        if(paymentsSum != null) {
            model.addAttribute("paymentsSum", paymentsSum);
        }
        if(topUpSum != null) {
            model.addAttribute("topUpSum", topUpSum);
        }
        if(paymentsSum != null && topUpSum != null) {
            DecimalFormat df = new DecimalFormat("#.###");
            Double statsKoef = Double.valueOf(df.format((double) topUpSum / paymentsSum));
            model.addAttribute("inToOut", statsKoef);

            /*Payment payment = paymentDao.getFirstAfterDate(dateFrom);
            Topup topUp = topupDao.getFirstAfterDate(dateFrom);
            Date leftDate = payment.getDate();
            if(topUp.getDate().before(leftDate))
                leftDate = topUp.getDate();
            payment = paymentDao.getLastBeforeDate(dateTo);
            topUp = topupDao.getLastBeforeDate(dateTo);
            Date rightDate = payment.getDate();
            if(topUp.getDate().after(rightDate))
                rightDate = topUp.getDate();*/

            PeriodStats stats = new PeriodStats();
            stats.setDateFrom(dateFrom);
            stats.setDateTo(dateTo);
            stats.setInToOut(statsKoef);
            stats.setTotalReplenish(BigInteger.valueOf(topUpSum));
            stats.setTotalTransfer(BigInteger.valueOf(paymentsSum));
            periodStatsDao.save(stats);
        }
    }

    public void refreshBlocked(Map<Card, Account> cardAccountMap, ModelMap model){
        List<Card> cards = new ArrayList<>(cardAccountMap.keySet());
        List<Account> accounts = new ArrayList<>(cardAccountMap.values());
        List<Account> accountsBd = getAccounts(cards);
        boolean isSomeBlocked = false;
        for(int i = 0; i < accounts.size(); i++){
            for(int j = 0; j < accountsBd.size(); j++){
                Account old = accounts.get(i);
                Account fresh = accountsBd.get(i);
                if(old.getId() == fresh.getId()){
                    if(fresh.getIsBlocked()){
                        isSomeBlocked = true;
                    }
                    old.setIsBlocked(fresh.getIsBlocked());
                    break;
                }
            }
        }
        if(!isSomeBlocked){
            model.addAttribute("block", false);
        }
    }

    public Account getAccountCommand( Map<Card, Account> cardAccountMap, ModelMap model, int cardNumber) throws IOException {
        if(cardNumber == 0){
            model.addAttribute("warning", "Warning: No card. Contact to support");
            return null;
        }

        List<Card> cards = new ArrayList<>(cardAccountMap.keySet());

        Card card = null;
        for(Card item : cards){
            if(item.getCardNumber( )== cardNumber){
                card = item;
                break;
            }
        }
        if(card == null){
            return null;
        }
        Account account = cardAccountMap.get(card);
        if(account == null){
            model.addAttribute("warning",
                    "Warning: no account with " + cardNumber + ". Contact to support");
            model.addAttribute("warning", "Warning: no account with " + cardNumber + ". Contact to support");

            //resp.sendRedirect("/test/client?command=Payments");
            return null;
        }
        return account;
    }
}

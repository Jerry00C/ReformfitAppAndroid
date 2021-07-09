package com.example.reformfitapp.purchaseFragment;

import com.example.reformfitapp.CreditCardInfo;

public class PurchaseSucceededPassBackData {



    private int paymentOption;
    private StoredCreditInfo storedCreditInfo;
    private DirectDebitInfo directDebitInfo;
    private NewCreditCardInfo newCreditCardInfo;
    private NewDirectDebitInfo newDirectDebitInfo;
    public static final int typeStoredCredit = 0;
    public static final int typeDirectDebit = 1;

    public PurchaseSucceededPassBackData(String cardNumber, String lastFour, String expMonth, String expYear) {
        this.storedCreditInfo = new StoredCreditInfo(cardNumber, lastFour, expMonth, expYear);
        paymentOption = typeStoredCredit;
    }

    public PurchaseSucceededPassBackData(String branchNumber, String transitNumber, String accountNumber) {
        this.directDebitInfo = new DirectDebitInfo(branchNumber, transitNumber, accountNumber);
        paymentOption = typeDirectDebit;
    }

    public PurchaseSucceededPassBackData(CreditCardInfo cardInfo) {
        this.newCreditCardInfo = new NewCreditCardInfo(cardInfo);
    }

    public PurchaseSucceededPassBackData(boolean test, String clientId, String nameOnAccount, String branchNumber, String transitNumber, String accountNumber, String acountType) {
        this.newDirectDebitInfo = new NewDirectDebitInfo(test, clientId, nameOnAccount, branchNumber, transitNumber, accountNumber, acountType);
    }

    public int getPaymentOption() {
        return paymentOption;
    }

    public StoredCreditInfo getStoredCreditInfo() {
        return storedCreditInfo;
    }

    public DirectDebitInfo getDirectDebitInfo() {
        return directDebitInfo;
    }

    public NewCreditCardInfo getNewCreditCardInfo() {
        return newCreditCardInfo;
    }

    public class StoredCreditInfo{
        private final String cardNumber;
        private final String lastFour;
        private final String expMonth;
        private final String expYear;

        public StoredCreditInfo(String cardNumber, String lastFour, String expMonth, String expYear) {
            this.cardNumber = cardNumber;
            this.lastFour = lastFour;
            this.expMonth = expMonth;
            this.expYear = expYear;
        }

        public String getCardNumber() {
            return cardNumber;
        }


        public String getLastFour() {
            return lastFour;
        }



        public String getExpMonth() {
            return expMonth;
        }


        public String getExpYear() {
            return expYear;
        }
    }

    public class DirectDebitInfo{
        private final String branchNumber;
        private final String transitNumber;
        private final String accountNumber;

        public DirectDebitInfo(String branchNumber, String transitNumber, String accountNumber) {
            this.branchNumber = branchNumber;
            this.transitNumber = transitNumber;
            this.accountNumber = accountNumber;
        }

        public String getBranchNumber() {
            return branchNumber;
        }

        public String getTransitNumber() {
            return transitNumber;
        }

        public String getAccountNumber() {
            return accountNumber;
        }
    }

    public class NewCreditCardInfo{

        private final CreditCardInfo cardInfo;

        public NewCreditCardInfo(CreditCardInfo cardInfo) {
            this.cardInfo = cardInfo;
        }

        public CreditCardInfo getCardInfo() {
            return cardInfo;
        }
    }

    public class NewDirectDebitInfo{
        private final boolean test;
        private final String clientId;
        private final String nameOnAccount;
        private final String branchNumber;
        private final String transitNumber;
        private final String accountNumber;
        private final String acountType;

        public NewDirectDebitInfo(boolean test, String clientId, String nameOnAccount, String branchNumber, String transitNumber, String accountNumber, String acountType) {
            this.test = test;
            this.clientId = clientId;
            this.nameOnAccount = nameOnAccount;
            this.branchNumber = branchNumber;
            this.transitNumber = transitNumber;
            this.accountNumber = accountNumber;
            this.acountType = acountType;
        }

        public boolean isTest() {
            return test;
        }

        public String getClientId() {
            return clientId;
        }

        public String getNameOnAccount() {
            return nameOnAccount;
        }

        public String getBranchNumber() {
            return branchNumber;
        }

        public String getTransitNumber() {
            return transitNumber;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getAcountType() {
            return acountType;
        }
    }


}

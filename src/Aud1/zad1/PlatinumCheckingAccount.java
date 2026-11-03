package Aud1.zad1;


public class PlatinumCheckingAccount extends Account implements InterestBearingAccount {

    public static final double INT_RATE = 0.06;

    public PlatinumCheckingAccount(String name, double balance) {
        super(name, balance);
    }

    @Override
    public void addInterest() {
        deposit(getBalance() * INT_RATE);
    }
}
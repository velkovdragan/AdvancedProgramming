package Aud1.zad1;

public class PlatinumCheckingAccount extends InterestCheckingAccount{
    public static final double INTEREST_RATE = .06;
    public PlatinumCheckingAccount(String name,double balance){
        super(name,balance);
    }

    @Override
    public void addInterest(){
        deposit(get_balance()*InterestCheckingAccount.INTEREST_RATE * 2);
    }
}

package Aud1.zad1;

public class InterestCheckingAccount extends Account implements InterestBearingAccount{

    public static final double INTEREST_RATE = .03;
    public InterestCheckingAccount(String name,double balance){
        super(name,balance);
    }

    @Override
    public void addInterest(){
        deposit(get_balance()*INTEREST_RATE);
    }


}

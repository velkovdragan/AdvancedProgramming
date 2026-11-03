package Aud1.zad1;

public class Bank{
    private Account[] accounts;
    private int totalAccs;
    private int max=0;

    public Bank(int max){
        this.max=max;
        this.totalAccs=0;
        accounts=new Account[max];
    }

    public double totalAssets(){
        double sum=0;
        for (int i = 0; i < totalAccs; i++) {
            sum += accounts[i].getBalance();
        }
        return sum;
    }

    public void addAccount(Account account) {
        if (totalAccs < accounts.length && account != null) {
            accounts[totalAccs++] = account;
        }
    }


    public void addInterest(){
        for(Account account: accounts){
            if(account instanceof InterestBearingAccount){
                InterestBearingAccount i=(InterestBearingAccount) account;
                i.addInterest();
            }
        }
    }

}
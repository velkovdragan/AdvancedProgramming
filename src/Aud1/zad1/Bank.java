package Aud1.zad1;

public class Bank{
        Account[] accounts;
        int numAcc;

        Bank(){
            this.accounts=new Account[10];
            numAcc=0;

        }

    public double getTotalAssets() {
        double sum = 0;
        for(int i = 0; i < numAcc; i++) {
            sum += accounts[i].get_balance();
        }

        return sum;
    }
        public void addInterest(){
            for(int i=0;i<numAcc;i++){
                if(accounts[i] instanceof InterestBearingAccount){
                    InterestBearingAccount iba=(InterestBearingAccount)accounts[i];
                    iba.addInterest();
                }
            }
        }
    public void addAccount(Account account) {
        accounts[numAcc++] = account;
    }





}

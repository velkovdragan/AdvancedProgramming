package Aud1.zad1;

public abstract class Account {

    public static int ACC_NUMBER=1;
    private String name;
    private int number;
    private double balance;


    public Account(String name,double balance){
        this.name=name;
        this.number=ACC_NUMBER++;
        this.balance=balance;
    }

    public double get_balance(){
        return this.balance;
    }




    public void deposit(double money){
        System.out.println("Deposit: " + money +  " to account " + number);
        this.balance+=money;
    }

    public void withdraw(double money){

        if(balance>=money){
            System.out.println("Withdrawal: " + money +  " from account " + number);

            this.balance-=money;

        }

    }


}

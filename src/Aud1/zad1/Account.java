package Aud1.zad1;

public abstract class Account{
    private String name;

    private double balance;
    private int number;
    static int counter=0;


    public Account(String name,double balance){
        this.name=name;

        this.balance=balance;

        this.number=counter++;


    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public int getNumber() {
        return number;
    }

    public void deposit(double amount){
        balance+=amount;
    }
    public void withdraw(double amount){
        balance-=amount;
    }
}
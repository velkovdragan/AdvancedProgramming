package Aud1.zad1;



public class Main {

    public static void main(String[] args) {

        // Create a bank with capacity for 10 accounts
        Bank bank = new Bank(10);

        // Create different types of accounts
        Account acc1 = new NonInterestCheckingAccount("John", 1000);
        Account acc2 = new InterestCheckingAccount("Alex", 2000);
        Account acc3 = new PlatinumCheckingAccount("Sarah", 5000);

        // Add accounts to the bank
        bank.addAccount(acc1);
        bank.addAccount(acc2);
        bank.addAccount(acc3);

        // Display account information before interest
        System.out.println("=== BEFORE INTEREST ===");

        System.out.println(acc1.getName() + ": " + acc1.getBalance());
        System.out.println(acc2.getName() + ": " + acc2.getBalance());
        System.out.println(acc3.getName() + ": " + acc3.getBalance());

        System.out.println("Total bank assets: " + bank.totalAssets());

        // Apply interest to eligible accounts
        bank.addInterest();

        // Display account information after interest
        System.out.println("\n=== AFTER INTEREST ===");

        System.out.println(acc1.getName() + ": " + acc1.getBalance());
        System.out.println(acc2.getName() + ": " + acc2.getBalance());
        System.out.println(acc3.getName() + ": " + acc3.getBalance());

        System.out.println("Total bank assets: " + bank.totalAssets());
    }
}
package Aud1.zad1;

public class Main {
    public static void main(String[] args) {
        int num = 1;
        String name = "Test Customer";
        float balance = 5000;

        Bank b = new Bank();

        Account a1 = new NonInterestCheckingAccount(name, balance);
        Account a2 = new InterestCheckingAccount(name, balance);
        Account a3 = new PlatinumCheckingAccount(name, balance);

        b.addAccount(a1);
        b.addAccount(a2);
        b.addAccount(a3);

        System.out.println(a1);
        System.out.println(a2);

        double totalAssets = b.getTotalAssets();
        System.out.println("Total Assets: " + totalAssets);

        b.addInterest();

        System.out.println(a1);
        System.out.println(a2);

    }
}

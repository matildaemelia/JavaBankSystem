package matzet4;
/**
 * @author Matilda Zettergren, matzet-4
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
	private String name;
	private String surname;
	private String personalNumber;
	private List<Account> accounts;
	
	// Kontruktor
	public Customer(String name, String surname, String personalNumber) {
		this.name = name;
		this.surname = surname;
		this.personalNumber = personalNumber;
		this.accounts = new ArrayList<>();
	}
	
	public void addAccount(Account account) {
		accounts.add(account);
	}
	
	// Metod för att få hela namnet
	public String getFullName() {
		return name + " " + surname;
	}
	
	// Getter för personnummret
	public String getPersonalNumber() {
		return personalNumber;
	}
	
	// Getter och setter för förnamnet
	public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    // Getter och setter för efternamnet
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    // Lista alla accounts
    public List<Account> getAccounts() {
    	return accounts;
    }
    
    // Ändrar namnet om det inte är tomt
    public boolean changeName(String newName, String newSurname) {
    	if (!newName.isEmpty()) {
    		this.name = newName;
    	}
    	if (!newSurname.isEmpty()) {
    		this.surname = newSurname;
    	}
    	return false;
    }
    
    // Skapar ett nytt sparkonto samt lägger till det i lista med kundens konton
    public Account createSavingsAccount() {
    	SavingsAccount account = new SavingsAccount();
    	accounts.add(account);
    	return account;
    }
    
    // Skapar ett nytt kreditkonto samt lägger till det i lista med kundens konton
    public Account createCreditAccount() {
    	CreditAccount account = new CreditAccount();
    	accounts.add(account);
    	return account;
    }
    
    // Hittar och returnerar account med accountNumber
    public Account getAccount(int accountNumber) {
    	for (Account account : accounts) {
    		if (account.getAccountNumber() == accountNumber) {
    			return account;
    		}
    	}
    	return null;
    }
    
    // Tar bort account från listan
    public Account closeAccount(int accountNumber) {
    	Account account = getAccount(accountNumber);
    	if (account != null) {
    		accounts.remove(account);
    	}
    	return account;
    }
    
    // Returnerar String med personnummer och namn
    public String toString() {
    	return personalNumber + " " + getFullName();
    }
}

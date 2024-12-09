package matzet4;
/**
 * @author Matilda Zettergren, matzet-4
 */
import java.util.ArrayList;
import java.util.List;

public class BankLogic {
	
	// Lista med alla kunder
	private static List<Customer> customers;
	
	// Konstruktor
	public BankLogic() {
		customers = new ArrayList<>();
	}
	
	// Hämtar alla kunder genom att skapa en ny lista med kunderna och sedan addera dessa
	public List<String> getAllCustomers() {
		List<String> customerList = new ArrayList<>();
		for (Customer customer : customers) {
            customerList.add(customer.toString());
        }
        return customerList;
	}
	
	// Metod för att lägga till en ny kund
	public static boolean createCustomer(String name, String surname, String personalNumber) {
		if (findCustomer(personalNumber) == null) { // Om inte personnummret finns med i listan så skapas en ny kund
            customers.add(new Customer(name, surname, personalNumber));
            return true;
        }
        return false;
	}
	
	// Metod för att ändra en kunds namn
	public boolean changeCustomerName(String newName, String newSurname, String personalNumber) {
        Customer customer = findCustomer(personalNumber); // Hitta kund med detta personnummer
        // Om kund inte är null och om förnamn och efternamn inte är null eller tomt så ändras namnet med setter i customer-klassen.
        if (customer != null) {
        	if (newName != null && !newName.isEmpty()) {
                customer.setName(newName); // Uppdatera förnamn om det inte är tomt
            }
        	if (newSurname != null && !newSurname.isEmpty()) {
                customer.setSurname(newSurname); // Uppdatera förnamn om det inte är tomt
            }
        	if ((newName == null || newName.isEmpty()) && (newSurname == null || newSurname.isEmpty())) {
                return false;
            }
        	return true;
        }
        return false;
	}

	// Metod för att hämta och returnera info om en kund
	public List<String> getCustomer(String personalNumber) {
		// Om kunden inte hittas så returneras null
		 Customer customer = findCustomer(personalNumber); // Hitta kund med detta personnummer
		 if (customer == null) {
			 return null;
		 }
		 List<String> customerInfo = new ArrayList<>(); // Om kunden hittas lagras infon
	     customerInfo.add(customer.toString()); // En sträng med kundens info läggs till i customerInfo
	     // En for-loop går igenom alla konton som tillhör kunden, varje konto-info läggs till i custsomerInfo.
	     for (Account account : customer.getAccounts()) {
	    	 customerInfo.add(account.toString());
	    }
	    return customerInfo;
	}
	
	public int createCreditAccount(String personalNumber) {
		Customer customer = findCustomer(personalNumber);
		if (customer != null) {
			CreditAccount account = new CreditAccount();
			customer.getAccounts().add(account);
			return account.getAccountNumber();
		}
		return -1;
	}
	
	public int createSavingsAccount(String personalNumber) {
        Customer customer = findCustomer(personalNumber); // Hitta kund med detta personnummer
        // Om kunden inte är null så skapas ett sparkonto och det nya kontot returneras.
        if (customer != null) {
        	SavingsAccount account = new SavingsAccount();
			customer.getAccounts().add(account);
            return account.getAccountNumber();
        }
        return -1;
	}
	
	// Metod för att hämta ett konto
	public String getAccount(String personalNumber, int accountId) {
        Customer customer = findCustomer(personalNumber); // Hämta kund med personnummret
        // Om kunden finns så hämtas kontot med accountId och om kontot finns så returnes en sträng med kontot, annars returneras null.
        if (customer != null) {
            Account account = customer.getAccount(accountId);
            if (account != null) {
            	return account.toString();
            }
        }
        return null;
	}
	
	// Metod för att lägga in pengar i kontot
	public boolean deposit(String personalNumber, int accountId, int amount) {
        Customer customer = findCustomer(personalNumber); // Hitta kund med detta personnummret
        // Om kunden finns och om beloppet inte är mer än 0 så returneras false.
        if (customer != null) {
        	if (amount < 0) {
        		return false;
        	}
            Account account = customer.getAccount(accountId); // Hämta kundens konto med accountId
            // Om kontot finns så läggs beloppet på i kontot.
            if (account != null) {
                account.deposit(amount);
                return true;
            }
        }
        return false;
	}
	
	// Metod för att ta ut pengar
	public boolean withdraw(String personalNumber, int accountId, int amount) {
        Customer customer = findCustomer(personalNumber); // Hitta kund med detta personnummer
        // Om kund finns så hämtas kontot med accountId
        if (customer != null) {
            Account account = customer.getAccount(accountId);
            // om kontot finns så dras beloppet av
            if (account != null) {
            	return account.withdraw(amount);
            }
        }
        return false;
	}
	
	// Metod för att hitts en kund med personnummer
	private static Customer findCustomer(String personalNumber) {
		for (Customer customer : customers) {
			if (customer.getPersonalNumber().equals(personalNumber)) {
				return customer;
			}
		}
		return null;
	}
	
	// Metod för att hämta alla transaktioner för ett specifikt account
	public List<String> getTransactions(String personalNumber, int accountId) {
		Customer customer = findCustomer(personalNumber);
		if (customer != null) {
			Account account = customer.getAccount(accountId);
			if (account != null) {
				List<String> transactionList = new ArrayList<>();
				for (Transaction transaction : account.getTransactions()) {
					transactionList.add(transaction.toString());
				}
				return transactionList;
			}
		}
		return null;
	}
	
	// Metod för att avsluta ett konto
	public String closeAccount(String personalNumber, int accountId) {
        Customer customer = findCustomer(personalNumber); // Hitta kund med detta personnummer
        // Om kunden finns så stängs kontot som hör till kunden med hjälp av accountId
        if (customer != null) {
            Account account = customer.closeAccount(accountId);
            if (account != null) {
            	return account.closeAccount();
            }
        }
        return null;
	}
	
	// Metod för att radera en kund
	public List<String> deleteCustomer(String personalNumber) {
        Customer customer = findCustomer(personalNumber); // Hitta kund med detta personnummer
        // Om kunden finns så läggs den in i en ny array
        if (customer != null) {
            List<String> removed = new ArrayList<>();
            removed.add(customer.toString());
            // För varje konto kunden har så stängs denna
            for (Account account : customer.getAccounts()) {
            	removed.add((account).closeAccount());
            }
            // Ta bort kunden och returnera removed
            customers.remove(customer);
            return removed;
        }
        return null;
	}
}

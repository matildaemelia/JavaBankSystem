package matzet4;
/**
 * @author Matilda Zettergren, matzet-4
 */
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankLogic {
	
	// Lista med alla kunder
	private static List<Customer> customers;
	
	private static int accountNumberCounter = 1001;
		
	private static Scanner scanner = new Scanner(System.in);
	
	// Konstruktor
	public BankLogic() {
		customers = new ArrayList<>();
	}
	
	public void addCustomer(String personalNumber, String name) {
		customers.add(new Customer(personalNumber, name, name));
	}
	
	// Spara till Fil
	public void saveToFile(String filePath) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
			oos.writeObject(customers);
			oos.writeInt(accountNumberCounter);
		}
	}
	
	// Hämta från Fil
	public void loadFromFile(String filePath) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
			List<Customer> loadedCustomers = (List<Customer>) ois.readObject();
			customers = loadedCustomers;
			
			int highestAccountNumber = 1000;
			for (Customer customer : customers) {
				for (Account account : customer.getAccounts()) {
					highestAccountNumber = Math.max(highestAccountNumber,  account.getAccountNumber());
				}
			}
			Account.setNextAccountNumber(highestAccountNumber + 1);
		}
	}
	
	// Exportera kontoutdrag
	public void exportStatement(String filePath, Customer customer) throws IOException {
		try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
			writer.println("Kontoutdrag för: " + customer.getFullName());
			writer.println("Personnummer: " + customer.getPersonalNumber());
			writer.println("Datum: " + java.time.LocalDate.now());
			for (Account account : customer.getAccounts()) {
				writer.println("Kontonummer: " + account.getAccountNumber());
				writer.println("Saldo: " + account.getBalance());
				writer.println("Transaktioner:");
				for (Transaction transaction : account.getTransactions()) {
					writer.println(transaction);
				}
			}
		}
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
	
	public static void createAccount() {
		int choice = scanner.nextInt();
		scanner.nextLine();
		
		String personalNumber = scanner.nextLine();
		
		Customer customer = findCustomer(personalNumber);
		
		if(customer != null) {
			Account newAccount;
			if (choice == 1) {
				newAccount = customer.createCreditAccount();
			} else if (choice == 2) {
				newAccount = customer.createSavingsAccount();
			} else {
				return;
			}
		} else {
			System.out.println("Kund med detta personnummer finns inte.");
		}
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
	public boolean deposit(String personalNumber, int accountId, double amount) {
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
	public boolean withdraw(String personalNumber, int accountId, double amount) {
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
	
	public static void chooseDepositOrWithdrawal() {
		String personalNumber = scanner.nextLine();
		Customer customer = findCustomer(personalNumber);
		if (customer != null) {
			int accountNumber = scanner.nextInt();
			scanner.nextLine();
			
			Account account = customer.getAccount(accountNumber);
			if (account != null) {
				int choice = scanner.nextInt();
				scanner.nextLine();
				
				if (choice == 1) {
					int amount = scanner.nextInt();
					if (account instanceof CreditAccount) {
						((CreditAccount) account).deposit(amount);
					} else if (account instanceof SavingsAccount) {
						((SavingsAccount) account).deposit(amount);
					}
					System.out.println("Insättning genomförd.");
				} else if (choice == 2) {
					int amount = scanner.nextInt();
					boolean success = account.withdraw(amount);
					if (success) {
					} else {
						System.out.println("Fel: Uttaget gick inte igenom.");
					}
				} else {
					System.out.println("Felaktigt val.");
				}
			} else {
				System.out.println("Konto inte hittat.");
			}
		} else {
			System.out.println("Kund med detta personnummer finns inte.");
		}
	}
	
	// Metod för att hitts en kund med personnummer
	static Customer findCustomer(String personalNumber) {
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
	
	// Visa transaktioner för specifikt konto
	public static void showTransactions() {
		System.out.print("Ange personnummer för kund: ");
		String personalNumber = scanner.nextLine();
		Customer customer = findCustomer(personalNumber);
		if (customer != null) {
			System.out.print("Ange kontonummer: ");
			int accountNumber = scanner.nextInt();
			scanner.nextLine();
			
			Account account = customer.getAccount(accountNumber);
			if (account != null) {
				System.out.println("trasaktioner för konto: " + accountNumber + ":");
				for (Transaction transaction : account.getTransactions()) {
					System.out.println(transaction.toString());
				}
			} else {
				System.out.println("Konto inte hittat");
			}
		} else {
			System.out.println("Kund med detta personnummer finns inte.");
		}
	}
	
	// Ta bprt konto
	public static void removeAccount() {
		String personalNumber = scanner.nextLine();
		Customer customer = findCustomer(personalNumber);
		if (customer != null) {
			int accountNumber = scanner.nextInt();
			scanner.nextLine();
			
			Account account = customer.getAccount(accountNumber);
			if (account != null) {
				String confirm = scanner.nextLine();
				if (confirm.equalsIgnoreCase("ja")) {
					customer.closeAccount(accountNumber);
					System.out.println("Konto " + accountNumber + " har tagits bort.");
				} else {
					System.out.println("Åtgärden avbröts.");
				}
			} else {
				System.out.println("Konto inte hittat.");
			}
		} else {
			System.out.println("Kund med detta personnummer finns inte.");
		}
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

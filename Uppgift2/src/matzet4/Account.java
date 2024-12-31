package matzet4;
/**
 * @author Matilda Zettergren, matzet-4
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

abstract class Account implements Serializable {
	private static final long serialVersionUID = 4466180876220015448L;
	protected static int nextAccountNumber = 1001;
	protected int accountNumber;
	protected BigDecimal balance;
	// Ändring från uppgift 1: ränta flyttad till kontotypens klass (Savings eller Credit)
	// accountType borttagen
	protected List<Transaction> transactions; // Ny lista för transaktioner
    protected static final DecimalFormat currencyFormat = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(new Locale("sv", "SE")));
    protected static final DecimalFormat percentageFormat = new DecimalFormat("0.0", new DecimalFormatSymbols(new Locale("sv", "SE")));
	
    public static void setNextAccountNumber(int nextNumber) {
    	nextAccountNumber = nextNumber;
    }
    
    public static int getNextAccountNumber() {
    	return nextAccountNumber;
    }
    
	public Account() {
		this.accountNumber = nextAccountNumber++;
		this.balance = BigDecimal.ZERO;
		this.transactions = new ArrayList<>();
	}
	
	public int getAccountNumber() { // Returnerar acccountNumber
        return accountNumber;
    }
	
    public BigDecimal getBalance() { // Returnerar balance
        return balance;
    }
    
    public void setBalance(BigDecimal balance) { // Setter för balance
        this.balance = balance;
    }
    
    public List<Transaction> getTransactions() { // Returnerar en kopia av lista med transaktioner
    	return List.copyOf(transactions);
    }
    
    public abstract String closeAccount();
    
    protected void addTransaction(BigDecimal amount) { // Lägger till ny transaktion med detaljer
    	String type = amount.compareTo(BigDecimal.ZERO) > 0 ? "deposit" : "withdraw";
    	this.balance = this.balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        transactions.add(new Transaction(formattedDate, amount.setScale(2, RoundingMode.HALF_UP), balance));
    }
    
    public abstract BigDecimal calcInterest();
    
    public abstract boolean withdraw(double amount);
    
    public abstract void deposit(double amount);
    
    public String toString() { // Returnerar en String med accountNumber och balance
    	return accountNumber + " " + currencyFormat.format(balance) + " kr";
    }
}

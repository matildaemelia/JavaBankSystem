package matzet4;
import java.io.Serializable;
/**
 * @author Matilda Zettergren, matzet-4
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Transaction implements Serializable {
	private BigDecimal amount;
	private BigDecimal balanceExTransaction;
	private String type;
	private String dateTime;
	private static final DecimalFormat decimalFormat;

	// Initialisera decimalFormat med svenska inställningar
	static {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("sv-SE"));
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		decimalFormat = new DecimalFormat("#,##0.00", symbols);
	}
	
	// Konstruktor
	public Transaction(String type, BigDecimal amount, BigDecimal balanceExTransaction) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		this.dateTime = now.format(formatter);
		this.amount = amount.setScale(2, RoundingMode.HALF_UP);
		this.balanceExTransaction = balanceExTransaction.setScale(2, RoundingMode.HALF_UP);
		this.type = type;
	}
	
	// Getter för transaktionstyp
	public String getTransactionType() {
		return type;
	}

	// Getter för amount
	public BigDecimal getAmount() {
		return amount;
	}

	// Getter för ny balans
	public BigDecimal getBalanceAfterTransaction() {
		return balanceExTransaction;
	}

	// Returnerar transaktionsdetaljer
	public String toString() {
		// Formatera belopp och saldo
		String formattedAmount = decimalFormat.format(amount);
		String formattedBalance = decimalFormat.format(balanceExTransaction);
		return String.format("%s %s kr Saldo: %s kr", dateTime, formattedAmount, formattedBalance);
	}
}

package matzet4;
/**
 * @author Matilda Zettergren, matzet-4
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class SavingsAccount extends Account {
	private boolean firstWithdrawal = true;
	private static final BigDecimal withdrawalFeeRate = new BigDecimal("0.02");
	private static final BigDecimal interestRate = new BigDecimal("2.4");
	private DecimalFormat decimalFormat;
	
	// Konstruktor
	public SavingsAccount() {
		super();
		decimalFormat = new DecimalFormat("#,##0.00");
	}
	
	// Om beloppet är större än 0 så läggs det till som en transaktion till totalbeloppet
	public void deposit(int amount) {
		if (amount > 0) {
			addTransaction(BigDecimal.valueOf(amount));
		}
	}
	
	// Metod för att ta ut pengar
	@Override
	public boolean withdraw(int amount) {
		if (amount > 0) {
			BigDecimal totalAmount = BigDecimal.valueOf(amount);
			if (!firstWithdrawal) { // Lägg till avgift om det inte är första uttaget
				totalAmount = totalAmount.add(totalAmount.multiply(withdrawalFeeRate)).setScale(2, RoundingMode.HALF_UP);
			}
			if (balance.compareTo(totalAmount) >= 0) {
				addTransaction(totalAmount.negate());
				if (firstWithdrawal) {
					firstWithdrawal = false;
				}
				return true;
			}
		}
		return false;
	}
	
	// Stänger kongog och ränkar ut ränta samt returnerar en String med kontodetaljer
	@Override
	public String closeAccount() {
		BigDecimal interest = calcInterest();
		String result = String.format("%d %s kr Sparkonto %s kr", accountNumber, decimalFormat.format(getBalance()), decimalFormat.format(interest));
		return result;
	}
	
	// Beräknar räntan
	@Override
	public BigDecimal calcInterest() {
		return balance.multiply(new BigDecimal("0.024")).setScale(2, RoundingMode.HALF_UP);
	}
	
	// Returnerar kontodetaljer
	public String toString() {
		return String.format("%d %s kr Sparkonto %.1f %%", accountNumber, decimalFormat.format(balance), interestRate);
	}

}

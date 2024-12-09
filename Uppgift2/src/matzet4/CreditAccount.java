package matzet4;
/**
 * @author Matilda Zettergren, matzet-4
 */
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CreditAccount extends Account {
	private static final BigDecimal creditLimit = new BigDecimal("-5000");
	private static final BigDecimal interestRate = new BigDecimal("1.1");
	private static final BigDecimal deptInterestRate = new BigDecimal("5.0");
    private DecimalFormat decimalFormat;
    private DecimalFormat interestFormat;
	
    // Konstruktor
	public CreditAccount() {
		super();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("sv-SE"));
        symbols.setDecimalSeparator(',');
		decimalFormat = new DecimalFormat("#,##0.00", symbols);
        interestFormat = new DecimalFormat("0.0", symbols);
	}
	
	// Om beloppet är större än 0 så läggs det till som en transaktion till totalbeloppet
	public void deposit(int amount) {
		if (amount > 0) {
			addTransaction(BigDecimal.valueOf(amount));
		}
	}
	
	// Ta ut belopp om det är positivt och inte överskrider creditLimit
	@Override
	public boolean withdraw(int amount) {
		if(amount > 0) {
			BigDecimal newBalance = balance.subtract(BigDecimal.valueOf(amount));
			if (newBalance.compareTo(creditLimit) >= 0) {
				addTransaction(BigDecimal.valueOf(-amount));
				return true;
			}
		}
		return false;
	}
	
	// Räknar ut ränta beroende på balance
	@Override
	public BigDecimal calcInterest() {
		if (balance.compareTo(BigDecimal.ZERO) < 0) {
			return balance.multiply(deptInterestRate);
		} else {
			return balance.multiply(interestRate);
		}
	}
	
	// Stänger ett account och returnerar en String med kontonummer, balans och ränta
	@Override
	public String closeAccount() {
		BigDecimal interest;
		if (getBalance().compareTo(BigDecimal.ZERO) >= 0) {
			interest = getBalance().multiply(new BigDecimal("0.011"));
			return String.format("%d %s kr Kreditkonto %s kr", accountNumber, decimalFormat.format(getBalance().setScale(2, RoundingMode.HALF_UP)), decimalFormat.format(interest));
		} else {
			interest = getBalance().multiply(new BigDecimal("0.05"));
			return String.format("%d %s kr Kreditkonto %s kr", accountNumber, decimalFormat.format(getBalance().setScale(2, RoundingMode.HALF_UP)), decimalFormat.format(interest));
		}
	}
	
	// Returnerar kontodetaljer
	public String toString() {
        BigDecimal returnedInterestRate = (balance.compareTo(BigDecimal.ZERO) < 0) ? deptInterestRate : interestRate;
        String formattedInterestRate; 
        if (returnedInterestRate.stripTrailingZeros().scale() <= 0) {
        	formattedInterestRate = returnedInterestRate.setScale(0, RoundingMode.HALF_UP).toString(); // Hade problem med att den returnerade 5,0 & istället för 5 %. Använder toString() så löste sig problemet.
        } else {
        	formattedInterestRate = interestFormat.format(returnedInterestRate.setScale(1, RoundingMode.HALF_UP));
        }
	    return String.format("%d %s kr Kreditkonto %s %%", accountNumber, decimalFormat.format(balance), formattedInterestRate);
	}
}

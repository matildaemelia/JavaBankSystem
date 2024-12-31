package matzet4;
/**
 * @author Matilda Zettergren, matzet-4
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class BankGui extends JFrame {
	
	private BankLogic bankLogic;
	private JTextArea displayArea;
	private JTextArea infoArea;
	private JTextField inputField;
	private JButton createCustomerButton, createAccountButton, deleteCustomerButton, deleteAccountButton, listCustomersButton, depositButton, withdrawButton, showTransactionsButton;
	
	public BankGui() {
		
		bankLogic = new BankLogic();
		
		setSize(800, 600);
		setLayout(new BorderLayout());
		
		// Display
		displayArea = new JTextArea();
		displayArea.setEditable(false); // Gör så att man inte kan ändra.
		JScrollPane scrollPane = new JScrollPane(displayArea); // Gör att mitten går att skrolla
		add(scrollPane, BorderLayout.CENTER);
		displayArea.setBackground(Color.LIGHT_GRAY);
		
		// Input
		inputField = new JTextField();
		inputField.setToolTipText("Ange indata här: ");
		add(inputField, BorderLayout.NORTH);
		
		// Instruktioner
		infoArea = new JTextArea();
		infoArea.setEditable(false);
		infoArea.setBackground(Color.WHITE);
		infoArea.setText("Välkommen! Välj en funktion nedan för att se instruktioner.");
		add(infoArea, BorderLayout.CENTER);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(infoArea), new JScrollPane(displayArea));
		splitPane.setDividerLocation(50);
		add(splitPane, BorderLayout.CENTER);
		
		
		// Meny
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Arkiv");
		
		// Buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 4));
		
		createCustomerButton = new JButton("Skapa Kund");
        createCustomerButton.addActionListener(new CreateCustomerAction());
		
		createAccountButton = new JButton("Skapa Konto");
		createAccountButton.addActionListener(new CreateAccountAction());
		
		deleteCustomerButton = new JButton("Ta Bort Kund");
		deleteCustomerButton.addActionListener(new DeleteCustomerAction());
		
		deleteAccountButton = new JButton("Ta Bort Konto");
		deleteAccountButton.addActionListener(new DeleteAccountAction());
		
		listCustomersButton = new JButton("Lista Kunder");
		listCustomersButton.addActionListener(e -> listCustomers());
		
		depositButton = new JButton("Insättning");
		depositButton.addActionListener(new DepositAction());
		
		withdrawButton = new JButton("Uttag");
		withdrawButton.addActionListener(new WithdrawAction());
		
		showTransactionsButton = new JButton("Visa Transaktioner");
		showTransactionsButton.addActionListener(new ShowTransactionsAction());
		
		buttonPanel.add(createCustomerButton);
		buttonPanel.add(createAccountButton);
		buttonPanel.add(deleteCustomerButton);
		buttonPanel.add(deleteAccountButton);
		buttonPanel.add(listCustomersButton);
		buttonPanel.add(depositButton);
		buttonPanel.add(withdrawButton);
		buttonPanel.add(showTransactionsButton);
		
		// Uppdaterad hovrar-funktion för instruktioner
		createCustomerButton.addMouseListener(new ButtonHoverListener("Ange: förnamn, efternamn och personnummer separerat med melllanslag"));
		createAccountButton.addMouseListener(new ButtonHoverListener("Ange: personnummer och kontotyp separerat med mellanslag"));
		deleteCustomerButton.addMouseListener(new ButtonHoverListener("Ange: personnummer utan mellanslag"));
		deleteAccountButton.addMouseListener(new ButtonHoverListener("Ange: personnummer och kontonummer separerat med mellanslag"));
		depositButton.addMouseListener(new ButtonHoverListener("Ange: personnummer, kontonummer och belopp separerat med mellanslag"));
		withdrawButton.addMouseListener(new ButtonHoverListener("Ange: personnummer, kontonummer och belopp separerat med mellanslag"));
		showTransactionsButton.addMouseListener(new ButtonHoverListener("Ange: personnummer och kontonummer separerat med mellanslag"));
		
		// Spar-knapp
		JButton saveButton = new JButton("Spara till Fil");
		saveButton.addActionListener(e -> {
			try {
				bankLogic.saveToFile("matzet4_files/bank.dat");
				displayArea.append("Sparade kunder. \n");
			} catch (IOException ex) {
				displayArea.append("Error: " + ex.getMessage() + "\n");
			}
		});
		
		// Hämt-knapp
		JButton loadButton = new JButton("Hämta från Fil");
		loadButton.addActionListener(e -> {
			try {
				bankLogic.loadFromFile("matzet4_files/bank.dat");
				displayArea.append("Hämtade Kunder. \n");
			} catch (IOException | ClassNotFoundException ex) {
				displayArea.append("Error: " + ex.getMessage() + "\n");
			}
		});
		
		// Export-knapp
		JButton exportButton = new JButton("Exportera kontoutdrag");
		exportButton.addActionListener(e -> {
			String personalNumber = JOptionPane.showInputDialog(this, "Ange Personnummer:");
			Customer customer = bankLogic.findCustomer(personalNumber);
			if (customer != null) {
				try {
					bankLogic.exportStatement("matzet4_files/" + personalNumber + "_statement.txt", customer);
					displayArea.append("Exporterade kontoutdrag för " + personalNumber + ".\n");
				} catch (IOException ex) {
					displayArea.append("Error: " + ex.getMessage() + "\n");
				}
			} else {
				displayArea.append("Kund hittades ej.\n");
			}
		});
		
		buttonPanel.add(saveButton);
		buttonPanel.add(loadButton);
		buttonPanel.add(exportButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	// Instruktioner vid hovring
	private class ButtonHoverListener extends java.awt.event.MouseAdapter {
		private final String instruction;
		
		public ButtonHoverListener(String instruction) {
			this.instruction = instruction;
		}
		
		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {
			infoArea.setText(instruction);
		}
		
		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {
			infoArea.setText("Välj en funktion för att se instruktioner.");
		}
	}
	
	// Skapa kund
	private class CreateCustomerAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String[] input = inputField.getText().split(" ");
            if (input.length < 3) {
                displayArea.append("Fel: Ange förnamn, efternamn och personnummer.\n");
                return;
            }
            String name = input[0];
            String surname = input[1];
            String personalNumber = input[2];
       
            boolean success = false;
			try {
				success = BankLogic.createCustomer(name, surname, personalNumber);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            if (success) {
                displayArea.append("Kund skapad: " + name + " " + surname + " (" + personalNumber + ")\n");
            } else {
                displayArea.append("Fel: Kunde inte skapa kund.\n");
            }
        }
    }
	
	// Skapa konto
	private class CreateAccountAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String personalNumber = inputField.getText();
			String accountType = JOptionPane.showInputDialog(this, "Välj kontotyp (1. Sparkonto, 2. Kreditkonto): ");
			int accountNumber = -1;
			
			if("1".equals(accountType)) {
				accountNumber = bankLogic.createSavingsAccount(personalNumber);
			} else if ("2".equals(accountType)) {
				accountNumber = bankLogic.createSavingsAccount(personalNumber);
			}
			if (accountNumber >= 0) {
				displayArea.append("Konto skapat: Kontonummer " + accountNumber + "\n");
			} else {
				displayArea.append("Fel: Kunde inte skapa konto. \n");
			}
		}
	}
	
	// Insättning
	private class DepositAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String input = inputField.getText();
			String[] inputs = input.split(" ");
			
			if (inputs.length != 3) {
				displayArea.append("Fel: Ange personnummer, kontonummer och belopp.\n");
				return;
			}
			
			String personalNumber = inputs[0];
			int accountNumber = Integer.parseInt(inputs[1]);
			double amount = Double.parseDouble(inputs[2]);
			
			boolean success = bankLogic.deposit(personalNumber, accountNumber, amount);
			if (success) {
				displayArea.append("Insättning på " + amount + " kr genomförd för: " + personalNumber + "\n");
			} else {
				displayArea.append("Fel: Kunde inte göra insättning.\n");
			}
		}
	}
	
	// Uttag
	private class WithdrawAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String input = inputField.getText();
			String[] inputs = input.split(" ");
			
			if (inputs.length != 3) {
				displayArea.append("Fel: Ange personnummer, kontonummer och belopp.\n");
				return;
			}
			
			String personalNumber = inputs[0];
			int accountNumber = Integer.parseInt(inputs[1]);
			double amount = Double.parseDouble(inputs[2]);
			
			boolean success = bankLogic.withdraw(personalNumber, accountNumber, amount);
			if (success) {
				displayArea.append("Uttag på " + amount + " kr genomförd för: " + personalNumber + "\n");
			} else {
				displayArea.append("Fel: Kunde inte göra uttag.\n");
			}
		}
	}
	
	// Visa transaktioner
	private class ShowTransactionsAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String input = inputField.getText();
			String[] inputs = input.split(" ");
			
			if (inputs.length != 2) {
				displayArea.append("Fel: Ange personnummer och kontonummer.\n");
				return;
			}
			
			String personalNumber = inputs[0];
			int accountNumber = Integer.parseInt(inputs[1]);
			
			List<String> transactions = bankLogic.getTransactions(personalNumber, accountNumber);
			
			if (transactions != null && !transactions.isEmpty()) {
				displayArea.append("Transaktioner för konto " + accountNumber + ":\n");
				for (String transaction : transactions) {
					displayArea.append(transaction.toString() + "\n");
				}
			} else {
				displayArea.append("Inga transaktioner för " + accountNumber + " hittades.\n");
			}
		}
	}

	// Ta bort kund
	private class DeleteCustomerAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String personalNumber = inputField.getText().trim(); // Tar bort mellanslag
			
			if (personalNumber.isEmpty()) {
				displayArea.append("Fel: Ange ett personnummer.\n");
				return;
			}
			
			// Be om bekräftelse för borttagning
			int confirm = JOptionPane.showConfirmDialog(
					BankGui.this, 
					"Är du säker på att du vill ta bort kundenmed personnummer: " + personalNumber + "?",
					"Bekräfta borttagning",
					JOptionPane.YES_NO_OPTION);
			
			if (confirm == JOptionPane.YES_OPTION) {
				List<String> deletedCustomerInfo = bankLogic.deleteCustomer(personalNumber);
				if (deletedCustomerInfo != null) {
					displayArea.append("Kund borttagen: " + deletedCustomerInfo + "\n");
				} else {
					displayArea.append("Fel: Kunde inte ta bort kund.\n");
				}
			} else {
				displayArea.append("Borttagning av kund avbruten.\n");
			}
		}
	}
	
	// Ta bort konto
	private class DeleteAccountAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String input = inputField.getText();
			String[] inputs = input.split(" ");
			
			if (inputs.length != 2) {
				displayArea.append("Fel: Ange personnummer och kontonummer.\n");
				return;
			}

			String personalNumber = inputs[0].trim();
			int accountNumber;
			
			try {
				accountNumber = Integer.parseInt(inputs[1]);
			} catch (NumberFormatException ex) {
				displayArea.append("Fel: Kontonummer måste vara ett heltal.\n");
				return;
			}
			
			// Be om bekräftelse för borttagning
			int confirm = JOptionPane.showConfirmDialog(
					BankGui.this, 
					"Är du säker på att du vill ta bort konto med kontonummer: " + accountNumber + "?",
					"Bekräfta borttagning",
					JOptionPane.YES_NO_OPTION);
			
			if (confirm == JOptionPane.YES_OPTION) {
				String success = bankLogic.closeAccount(personalNumber, accountNumber);
				if (success != null) {
					displayArea.append("Konto med kontonummer: " + accountNumber + " borttaget\n");
				} else {
					displayArea.append("Fel: Kunde inte ta bort kund. Kontrollera uppgifterna\n");
				}
			} else {
				displayArea.append("Borttagning av kund avbruten.\n");
			}
		}
	}
	
	private void listCustomers() {
		displayArea.setText("");
		for (String customerInfo : bankLogic.getAllCustomers()) {
			displayArea.append(customerInfo + "\n");
		}
	}
		
	public static void main (String[] args) {
		SwingUtilities.invokeLater(BankGui::new);
		JFrame frame = new BankGui();
		frame.setTitle("Mitt Bank GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
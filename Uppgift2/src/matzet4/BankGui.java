package matzet4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BankGui extends JFrame {
	
	private BankLogic bankLogic;
	
	private JTextArea displayArea;
	private JTextField inputField;
	private JButton createCustomerButton, createAccountButton, deleteCustomerButton, listCustomersButton;
	
	public BankGui() {
		
		bankLogic = new BankLogic();
		
		setSize(600, 400);
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
		
		// Meny
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("Arkiv");
		
		// Buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2, 2));
		
		createCustomerButton = new JButton("Skapa Kund");
        createCustomerButton.addActionListener(new CreateCustomerAction());
		
		createAccountButton = new JButton("Skapa Konto");
		createAccountButton.addActionListener(new CreateAccountAction());
		
		deleteCustomerButton = new JButton("Ta Bort Kund");
		deleteCustomerButton.addActionListener(new DeleteCustomerAction());
		
		listCustomersButton = new JButton("Lista Kunder");
		listCustomersButton.addActionListener(e -> listCustomers());
		
		buttonPanel.add(createCustomerButton);
		buttonPanel.add(createAccountButton);
		buttonPanel.add(deleteCustomerButton);
		buttonPanel.add(listCustomersButton);
		
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	// Skapa kund
	private class CreateCustomerAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String[] input = inputField.getText().split(" ");
            if (input.length < 3) {
                displayArea.append("Fel: Ange namn, efternamn och personnummer.\n");
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
			int accountNumber = bankLogic.createSavingsAccount(personalNumber);
			if (accountNumber >= 0) {
				displayArea.append("Konto skapat: Kontonummer " + accountNumber + "\n");
			} else {
				displayArea.append("Fel: Kunde inte skapa konto. \n");
			}
		}
	}
	
	private class DeleteCustomerAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String personalNumber = inputField.getText();
			List<String> deletedCustomerInfo = bankLogic.deleteCustomer(personalNumber);
			if (deletedCustomerInfo != null) {
				displayArea.append("Kund borttagen: " + deletedCustomerInfo + "\n");
			} else {
				displayArea.append("Fel: Kunde inte ta bort kund.\n");
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
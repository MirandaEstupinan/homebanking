package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static com.mindhub.homebanking.models.AccountType.CURRENTACCOUNT;
import static com.mindhub.homebanking.models.AccountType.SAVINGBANK;
import static com.mindhub.homebanking.models.CardColor.*;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;



@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientService clientService, AccountService accountService, TransactionService transactionService,
									  LoanService loanService, ClientLoanService clientLoanService, CardService cardService){
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEnconder.encode("melba123"));
			Client client2 = new Client("Carlos", "Gomez", "carlos@hotmail.com",passwordEnconder.encode("carlos123"));
			Client client3 = new Client("admin", "admin","admin@admin",passwordEnconder.encode("admin"));
			Account account1 = new Account(5000.0, "VIN001", LocalDateTime.now(), client1, true, SAVINGBANK);
			Account account2 = new Account(7500.0, "VIN002", LocalDateTime.now().plusDays(1),client1, true, CURRENTACCOUNT);
			Account account3 = new Account(1500.0, "VIN003", LocalDateTime.now(), client2, true, CURRENTACCOUNT);
			Transaction transaction1 = new Transaction(DEBIT, -1000.0, "Pago", LocalDateTime.now().plusDays(-10), account1, account1.getBalance(), true);
			Transaction transaction5 = new Transaction(DEBIT, 1000.0, "pago", LocalDateTime.now().plusMonths(-1), account1, account1.getBalance(), true);
			Transaction transaction6 = new Transaction(DEBIT, 1000.0, "debito", LocalDateTime.now().plusDays(-10), account1, account1.getBalance(), true);
			Transaction transaction2 = new Transaction(CREDIT, 3000.0, "pago", LocalDateTime.now(), account3,account3.getBalance(), true);
			Transaction transaction3 = new Transaction(CREDIT, 2500.0, "pago", LocalDateTime.now(), account2, account2.getBalance(),true);
			Transaction transaction4 = new Transaction(DEBIT, -1000, "pago", LocalDateTime.now(), account2, account2.getBalance(), true);
			Loan hipotecario = new Loan("mortgage", 500000.00, List.of(12, 24, 36, 48, 60), 20.00);
			Loan personal = new Loan("personal", 100000.00, List.of(6, 12, 24), 15.00);
			Loan automotriz = new Loan("car", 300000.00, List.of(6,12,24,36), 17.00);
			ClientLoan clientLoan1 = new ClientLoan(hipotecario, 400000.00, 60, client1);
			ClientLoan clientLoan2 = new ClientLoan(personal, 50000.00, 12, client1);
			ClientLoan clientLoan3 = new ClientLoan(personal, 100000.00, 24, client2);
			ClientLoan clientLoan4 = new ClientLoan(automotriz, 200000.00, 36, client2);
			Card card1 = new Card(client1.getFirstName() + " " + client1.getLastName(), "3568-8521-9658-3266", 341, LocalDate.now().plusYears(-1),
					LocalDate.now(),GOLD, CardType.DEBIT, client1, true, account1);
			Card card2 = new Card(client1.getFirstName() + " " + client1.getLastName(), "1234-5678-9101-1121", 852, LocalDate.now().plusYears(5),
				LocalDate.now(),TITANIUM, CardType.CREDIT, client1, true, account1);
			Card card3 = new Card(client2.getFirstName() + " " + client2.getLastName(), "3141-5161-7181-9202", 852, LocalDate.now().plusYears(5),
					LocalDate.now(),SILVER, CardType.CREDIT, client2, true, account3);

			client1.addAccount(account2);

			clientService.saveClient(client1);
			clientService.saveClient(client2);
			clientService.saveClient(client3);
			accountService.saveAccount(account1);
			accountService.saveAccount(account2);
			accountService.saveAccount(account3);
			transactionService.saveTransaction(transaction1);
			transactionService.saveTransaction(transaction2);
			transactionService.saveTransaction(transaction3);
			transactionService.saveTransaction(transaction4);
			transactionService.saveTransaction(transaction5);
			transactionService.saveTransaction(transaction6);
			loanService.saveLoan(hipotecario);
			loanService.saveLoan(personal);
			loanService.saveLoan(automotriz);
			clientLoanService.saveClientLoan(clientLoan1);
			clientLoanService.saveClientLoan(clientLoan2);
			clientLoanService.saveClientLoan(clientLoan3);
			clientLoanService.saveClientLoan(clientLoan4);
			cardService.saveCard(card1);
			cardService.saveCard(card2);
			cardService.saveCard(card3);
	};
	}
	@Autowired
	private PasswordEncoder passwordEnconder;

	public PasswordEncoder getPasswordEnconder() {
		return passwordEnconder;
	}

	public void setPasswordEnconder(PasswordEncoder passwordEnconder) {
		this.passwordEnconder = passwordEnconder;
	}
}


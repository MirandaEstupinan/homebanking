package com.mindhub.homebanking;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static com.mindhub.homebanking.models.CardColor.GOLD;
import static com.mindhub.homebanking.models.CardColor.SILVER;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {


    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    @Test
    public void existClients(){
        List<Client> client = clientRepository.findAll();
        assertThat(client,is(not(empty())));
    }

    @Test
    public void existClientMelba(){
        List<Client> client = clientRepository.findAll();
        assertThat(client, hasItem(hasProperty("firstName", is("Melba"))));
    }

    @Test
    public void accountNumber(){
        List<Account> account = accountRepository.findAll();
        assertThat(account, hasItem(hasProperty("number", startsWith("VIN"))));
    }

    @Test
    public void existAccount(){
        List<Account> account = accountRepository.findAll();
        assertThat(account,is(not(empty())));
    }

    @Test
    public void typeCardGold(){
        List <Card> card = cardRepository.findAll();
        assertThat(card, hasItem(hasProperty("cardColor", is(GOLD))));
    }

    @Test
    public void typeCardSilver(){
        List <Card> card = cardRepository.findAll();
        assertThat(card, hasItem(hasProperty("cardColor", is(SILVER))));
    }

    @Test
    public void typeTransactionCredit(){
        List <Transaction> transaction = transactionRepository.findAll();
        assertThat(transaction, hasItem(hasProperty("type", is(CREDIT))));
    }

    @Test
    public void typeTransactionDebit(){
        List <Transaction> transaction = transactionRepository.findAll();
        assertThat(transaction, hasItem(hasProperty("type", is(DEBIT))));
    }
    }


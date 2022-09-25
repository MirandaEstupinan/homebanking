package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.configurations.WebAuthentication;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {
@Autowired
private ClientRepository clientRepository;

@Autowired
private AccountRepository accountRepository;

@Autowired
private ClientService clientService;

@Autowired
private AccountService accountService;

@GetMapping("/clients")
public List<ClientDTO> getClients(){
    return clientService.getAllClients().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
}

@GetMapping("/clients/{id}")
public ClientDTO getClient(@PathVariable Long id){
    return new ClientDTO(clientService.getClientById(id));
}


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebAuthentication webAuthentication;



    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {


        if (firstName.isEmpty() || lastName.isEmpty() ||  email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientService.getClientByEmail(email) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }


        String accountNumber = "VIN-" + getRandomNumber(1, 99999999);

        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);
        accountService.saveAccount(new Account(0,  accountNumber, LocalDateTime.now(), newClient, true, AccountType.SAVINGBANK));
        return new ResponseEntity<>(HttpStatus.CREATED);


    }


    @GetMapping("/clients/current")

    public ClientDTO getAll(Authentication authentication) {
        return new ClientDTO(clientService.getClientByEmail(authentication.getName()));
    }

}

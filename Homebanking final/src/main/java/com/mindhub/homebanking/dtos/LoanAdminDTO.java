package com.mindhub.homebanking.dtos;

public class LoanAdminDTO {

    private long id;
    private String name;
    private Double amount;
    private int payments;

    public LoanAdminDTO(){};

    public LoanAdminDTO(long id, String name, Double amount, int payments){}
}

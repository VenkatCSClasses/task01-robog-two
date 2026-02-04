package edu.ithaca.dturnbull.bank;

import java.io.Serial;

public class InsufficientFundsException extends Exception{
    @Serial
    private static final long serialVersionUID = 1L;

    public InsufficientFundsException(String s) {
        super(s);
    }

}
package edu.ithaca.dturnbull.bank;

import java.util.HashSet;
import java.util.Set;

public class BankAccount {

    private final String email;
    private double balance;

    /**
     * @throws IllegalArgumentException if email is invalid
     */
    public BankAccount(String email, double startingBalance) {
        if (isEmailValid(email)) {
            this.email = email;
            this.balance = startingBalance;
        } else {
            throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");
        }
    }

    /**
     * @return The user's current bank account balance
     */
    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Withdraw some money from an account, usually for a purchase.
     * @post reduces the balance by amount if amount is non-negative and smaller than balance
     */
    public void withdraw (double amount) throws InsufficientFundsException {
        if (amount < 0) {
            throw new InsufficientFundsException("Cannot withdraw negative amount");
        } else if (amount <= balance) {
            balance -= amount;
        } else {
            throw new InsufficientFundsException("Not enough money");
        }
    }


    // An email is local-part@domain according to the spec.
    // These states describe an FSM that accepts an email that is
    // either a dot-atom or a quoted-string type, then accepts
    // another dot-atom for the domain.
    private enum EmailState {
        START,
        LOCAL_PART_ACCEPT_DOT_ATOM,
        LOCAL_PART_ACCEPT_DOT_ATOM_FOUND_DOT,
        LOCAL_PART_ACCEPT_QUOTED,
        LOCAL_PART_ACCEPT_QUOTED_ESCAPED_CHAR,
        LOCAL_PART_NEEDS_AT_AFTER_QUOTE,
        DOMAIN_START,
        DOMAIN_ACCEPT_DOT_ATOM(true), //This means that we are in the letter part of a domain
        DOMAIN_ACCEPT_DOT_ATOM_FOUND_DOT,
        DIE;

        private final boolean isFinished;
        EmailState(boolean isFinished) { this.isFinished = true; }
        EmailState() { this.isFinished = false; }

        boolean isFinished() { return isFinished; }
    }

    // Valid characters with O(1) lookup
    private static final Set<Character> dotAtomValidChars = new HashSet<>(){{
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&'*+-/=?^_`{|}~"
                // java-fu to add them all to this set just at JVM startup
                .chars().forEach((c) -> this.add((char)c));
    }};

    public static boolean isEmailValid(String email) {
        EmailState state = EmailState.START;
        for (char character : email.toCharArray()) {
            switch (state) {
                case START -> {
                    if (character == '"') {
                        state = EmailState.LOCAL_PART_ACCEPT_QUOTED;
                    } else if (!dotAtomValidChars.contains(character)) {
                        state = EmailState.DIE;
                    } else {
                        state = EmailState.LOCAL_PART_ACCEPT_DOT_ATOM;
                    }
                }
                case LOCAL_PART_ACCEPT_DOT_ATOM -> {
                    if (character == '.') {
                        state = EmailState.LOCAL_PART_ACCEPT_DOT_ATOM_FOUND_DOT;
                    } else if (character == '@') {
                        state = EmailState.DOMAIN_START;
                    } else if (!dotAtomValidChars.contains(character)) {
                        state = EmailState.DIE;
                    }
                    // otherwise, we can stay in this state; there are more characters to go
                }
                case LOCAL_PART_ACCEPT_DOT_ATOM_FOUND_DOT -> {
                    // We already found a dot. The next character must be valid (no double dots!)
                    if (!dotAtomValidChars.contains(character)) {
                        state = EmailState.DIE;
                    } else {
                        state = EmailState.LOCAL_PART_ACCEPT_DOT_ATOM;
                    }
                }
                case LOCAL_PART_ACCEPT_QUOTED -> {
                    if (character == '"') state = EmailState.LOCAL_PART_NEEDS_AT_AFTER_QUOTE;
                    if (character == '\\') state = EmailState.LOCAL_PART_ACCEPT_QUOTED_ESCAPED_CHAR;
                    // otherwise, stay in this state: all characters are valid!
                }
                case LOCAL_PART_ACCEPT_QUOTED_ESCAPED_CHAR -> {
                    if (character == '"' || character == '\\') {
                        state = EmailState.LOCAL_PART_ACCEPT_QUOTED;
                    } else {
                        // Unescaped backslash character
                        state = EmailState.DIE;
                    }
                }
                case LOCAL_PART_NEEDS_AT_AFTER_QUOTE -> {
                    if (character != '@') {
                        state = EmailState.DIE;
                    } else {
                        state = EmailState.DOMAIN_START;
                    }
                }
                case DOMAIN_START -> {
                    if (!dotAtomValidChars.contains(character)) {
                        state = EmailState.DIE;
                    } else {
                        state = EmailState.DOMAIN_ACCEPT_DOT_ATOM;
                    }
                }
                case DOMAIN_ACCEPT_DOT_ATOM -> {
                    if (character == '.') {
                        state = EmailState.DOMAIN_ACCEPT_DOT_ATOM_FOUND_DOT;
                    } else if (!dotAtomValidChars.contains(character)) {
                        state = EmailState.DIE;
                    }
                    // otherwise, we can stay in this state; there are more characters to go
                }
                case DOMAIN_ACCEPT_DOT_ATOM_FOUND_DOT -> {
                    // We already found a dot. The next character must be valid (no double dots!)
                    if (!dotAtomValidChars.contains(character)) {
                        state = EmailState.DIE;
                    } else {
                        state = EmailState.DOMAIN_ACCEPT_DOT_ATOM;
                    }
                }
            }

            // Short circuit early
            if (state == EmailState.DIE) return false;
        }
        return state.isFinished();
    }
}
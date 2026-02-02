package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static edu.ithaca.dturnbull.bank.BankAccount.*;


class BankAccountTest {

    @Test
    void withdrawAndBalanceTest() throws InsufficientFundsException{
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        // EC: Withdrawal succeeds
        assertEquals(200, bankAccount.getBalance(), 0.001);
        bankAccount.withdraw(100);
        assertEquals(100, bankAccount.getBalance(), 0.001);

        // EC: Withdrawal amount is invalid, bordered by too large and too small
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(-300));

        // Boundary case, withdrawal is zero
        bankAccount.withdraw(0);
        // Boundary case, withdrawal is exactly correct
        bankAccount.withdraw(100);

        assertEquals(0, bankAccount.getBalance(), 0.001);
    }

    @Test
    void isEmailValidTest(){
        assertTrue(isEmailValid( "a@b.com"));   // valid email address
        assertFalse(isEmailValid(""));         // empty string

//      ============================
//      "local-part" validation
//      ============================

        // Allowed chars
        assertFalse(isEmailValid("anemail🧊@gmail.com")); //EC: Local part contains invalid characters
        assertTrue(isEmailValid("0123456789qwertyuiopasdfghjklzxcvbnm@gmail.com")); //EC: Local part contains only valid characters

        // Periods as separators (dot-atom specification)
        assertFalse(isEmailValid(".anemail@gmail.com")); //EC: Border, local part starts with a period
        assertFalse(isEmailValid("anemail.@gmail.com")); //EC: Border, local part ends with a period
        assertFalse(isEmailValid("an..email@gmail.com")); //EC: Border, local part contains consecutive periods
        assertTrue(isEmailValid("samuel.elliot.knight@gmail.com")); //EC: Valid, local part contains periods as separators


        // Quoted string type
        assertFalse(isEmailValid("\"Samuel \\Kni\"ght\"@gmail.com")); // EC: Invalid case, string contains an unescaped backslash or quote
        assertTrue(isEmailValid("\"Samuel Knight\"@gmail.com")); // EC: Valid case, no special characters in string
        assertTrue(isEmailValid("\"Samuel \\\"Sam\\\" \\\\Knight\"@gmail.com")); // EC: Border case, string contains escaped quotes/backslashes


//      ============================
//      "domain" validation
//      ============================

        // Number of tlds
        assertTrue(isEmailValid("anemail@mail.google.com")); //EC: Domain contains many parts (border of the valid case)
        assertTrue(isEmailValid("anemail@localhost")); //EC: Domain contains a single (border of the valid case)

        // Periods as separators (dot-atom specification)
        assertFalse(isEmailValid("anemail@.gmail.com")); //EC: Domain starts with a period - border case
        assertFalse(isEmailValid("anemail@gmail.com.")); //EC: Domain ends with a period - border case
        assertFalse(isEmailValid("anemail@gmail..com")); //EC: Domain contains consecutive periods - border case
        assertTrue(isEmailValid("anemail@gmail.com")); //EC: Domain contains periods as separators - valid case

        // Valid chars
        assertFalse(isEmailValid("anemail@gm⚾️ail.com")); //EC: Domain contains invalid characters
        assertTrue(isEmailValid("anemail@12.13.14.net")); //EC: Domain contains no invalid characters

        assertFalse(isEmailValid("anemail@")); // Missing a domain entirely
        assertFalse(isEmailValid("@google.com")); // Only containing a domain
    }

    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);
        //check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("", 100));
    }

}
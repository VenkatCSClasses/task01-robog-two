package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static edu.ithaca.dturnbull.bank.BankAccount.*;


class BankAccountTest {

    @Test
    void getBalanceTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals(200, bankAccount.getBalance(), 0.001);
    }

    @Test
    void withdrawTest() throws InsufficientFundsException{
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.withdraw(100);

        assertEquals(100, bankAccount.getBalance(), 0.001);
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));
    }

    @Test
    void isEmailValidTest(){
        assertTrue(isEmailValid( "a@b.com"));   // valid email address
        assertFalse(isEmailValid(""));         // empty string

//      ============================
//      "local-part" validation
//      ============================

        // Allowed chars
        assertFalse(isEmailValid("anemail🧊@gmail.com"));
        assertTrue(isEmailValid("a0123456789qwertyuiopasdfghjklzxcvbnm@gmail.com"));

        // Periods as separators (dot-atom specification)
        assertFalse(isEmailValid(".anemail.@gmail.com"));
        assertFalse(isEmailValid("an..email@gmail.com"));
        assertTrue(isEmailValid("samuel.elliot.knight@gmail.com"));

/* Really tricky cases - skip for now

       // Quoted string type
        assertFalse(isEmailValid("\"Samuel \\Knight\"@gmail.com"));
        assertFalse(isEmailValid("\"Samuel \"Knight\"@gmail.com"));
        assertTrue(isEmailValid("\"Samuel Knight\"@gmail.com"));
        assertTrue(isEmailValid("\"Samuel \\\"Sam\\\" Knight\"@gmail.com"));
*/

//      ============================
//      "domain" validation
//      ============================

        // Number of tlds
        assertTrue(isEmailValid("anemail@mail.amazon.co.uk"));
        assertTrue(isEmailValid("anemail@mail.google.com"));
        assertTrue(isEmailValid("anemail@gmail.com"));

        // Periods as separators (dot-atom specification)
        assertFalse(isEmailValid("anemail@.gmail.com"));
        assertFalse(isEmailValid("anemail@gmail..com"));
        assertTrue(isEmailValid("anemail@gmail.com"));

        // Valid chars
        assertFalse(isEmailValid("anemail@gm⚾️ail.com"));
        assertTrue(isEmailValid("anemail@12.13.14.net"));
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
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
        assertFalse(isEmailValid("anemail🧊@gmail.com")); //EC: Local part contains invalid characters
        assertTrue(isEmailValid("a0123456789qwertyuiopasdfghjklzxcvbnm@gmail.com")); //EC: Local part contains noinvalid characters

        // Periods as separators (dot-atom specification)
        assertFalse(isEmailValid(".anemail.@gmail.com")); //EC: Local part starts or ends with a period - border case
        assertFalse(isEmailValid("an..email@gmail.com")); //EC: Local part contains consecutive periods - border case
        assertTrue(isEmailValid("samuel.elliot.knight@gmail.com")); //EC: Local part contains periods as separators 

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
        assertTrue(isEmailValid("anemail@mail.amazon.co.uk")); //EC: Domain contains multiple subdomains
        assertTrue(isEmailValid("anemail@mail.google.com")); //EC: Domain contains single subdomain
        assertTrue(isEmailValid("anemail@gmail.com")); //EC: Domain contains no subdomains

        // Periods as separators (dot-atom specification)
        assertFalse(isEmailValid("anemail@.gmail.com")); //EC: Domain starts with a period - border case
        assertFalse(isEmailValid("anemail@gmail..com")); //EC: Domain contains consecutive periods - border case
        assertTrue(isEmailValid("anemail@gmail.com")); //EC: Domain contains periods as separators

        // Valid chars
        assertFalse(isEmailValid("anemail@gm⚾️ail.com")); //EC: Domain contains invalid characters
        assertTrue(isEmailValid("anemail@12.13.14.net")); //EC: Domain contains no invalid characters

        //Could have ECs for whether domains and local parts are missing, and if they are present
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
//package com.example.lantconsulting.entity;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.example.lantconsulting.entity.mocks.AccountMock;
//
//@SpringBootTest
//public class ContractTest {
//
//    @Test
//    public void testContractGettersAndSetters() {
//        Contract contract = new Contract();
//        contract.setId(1L);
//        contract.setDetails("Contract details");
//        contract.setStartDate(LocalDate.of(2023, 1, 1));
//        contract.setEndDate(LocalDate.of(2023, 12, 31));
//        Account user = new AccountMock();
//        contract.setUser(user);
//
//        assertEquals(1L, contract.getId());
//        assertEquals("Contract details", contract.getDetails());
//        assertEquals(LocalDate.of(2023, 1, 1), contract.getStartDate());
//        assertEquals(LocalDate.of(2023, 12, 31), contract.getEndDate());
//        assertEquals(user, contract.getUser());
//    }
//}
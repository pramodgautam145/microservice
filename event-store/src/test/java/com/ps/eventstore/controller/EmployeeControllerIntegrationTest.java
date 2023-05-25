package com.ps.eventstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.eventstore.model.Employee;
import com.ps.eventstore.service.BiometricAttendanceService;
import com.ps.eventstore.service.EmployeeService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService mockEmployeeService;
    @MockBean
    private BiometricAttendanceService mockBiometricAttendanceService;


    @Test
    public void testGetUserById() throws Exception {
        Employee emp = new Employee();
        emp.setId(1L);
        emp.setFirstName("Pramod");
        emp.setLastName("Kumar");
        emp.setGender("Male");
        emp.setMobileNo(123456789L);
        emp.setTitle("developer");
        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(emp);
        Mockito.when(mockEmployeeService.addEmployee(emp)).thenReturn(emp);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/employee/register").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        Assertions.assertEquals(201,mvcResult.getResponse().getStatus());
        Employee responseUser = objectMapper.readValue(userJson, Employee.class);
        Assertions.assertEquals(emp.getFirstName(), responseUser.getFirstName());

    }
}

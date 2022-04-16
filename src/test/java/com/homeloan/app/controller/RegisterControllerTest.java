package com.homeloan.app.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homeloan.app.model.Users;
import com.homeloan.app.repo.UserRepository;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(RegisterControllerTest.class)
public class RegisterControllerTest {
    
	@Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;  
    //ObjectMapper provides functionality for reading and writing JSON,
    // either to and from basic POJOs
    
    @MockBean
    UserRepository userRepo;
    
    Users RECORD_1 = new Users(1, "user1","User" , "One","user1@123", "user1@gmail.com",Long.parseLong("8796545432"));
    Users RECORD_2 = new Users(2, "user2","User" , "Two","user2@123", "user2@gmail.com",Long.parseLong("8796545433"));
    Users RECORD_3 = new Users(3, "user3","User" , "Three","user3@123", "user3@gmail.com",Long.parseLong("8796545434"));
    
    @Test
	void contextLoads() {
	}
    
    List<Users> records = new ArrayList<>();
    
    @AfterEach
    public void insertMock_Records() {
    	records.add(RECORD_1);
    	records.add(RECORD_2);
    	records.add(RECORD_3);
    }
	
    
    @Test
    public void getAllRecords_success() throws Exception {
        
        Mockito.when(userRepo.findAll()).thenReturn(records);
        //When findAll called then ready with records  (No DB calls) 
        mockMvc.perform(MockMvcRequestBuilders
                .get("/getAllUsers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) //200
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].username", is("user3")));
    }
    
//    @Test
//    public void getPatientById_success() throws Exception {
//        Mockito.when(patientRecordRepository.findById(RECORD_1.getPatientId()))
//        				.thenReturn(java.util.Optional.of(RECORD_1));
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .get("/patient/1")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", notNullValue()))
//                .andExpect(jsonPath("$.name", is("Rayven Yor")));
//    }
//    
//    @Test
//    public void createRecord_success() throws Exception {
//        PatientRecord record = PatientRecord.builder()
//                .name("John Doe")
//                .age(47)
//                .address("New York USA")
//                .build();
//
//        Mockito.when(patientRecordRepository.save(record)).thenReturn(record);
//
//        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/patient")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(record));
//
//        mockMvc.perform(mockRequest)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", notNullValue()))
//                .andExpect(jsonPath("$.name", is("John Doe")));
//        }
//    
//    @Test
//    public void updatePatientRecord_success() throws Exception {
//        PatientRecord updatedRecord = PatientRecord.builder()
//                .patientId(1l)
//                .name("Rayven Zambo")
//                .age(23)
//                .address("Cebu Philippines")
//                .build();
//
//        Mockito.when(patientRecordRepository.findById(RECORD_1.getPatientId()))
//        .thenReturn(Optional.of(RECORD_1));
//        
//        Mockito.when(patientRecordRepository.save(updatedRecord)).thenReturn(updatedRecord);
//
//        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/patient")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(updatedRecord));
//
//        mockMvc.perform(mockRequest)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", notNullValue()))
//                .andExpect(jsonPath("$.name", is("Rayven Zambo")));
//    }
//    
//    @Test
//    public void updatePatientRecord_nullId() throws Exception {
//        PatientRecord updatedRecord = PatientRecord.builder()
//                .name("Sherlock Holmes")
//                .age(40)
//                .address("221B Baker Street")
//                .build();
//
//        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/patient")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(updatedRecord));
//
//        mockMvc.perform(mockRequest)
//        		.andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(result ->
//                    assertTrue(result.getResolvedException() 
//                    		instanceof InvalidRequestException))
//                .andExpect(result ->
//                	assertEquals("PatientRecord or ID must not be null!", 
//            		result.getResolvedException().getMessage()));
//        }
//    
//    @Test
//    public void updatePatientRecord_recordNotFound() throws Exception {
//        PatientRecord updatedRecord = PatientRecord.builder()
//                .patientId(5l)
//                .name("Sherlock Holmes")
//                .age(40)
//                .address("221B Baker Street")
//                .build();        
//
//        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/patient")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(updatedRecord));
//
//        mockMvc.perform(mockRequest)
//                .andExpect(status().isBadRequest())
//                .andExpect(result ->
//                    assertTrue(result.getResolvedException() 
//                    		instanceof InvalidRequestException))
//        .andExpect(result ->
//            assertEquals("Patient with ID 5 does not exist.", 
//            		result.getResolvedException().getMessage()));
//    }
//    
//    @Test
//    public void deletePatientById_success() throws Exception {
//        
//    	Mockito.when(patientRecordRepository.findById(RECORD_2.getPatientId()))
//        .thenReturn(Optional.of(RECORD_2));
//    	
//        mockMvc.perform(MockMvcRequestBuilders
//                .delete("/patient/2")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());              
//    }
//
//    @Test
//    public void deletePatientById_notFound() throws Exception {       
//
//        mockMvc.perform(MockMvcRequestBuilders
//                .delete("/patient/5")
//                .contentType(MediaType.APPLICATION_JSON))
//        		.andExpect(status().isBadRequest())
//                .andExpect(result ->
//                        assertTrue(result.getResolvedException() 
//                        		instanceof InvalidRequestException))
//                .andExpect(result ->
//                assertEquals("Patient with ID 5 does not exist.", 
//                		result.getResolvedException().getMessage()));
//    }
}

    
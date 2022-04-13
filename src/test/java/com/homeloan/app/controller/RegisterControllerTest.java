/*
 * package com.homeloan.app.controller;
 * 
 * 
 * import static org.hamcrest.CoreMatchers.is; import static
 * org.hamcrest.CoreMatchers.notNullValue; import static
 * org.junit.Assert.assertEquals; import static
 * org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
 * import static
 * org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
 * import static
 * org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
 * 
 * import java.util.ArrayList; import java.util.Arrays; import
 * java.util.Comparator; import java.util.Date; import java.util.List; import
 * java.util.stream.Collectors;
 * 
 * import org.junit.jupiter.api.Test; import org.mockito.Mockito; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; import
 * org.springframework.boot.test.mock.mockito.MockBean; import
 * org.springframework.http.MediaType; import
 * org.springframework.test.web.servlet.MockMvc; import
 * org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
 * import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
 * 
 * import com.fasterxml.jackson.databind.ObjectMapper; import
 * com.homeloan.app.model.Users; import com.homeloan.app.repo.UserRepository;
 * import com.homeloan.app.service.UserService;
 * 
 * //@RunWith(SpringRunner.class)
 * 
 * @WebMvcTest(value = RegisterController.class) public class
 * RegisterControllerTest {
 * 
 * @Autowired private MockMvc mockMvc;
 * 
 * @MockBean UserRepository userRepo;
 * 
 * @Autowired ObjectMapper mapper;
 * 
 * 
 * @MockBean UserService userService; Users mockUser = new Users() Users
 * mockUser1 = new Users(23,"AAA","ZZZ","jay23@gmail.com","9843322313",new
 * Date(1980-8-14),"330013","yes",new Date(2000-10-15)); User mockUser2 = new
 * User((long) 24,"BBB","YYY","jay24@gmail.com","9843322314",new
 * Date(1988-8-14),"320012","no",new Date(2006-10-15)); User mockUser3 = new
 * User((long) 25,"CCC","XXX","jay25@gmail.com","9843322315",new
 * Date(1990-8-14),"310011","yes",new Date(2010-10-15));
 * 
 * @Test void contextLoads() { }
 * 
 * List<User> userList = new ArrayList<> (Arrays.asList(mockUser1, mockUser2,
 * mockUser3));
 * 
 * @Test public void testGetAllUsers() throws Exception { List<User> userList =
 * new ArrayList<> (Arrays.asList(mockUser1, mockUser2, mockUser3));
 * 
 * Mockito.when(userService.getAllUsers()).thenReturn(userList);
 * 
 * mockMvc.perform(MockMvcRequestBuilders .get("/getusers")
 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isOk()) //200
 * .andDo(print()) .andExpect(jsonPath("$[1].active").value("no")); }
 * 
 * @Test public void testGetOnlyActiveUsers() throws Exception { List<User>
 * userList = new ArrayList<> (Arrays.asList(mockUser1, mockUser2, mockUser3));
 * 
 * Mockito.when(userService.getOnlyActiveUsers()).thenReturn(userList.stream().
 * filter(u -> u.getActive().equals("yes")).collect(Collectors.toList()));
 * 
 * mockMvc.perform(MockMvcRequestBuilders .get("/users")
 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isOk()) //200
 * .andDo(print()) .andExpect(jsonPath("$[1].active").value("yes")); }
 * 
 * @Test public void testGetUserById() throws Exception {
 * Mockito.when(userService.getUserById(mockUser1.getId()))
 * .thenReturn(mockUser1);
 * 
 * mockMvc.perform(MockMvcRequestBuilders .get("/user/23")
 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isOk())
 * .andExpect(jsonPath("$", notNullValue())) .andExpect(jsonPath("$.name",
 * is("AAA"))); }
 * 
 * @Test public void testAddUser() throws Exception { User mockUser =
 * User.builder() .id((long)26) .name("Jay26").surname("Vaja")
 * .email("jay26@gmail.com").phone("9834441223") .dob(new
 * Date(1980-8-14)).joiningdate(new Date(2010-8-14))
 * .active("yes").pincode("362520") .build();
 * 
 * Mockito.when(userService.saveUser(mockUser)).thenReturn(mockUser);
 * 
 * MockHttpServletRequestBuilder mockRequest =
 * MockMvcRequestBuilders.post("/register")
 * .contentType(MediaType.APPLICATION_JSON) .accept(MediaType.APPLICATION_JSON)
 * .content(this.mapper.writeValueAsString(mockUser));
 * 
 * 
 * mockMvc.perform(mockRequest) .andExpect(status().isOk())
 * .andExpect(jsonPath("$", notNullValue())) .andExpect(jsonPath("$",
 * is("26 inserted"))); }
 * 
 * @Test public void testUpdateUser() throws Exception { User updateMockUser =
 * User.builder() .id((long)25) .name("Jay26").surname("Vaja")
 * .email("jay26@gmail.com").phone("9834441223") .dob(new
 * Date(1980-8-14)).joiningdate(new Date(2010-8-14))
 * .active("yes").pincode("362520") .build();
 * 
 * Mockito.when(userService.getUserById(updateMockUser.getId()))
 * .thenReturn(updateMockUser);
 * 
 * Mockito.when(userService.updateUserId(updateMockUser,(long)
 * 25)).thenReturn(updateMockUser);
 * 
 * MockHttpServletRequestBuilder mockRequest =
 * MockMvcRequestBuilders.put("/edit/25")
 * .contentType(MediaType.APPLICATION_JSON) .accept(MediaType.APPLICATION_JSON)
 * .content(this.mapper.writeValueAsString(updateMockUser));
 * 
 * mockMvc.perform(mockRequest) .andExpect(status().isOk())
 * .andExpect(jsonPath("$", notNullValue())) .andExpect(jsonPath("$.id",
 * is(25))) .andExpect(jsonPath("$.name", is("Jay26"))); }
 * 
 * @Test public void testHardDeleteUser() throws Exception { Long id =
 * mockUser1.getId();
 * 
 * mockMvc.perform(MockMvcRequestBuilders .delete("/delete/"+id)
 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isOk())
 * .andExpect(result -> assertEquals("Id "+id+" Deleted Successfully",
 * result.getResponse().getContentAsString()));
 * 
 * }
 * 
 * @Test public void testSoftDeleteUser() throws Exception { Long id =
 * mockUser3.getId(); Mockito.when(userService.softDeleteUser("no",id))
 * .thenReturn(id);
 * 
 * mockMvc.perform(MockMvcRequestBuilders .delete("/delete/soft/"+id)
 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isOk())
 * .andExpect(result -> assertEquals("Id "+id+" Soft Deleted Successfully",
 * result.getResponse().getContentAsString())); }
 * 
 * @Test public void testSortUsersByName() throws Exception {
 * 
 * Comparator<User> byName = Comparator.comparing(User::getName);
 * Mockito.when(userService.sortUsersByName()).thenReturn(userList.stream().
 * sorted(byName).collect(Collectors.toList()));
 * 
 * mockMvc.perform(MockMvcRequestBuilders .get("/sort/name")
 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isOk()) //200
 * .andDo(print()) .andExpect(jsonPath("$[0].name").value("AAA")); }
 * 
 * @Test public void testSortUsersBySurName() throws Exception {
 * 
 * Comparator<User> bySurname = Comparator.comparing(User::getSurname);
 * Mockito.when(userService.sortUsersBySurname()).thenReturn(userList.stream().
 * sorted(bySurname).collect(Collectors.toList()));
 * 
 * mockMvc.perform(MockMvcRequestBuilders .get("/sort/surname")
 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isOk()) //200
 * .andDo(print()) .andExpect(jsonPath("$[0].name").value("CCC")); }
 * 
 * @Test public void testSortUsersByDob() throws Exception {
 * 
 * Comparator<User> byDob = Comparator.comparing(User::getDob);
 * Mockito.when(userService.sortUsersByDob()).thenReturn(userList.stream().
 * sorted(byDob).collect(Collectors.toList()));
 * 
 * mockMvc.perform(MockMvcRequestBuilders .get("/sort/dob")
 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isOk()); //200
 * }
 * 
 * @Test public void testSortUsersByJoiningDate() throws Exception {
 * 
 * Comparator<User> byJoiningDate = Comparator.comparing(User::getJoiningdate);
 * Mockito.when(userService.sortUsersByJoiningDate()).thenReturn(userList.stream
 * ().sorted(byJoiningDate).collect(Collectors.toList()));
 * 
 * mockMvc.perform(MockMvcRequestBuilders .get("/sort/joiningdate")
 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isOk()) //200
 * .andDo(print()) .andExpect(jsonPath("$[0].name").value("AAA")); }
 * 
 * @Test public void testSortUsersByPincode() throws Exception {
 * 
 * Comparator<User> byPincode = Comparator.comparing(User::getPincode);
 * Mockito.when(userService.sortUsersByPincode()).thenReturn(userList.stream().
 * sorted(byPincode).collect(Collectors.toList()));
 * 
 * mockMvc.perform(MockMvcRequestBuilders .get("/sort/pincode")
 * .contentType(MediaType.APPLICATION_JSON)) .andExpect(status().isOk()) //200
 * .andDo(print()) .andExpect(jsonPath("$[0].name").value("CCC")); }
 * 
 * }
 */
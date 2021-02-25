package ru.max.securitytask.springsecjwttask;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.max.securitytask.springsecjwttask.model.dto.AuthRequestDto;
import ru.max.securitytask.springsecjwttask.model.dto.RegisterRequestDto;
import ru.max.securitytask.springsecjwttask.model.entity.UserEntity;
import ru.max.securitytask.springsecjwttask.repository.UserRepository;
import ru.max.securitytask.springsecjwttask.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by maxxii on 25.02.2021.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private Gson gson = new Gson();
    private final String apiUrl = "/secret/api/v1";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Value("${jwt.header}")
    private String authotization;


    @Test
    @Sql(value = {"/delete-users.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void register() throws Exception {
        RegisterRequestDto register = new RegisterRequestDto("testing", "testing");
        RegisterRequestDto incorrect = new RegisterRequestDto("testing", null);
        mockMvc.perform(post(apiUrl + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(incorrect)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid login or password"));

        mockMvc.perform(post(apiUrl + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(register)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("The user has been registered"));

    }

    @Test
    public void login() throws Exception {
        AuthRequestDto wrong = new AuthRequestDto("testing", "incorrect");
        mockMvc.perform(post(apiUrl + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(wrong)))
                .andDo(print())
                .andExpect(status().isBadRequest());
        UserEntity user = userRepository.findByLogin(wrong.getLogin());
        Assertions.assertEquals(1, user.getFailedLoginAttempts());


        AuthRequestDto login = new AuthRequestDto("testing", "testing");
        mockMvc.perform(post(apiUrl + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
        UserEntity user2 = userRepository.findByLogin(login.getLogin());
        Assertions.assertEquals(0, user2.getFailedLoginAttempts());

    }

    @Test
    @Sql(value = {"/delete-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void loginLocked() throws Exception {
        AuthRequestDto login = new AuthRequestDto("testing", "testing");
        UserEntity user = userRepository.findByLogin(login.getLogin());
        user.setLoginDisabled(true);
        userRepository.save(user);
        mockMvc.perform(post(apiUrl + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(login)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User account is locked"));
    }

    @Test
    public void paymentWithoutToken() throws Exception {
        mockMvc.perform(post(apiUrl + "/payment"))
                .andDo(print())
                .andExpect(status().is(403));
    }

    @Test
    @Sql(value = {"/delete-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void payment() throws Exception {
        AuthRequestDto login = new AuthRequestDto("tempuser", "tempuser");
        UserEntity tempUser = new UserEntity();
        tempUser.setLogin(login.getLogin());
        tempUser.setPassword(login.getPassword());
        userService.saveUser(tempUser);
        MvcResult mvcResult = mockMvc.perform(post(apiUrl + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();
        String token = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.token");

        mockMvc.perform(post(apiUrl + "/payment").header(authotization, token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Payment completed successfully"));
        UserEntity user = userService.getUserByLogin(login.getLogin());
        Assertions.assertEquals(6.9,user.getBalance());
    }

    @Test
    @Sql(value = {"/delete-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void noMoneyForPay() throws Exception {
        AuthRequestDto login = new AuthRequestDto("tempuser", "tempuser");
        UserEntity tempUser = new UserEntity();
        tempUser.setLogin(login.getLogin());
        tempUser.setPassword(login.getPassword());
        userService.saveUser(tempUser);
        UserEntity user = userService.getUserByLogin(tempUser.getLogin());
        user.setBalance(0.0);
        userRepository.save(user);

        MvcResult mvcResult = mockMvc.perform(post(apiUrl + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();
        String token = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.token");

        mockMvc.perform(post(apiUrl + "/payment").header(authotization, token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Not enough cash to pay"));
    }
}

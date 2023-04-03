package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mvc;
    ObjectMapper om = new ObjectMapper();
    UserController userController;

    @BeforeEach
    void setup() {
        userController = new UserController();
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
        om.registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    @Test
    void findAll() {
        User userOne = new User();
        userOne.setEmail("email1@yandex.ru");
        userOne.setLogin("login1");
        userOne.setName("Name1");
        userOne.setBirthday(LocalDate.of(2015, 3, 15));
        User user1 = userController.create(userOne);
        String jsonRequest = om.writeValueAsString(user1);
        mvc.perform(get("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().is(200)).andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].name").value("Name1"));

        User userTwo = new User();
        userTwo.setEmail("email2@yandex.ru");
        userTwo.setLogin("login2");
        userTwo.setName("Name2");
        userTwo.setBirthday(LocalDate.of(2015, 3, 15));
        User user2 = userController.create(userTwo);
        String jsonRequest2 = om.writeValueAsString(user2);
        mvc.perform(get("/users").contentType("application/json").content(jsonRequest2)).andDo(print()).andExpect(status().is(200)).andExpect(jsonPath("$", hasSize(2))).andExpect(jsonPath("$[1].name").value("Name2"));
    }

    @SneakyThrows
    @Test
    void createEmptyUser() {
        String jsonRequest = om.writeValueAsString(" ");

        mvc.perform(post("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void createNormalUser() {
        User userOne = new User();
        userOne.setEmail("email1@yandex.ru");
        userOne.setLogin("login1");
        userOne.setName("Name1");
        userOne.setBirthday(LocalDate.of(2015, 3, 15));
        String jsonRequest = om.writeValueAsString(userOne);

        mvc.perform(post("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().is(200)).andExpect(jsonPath("$.name", Matchers.containsString("Name1"))).andExpect(jsonPath("$.email").value("email1@yandex.ru"));
    }

    @SneakyThrows
    @Test
    void createUserWrongEmail() {
        User userOne = new User();
        userOne.setEmail("email1yandex.ru");
        userOne.setLogin("login1");
        userOne.setName("Name1");
        userOne.setBirthday(LocalDate.of(2015, 3, 15));
        String jsonRequest = om.writeValueAsString(userOne);

        mvc.perform(post("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void createUserWrongLogin() {
        User userOne = new User();
        userOne.setEmail("email1@yandex.ru");
        userOne.setLogin("lo gi n1");
        userOne.setName("Name1");
        userOne.setBirthday(LocalDate.of(2015, 3, 15));
        String jsonRequest = om.writeValueAsString(userOne);

        mvc.perform(post("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void createUserWrongBirthday() {
        User userOne = new User();
        userOne.setEmail("email1@yandex.ru");
        userOne.setLogin("login1");
        userOne.setName("Name1");
        userOne.setBirthday(LocalDate.of(2025, 3, 15));
        String jsonRequest = om.writeValueAsString(userOne);

        mvc.perform(post("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void putNormalUser() {
        User userOne = new User();
        userOne.setEmail("email1@yandex.ru");
        userOne.setLogin("login1");
        userOne.setName("Name1");
        userOne.setBirthday(LocalDate.of(2015, 3, 15));
        User user1 = userController.create(userOne);
        User user2_1 = new User(1, "email2@yandex.ru", "login2", "Name2", LocalDate.of(2015, 3, 15));
        String jsonRequest = om.writeValueAsString(user2_1);

        mvc.perform(put("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().is(200)).andExpect(jsonPath("$.name").value("Name2")).andExpect(jsonPath("$.email").value("email2@yandex.ru"));
    }

    @SneakyThrows
    @Test
    void putUserWrongId() {
        User userOne = new User();
        userOne.setEmail("email1@yandex.ru");
        userOne.setLogin("login1");
        userOne.setName("Name1");
        userOne.setBirthday(LocalDate.of(2015, 3, 15));
        User user1 = userController.create(userOne);
        User userTwo = new User(999, "email2@yandex.ru", "login2", "Name2", LocalDate.of(2015, 3, 15));

        String jsonRequest = om.writeValueAsString(userTwo);

        mvc.perform(put("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void putUserWrongEmail() {
        User userOne = new User();
        userOne.setEmail("email1@yandex.ru");
        userOne.setLogin("login1");
        userOne.setName("Name1");
        userOne.setBirthday(LocalDate.of(2015, 3, 15));
        User user1 = userController.create(userOne);
        User userTwo = new User(1, "email1 yandex.ru", "login2", "Name2", LocalDate.of(2015, 3, 15));

        String jsonRequest = om.writeValueAsString(userTwo);

        mvc.perform(put("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void putUserWrongLogin() {
        User userOne = new User();
        userOne.setEmail("email1@yandex.ru");
        userOne.setLogin("login1");
        userOne.setName("Name1");
        userOne.setBirthday(LocalDate.of(2015, 3, 15));
        User user1 = userController.create(userOne);
        User userTwo = new User(1, "email1@yandex.ru", "log in2", "Name2", LocalDate.of(2015, 3, 15));

        String jsonRequest = om.writeValueAsString(userTwo);

        mvc.perform(put("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void putUserWrongBirthday() {
        User userOne = new User();
        userOne.setEmail("email1@yandex.ru");
        userOne.setLogin("login1");
        userOne.setName("Name1");
        userOne.setBirthday(LocalDate.of(2015, 3, 15));
        User user1 = userController.create(userOne);
        User user2_1 = new User(1, "email1@yandex.ru", "login2", "Name2", LocalDate.of(2025, 3, 15));

        String jsonRequest = om.writeValueAsString(user2_1);

        mvc.perform(put("/users").contentType("application/json").content(jsonRequest)).andDo(print()).andExpect(status().isBadRequest());
    }
}
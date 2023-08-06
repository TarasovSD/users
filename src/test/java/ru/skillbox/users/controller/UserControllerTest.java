package ru.skillbox.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.skillbox.users.dto.UserDto;
import ru.skillbox.users.dto.UserFullDto;
import ru.skillbox.users.entity.City;
import ru.skillbox.users.entity.Gender;
import ru.skillbox.users.entity.User;
import ru.skillbox.users.mapper.UserMapper;
import ru.skillbox.users.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ExtendWith(SpringExtension.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    private UserDto userDto;

    private UserDto updatedUserDto;

    private UserFullDto userFullDto;

    private UserFullDto updatedUserFullDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(null,
                "User1First",
                "User1Last",
                "User1Patr",
                Gender.MALE,
                "11.11.2001",
                1,
                "link1",
                "description1",
                "nickname",
                "email1@ya.ru",
                "79215674567"
        );
        updatedUserDto = new UserDto(null,
                "UpdatedUser1First",
                "Updated1Last",
                "Updated1Patr",
                Gender.MALE,
                "11.11.2001",
                1,
                "updatedLink1",
                "updatedDescription1",
                "Updated nickname",
                "updatedemail1@ya.ru",
                "79215677787"
        );
        City city = new City(1, "Moscow");
        User savedUser = new User(1, "User1First",
                "User1Last",
                "User1Patr",
                Gender.MALE,
                "11.11.2001",
                city,
                "link1",
                "description1",
                "nickname",
                "email1@ya.ru",
                "79215674567");
        User updatedUser = new User(1,
                "UpdatedUser1First",
                "Updated1Last",
                "Updated1Patr",
                Gender.MALE,
                "11.11.2001",
                city,
                "updatedLink1",
                "updatedDescription1",
                "Updated nickname",
                "updatedemail1@ya.ru",
                "79215677787");
        userFullDto = UserMapper.toUserFullDto(savedUser);
        updatedUserFullDto = UserMapper.toUserFullDto(updatedUser);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void createUser() throws Exception {
        when(userService.createUser(userDto)).thenReturn(userFullDto);

        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(userDto))
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.equalTo(userFullDto.id())))
                .andExpect(jsonPath("$.firstName", Matchers.equalTo(userFullDto.firstName())))
                .andExpect(jsonPath("$.lastName", Matchers.equalTo(userFullDto.lastName())))
                .andExpect(jsonPath("$.patronymic", Matchers.equalTo(userFullDto.patronymic())))
                .andExpect(jsonPath("$.gender", Matchers.equalTo(userFullDto.gender().toString())))
                .andExpect(jsonPath("$.birthday", Matchers.equalTo(userFullDto.birthday())))
                .andExpect(jsonPath("$.city.id", Matchers.equalTo(userFullDto.city().id())))
                .andExpect(jsonPath("$.city.name", Matchers.equalTo(userFullDto.city().name())))
                .andExpect(jsonPath("$.link", Matchers.equalTo(userFullDto.link())))
                .andExpect(jsonPath("$.description", Matchers.equalTo(userFullDto.description())))
                .andExpect(jsonPath("$.nickname", Matchers.equalTo(userFullDto.nickname())))
                .andExpect(jsonPath("$.email", Matchers.equalTo(userFullDto.email())))
                .andExpect(jsonPath("$.phone", Matchers.equalTo(userFullDto.phone())));

        verify(userService, times(1))
                .createUser(userDto);
    }

    @Test
    public void getUser() throws Exception {
        when(userService.getUser(1)).thenReturn(userFullDto);

        ResultActions resultActions = mockMvc.perform(get("/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.equalTo(userFullDto.id())))
                .andExpect(jsonPath("$.firstName", Matchers.equalTo(userFullDto.firstName())))
                .andExpect(jsonPath("$.lastName", Matchers.equalTo(userFullDto.lastName())))
                .andExpect(jsonPath("$.patronymic", Matchers.equalTo(userFullDto.patronymic())))
                .andExpect(jsonPath("$.gender", Matchers.equalTo(userFullDto.gender().toString())))
                .andExpect(jsonPath("$.birthday", Matchers.equalTo(userFullDto.birthday())))
                .andExpect(jsonPath("$.city.id", Matchers.equalTo(userFullDto.city().id())))
                .andExpect(jsonPath("$.city.name", Matchers.equalTo(userFullDto.city().name())))
                .andExpect(jsonPath("$.link", Matchers.equalTo(userFullDto.link())))
                .andExpect(jsonPath("$.description", Matchers.equalTo(userFullDto.description())))
                .andExpect(jsonPath("$.nickname", Matchers.equalTo(userFullDto.nickname())))
                .andExpect(jsonPath("$.email", Matchers.equalTo(userFullDto.email())))
                .andExpect(jsonPath("$.phone", Matchers.equalTo(userFullDto.phone())));

        verify(userService, times(1))
                .getUser(1);
    }

    @Test
    public void deleteUser() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/users/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());

        verify(userService, times(1))
                .deleteUser(1);
    }

    @Test
    public void updateUser() throws Exception {
        when(userService.updateUser(updatedUserDto, 1))
                .thenReturn(updatedUserFullDto);

        ResultActions resultActions = mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedUserDto))
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.equalTo(updatedUserFullDto.id())))
                .andExpect(jsonPath("$.firstName", Matchers.equalTo(updatedUserFullDto.firstName())))
                .andExpect(jsonPath("$.lastName", Matchers.equalTo(updatedUserFullDto.lastName())))
                .andExpect(jsonPath("$.patronymic", Matchers.equalTo(updatedUserFullDto.patronymic())))
                .andExpect(jsonPath("$.gender", Matchers.equalTo(updatedUserFullDto.gender().toString())))
                .andExpect(jsonPath("$.birthday", Matchers.equalTo(updatedUserFullDto.birthday())))
                .andExpect(jsonPath("$.city.id", Matchers.equalTo(updatedUserFullDto.city().id())))
                .andExpect(jsonPath("$.city.name", Matchers.equalTo(updatedUserFullDto.city().name())))
                .andExpect(jsonPath("$.link", Matchers.equalTo(updatedUserFullDto.link())))
                .andExpect(jsonPath("$.description", Matchers.equalTo(updatedUserFullDto.description())))
                .andExpect(jsonPath("$.nickname", Matchers.equalTo(updatedUserFullDto.nickname())))
                .andExpect(jsonPath("$.email", Matchers.equalTo(updatedUserFullDto.email())))
                .andExpect(jsonPath("$.phone", Matchers.equalTo(updatedUserFullDto.phone())));

        verify(userService, times(1))
                .updateUser(updatedUserDto, 1);
    }

    @Test
    public void subscribeToUser() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/users/1/subscribe/2")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());

        verify(userService, times(1))
                .subscribeToUser(1, 2);
    }

    @Test
    public void unsubscribeToUser() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/users/1/unsubscribe/2")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());

        verify(userService, times(1))
                .unsubscribeFromUser(1, 2);
    }
}
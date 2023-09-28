package ru.skillbox.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.users.containers.PostgresContainerWrapper;
import ru.skillbox.users.dto.CityDto;
import ru.skillbox.users.dto.UserDto;
import ru.skillbox.users.entity.Gender;
import ru.skillbox.users.entity.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.skillbox.users.utils.TestUtils.stringToObject;

@SpringBootTest(properties = {"spring.liquibase.enabled=false"})
@AutoConfigureMockMvc
@Testcontainers
public class UserControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer<PostgresContainerWrapper> postgresContainer = new PostgresContainerWrapper();

    @DynamicPropertySource
    public static void initSystemParams(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private UserDto userDto;

    private UserDto userDto2;

    private UserDto forUpdate;

    private CityDto cityDto;

    @BeforeEach
    void setUp() {
        cityDto = new CityDto(null, "Saint Petersburg");
        userDto = new UserDto(null, "Vasiliy", "Vasiliyevich", "Vasiliev", Gender.MALE,
                "11.11.2005", 1, "12345.ru", "Vasiliy's profile", "Vasyan",
                "evas@mail.ru", "900");

        forUpdate = new UserDto(1, "Ivan", "Vasiliyevich", "Vasiliev", Gender.MALE,
                "11.11.1589", 1, "867865.ru", "Ivan's profile", "Vano",
                "evan@mail.ru", "800");

        userDto2 = new UserDto(null, "Ivan", "Vasiliyevich", "Vasiliev", Gender.MALE,
                "11.11.1589", 1, "867865.ru", "Ivan's profile", "Vano",
                "evan@mail.ru", "800");
    }

    @Test
    void createUser() throws Exception {
        this.mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cityDto)));

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(jsonPath("$.firstName", Matchers.equalTo(userDto.firstName())))
                .andExpect(jsonPath("$.lastName", Matchers.equalTo(userDto.lastName())))
                .andExpect(jsonPath("$.patronymic", Matchers.equalTo(userDto.patronymic())))
                .andExpect(jsonPath("$.gender", Matchers.equalTo(userDto.gender().toString())))
                .andExpect(jsonPath("$.birthday", Matchers.equalTo(userDto.birthday())))
                .andExpect(jsonPath("$.city.id", Matchers.equalTo(userDto.cityId())))
                .andExpect(jsonPath("$.city.name", Matchers.equalTo(cityDto.name())))
                .andExpect(jsonPath("$.link", Matchers.equalTo(userDto.link())))
                .andExpect(jsonPath("$.description", Matchers.equalTo(userDto.description())))
                .andExpect(jsonPath("$.nickname", Matchers.equalTo(userDto.nickname())))
                .andExpect(jsonPath("$.email", Matchers.equalTo(userDto.email())))
                .andExpect(jsonPath("$.phone", Matchers.equalTo(userDto.phone())));
    }

    @Test
    void getUser() throws Exception {
        this.mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cityDto)));

        String saveResponse = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andReturn().getResponse()
                .getContentAsString();

        User saveResponseString = stringToObject(saveResponse, User.class);

        Integer id = saveResponseString.getId();

        String url = "/users/" + id;

        this.mockMvc.perform(get(url))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.equalTo(id)))
                .andExpect(jsonPath("$.firstName", Matchers.equalTo(userDto.firstName())))
                .andExpect(jsonPath("$.lastName", Matchers.equalTo(userDto.lastName())))
                .andExpect(jsonPath("$.patronymic", Matchers.equalTo(userDto.patronymic())))
                .andExpect(jsonPath("$.gender", Matchers.equalTo(userDto.gender().toString())))
                .andExpect(jsonPath("$.birthday", Matchers.equalTo(userDto.birthday())))
                .andExpect(jsonPath("$.city.id", Matchers.equalTo(userDto.cityId())))
                .andExpect(jsonPath("$.city.name", Matchers.equalTo(cityDto.name())))
                .andExpect(jsonPath("$.link", Matchers.equalTo(userDto.link())))
                .andExpect(jsonPath("$.description", Matchers.equalTo(userDto.description())))
                .andExpect(jsonPath("$.nickname", Matchers.equalTo(userDto.nickname())))
                .andExpect(jsonPath("$.email", Matchers.equalTo(userDto.email())))
                .andExpect(jsonPath("$.phone", Matchers.equalTo(userDto.phone())));
    }

    @Test
    void deleteUser() throws Exception {
        this.mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cityDto)));

        String savedResponse = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andReturn().getResponse()
                .getContentAsString();

        User saveResponseString = stringToObject(savedResponse, User.class);

        Integer id = saveResponseString.getId();

        String url = "/users/" + id;

        this.mockMvc.perform(delete(url))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser() throws Exception {
        this.mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cityDto)));

        String savedResponse = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andReturn().getResponse()
                .getContentAsString();

        User saveResponseString = stringToObject(savedResponse, User.class);

        Integer id = saveResponseString.getId();

        String url = "/users/" + id;

        this.mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(forUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(id)))
                .andExpect(jsonPath("$.firstName", Matchers.equalTo(forUpdate.firstName())))
                .andExpect(jsonPath("$.lastName", Matchers.equalTo(forUpdate.lastName())))
                .andExpect(jsonPath("$.patronymic", Matchers.equalTo(forUpdate.patronymic())))
                .andExpect(jsonPath("$.gender", Matchers.equalTo(forUpdate.gender().toString())))
                .andExpect(jsonPath("$.birthday", Matchers.equalTo(forUpdate.birthday())))
                .andExpect(jsonPath("$.city.id", Matchers.equalTo(forUpdate.cityId())))
                .andExpect(jsonPath("$.city.name", Matchers.equalTo(cityDto.name())))
                .andExpect(jsonPath("$.link", Matchers.equalTo(forUpdate.link())))
                .andExpect(jsonPath("$.description", Matchers.equalTo(forUpdate.description())))
                .andExpect(jsonPath("$.nickname", Matchers.equalTo(forUpdate.nickname())))
                .andExpect(jsonPath("$.email", Matchers.equalTo(forUpdate.email())))
                .andExpect(jsonPath("$.phone", Matchers.equalTo(forUpdate.phone())));
    }

    @Test
    void subscribeToUserAndUnsubscribeFromUser() throws Exception {
        this.mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cityDto)));

        String savedResponse = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andReturn().getResponse()
                .getContentAsString();

        String savedResponse2 = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto2)))
                .andReturn().getResponse()
                .getContentAsString();

        User saveResponseString = stringToObject(savedResponse, User.class);

        Integer id = saveResponseString.getId();

        User saveResponseString2 = stringToObject(savedResponse2, User.class);

        Integer id2 = saveResponseString2.getId();

        String url = "/users/" + id + "/subscribe/" + id2;

        this.mockMvc.perform(post(url))
                .andExpect(status().isOk());

        String url1 = "/users/" + id + "/unsubscribe/" + id2;

        this.mockMvc.perform(delete(url1))
                .andExpect(status().isOk());
    }
}

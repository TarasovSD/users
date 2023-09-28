package ru.skillbox.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.skillbox.users.containers.PostgresContainerWrapper;
import ru.skillbox.users.dto.CityDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.skillbox.users.utils.TestUtils.stringToObject;

@SpringBootTest(properties = {"spring.liquibase.enabled=false"})
@AutoConfigureMockMvc
@Testcontainers
public class CityControllerIntegrationTest {

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

    private CityDto cityDto;

    @BeforeEach
    void setUp() {
        cityDto = new CityDto(null, "Saint Petersburg");
    }

    @Test
    void applicationContextStartedSuccessfullyTest(ApplicationContext context) throws Exception {
        assertThat(context).isNotNull();
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health")).andExpect(status().isOk());
    }

    @Test
    void createCity() throws Exception {
        this.mockMvc.perform(post("/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cityDto)))
                .andExpect(jsonPath("$.name", Matchers.equalTo(cityDto.name())));
    }

    @Test
    void getCity() throws Exception {
        String saveResponse = this.mockMvc.perform(post("/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cityDto)))
                .andReturn().getResponse()
                .getContentAsString();
        CityDto saveResponseString = stringToObject(saveResponse, CityDto.class);
        Integer id = saveResponseString.id();

        String url = "/cities/" + id;

        this.mockMvc.perform(get(url))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.equalTo(id)))
                .andExpect(jsonPath("$.name", Matchers.equalTo(cityDto.name())));
    }

    @Test
    void deleterCity() throws Exception {
        String saveResponse = this.mockMvc.perform(post("/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cityDto)))
                .andReturn().getResponse()
                .getContentAsString();
        CityDto saveResponseString = stringToObject(saveResponse, CityDto.class);
        Integer id = saveResponseString.id();

        String url = "/cities/" + id;

        this.mockMvc.perform(delete(url))
                .andExpect(status().isOk());

        this.mockMvc.perform(get(url))
                .andExpect(status().isNotFound());
    }

    @Test
    void putCity() throws Exception {
        String saveResponse = this.mockMvc.perform(post("/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(cityDto)))
                .andReturn().getResponse()
                .getContentAsString();
        CityDto saveResponseString = stringToObject(saveResponse, CityDto.class);
        Integer id = saveResponseString.id();

        String url = "/cities/" + id;

        CityDto forUpdate = new CityDto(id, "Moscow");

        this.mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(forUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(forUpdate.id())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(forUpdate.name())));

        this.mockMvc.perform(get(url))
                .andExpect(jsonPath("$.name", Matchers.equalTo(forUpdate.name())));
    }
}

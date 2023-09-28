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
import ru.skillbox.users.dto.CityDto;
import ru.skillbox.users.service.CityService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = {"spring.liquibase.enabled=false"})
@ExtendWith(SpringExtension.class)
class CityControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @Autowired
    private ObjectMapper mapper;

    private CityDto cityDto;

    private CityDto createdCityDto;

    private CityDto updatedCityDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        cityDto = new CityDto(null, "Moscow");
        createdCityDto = new CityDto(1, "Moscow");
        updatedCityDto = new CityDto(1, "SPb");
    }

    @Test
    public void createCity() throws Exception {
        when(cityService.createCity(any())).thenReturn(createdCityDto);

        ResultActions resultActions = mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cityDto)));

        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.equalTo(createdCityDto.id())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(createdCityDto.name())));

        verify(cityService, times(1))
                .createCity(any());
    }

    @Test
    public void getCity() throws Exception {
        when(cityService.getCity(1)).thenReturn(createdCityDto);

        ResultActions resultActions = mockMvc.perform(get("/cities/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.equalTo(createdCityDto.id())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(createdCityDto.name())));

        verify(cityService, times(1))
                .getCity(anyInt());
    }

    @Test
    public void deleteCity() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/cities/1")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(cityService, times(1))
                .deleteCity(anyInt());
    }

    @Test
    public void updateCity() throws Exception {
        when(cityService.updateCity(cityDto, 1)).thenReturn(updatedCityDto);

        ResultActions resultActions = mockMvc.perform(put("/cities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cityDto)));

        resultActions.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Matchers.equalTo(updatedCityDto.id())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(updatedCityDto.name())));

        verify(cityService, times(1))
                .updateCity(any(), anyInt());
    }
}
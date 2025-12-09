package org.adso.minimarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;

@WebMvcTest(UserService.class)
public class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;
}

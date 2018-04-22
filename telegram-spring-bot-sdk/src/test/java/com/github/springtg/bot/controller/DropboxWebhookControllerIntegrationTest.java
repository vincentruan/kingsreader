package com.github.springtg.bot.controller;

import com.github.springtg.bot.service.DeltaUsersParserService;
import com.github.springtg.bot.service.DropboxClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Integration test for {@link DropboxWebhookController}.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DropboxWebhookControllerIntegrationTest {

    private static final String ANY_CHALLENGE = "anyChallenge";
    private static final String ANY_NOTIFICATION = "anyNotification";
    private static final String FIRST_USER = "firstUser";

    @MockBean
    private DropboxClientService dropboxClientService;

    @MockBean
    private DeltaUsersParserService deltaUsersParserServiceMock;

    @Resource
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        initMocks(this);
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldReturnChallengeRequestParamItself() throws Exception {
        // GIVEN

        // WHEN
        final MvcResult mvcResult = mockMvc.perform(get("/webhook").param("challenge", ANY_CHALLENGE)).andReturn();

        // THEN
        assertEquals(ANY_CHALLENGE, mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void shouldDontLogChangedFilesIfThereAreNoUsersInNotification() throws Exception {
        // GIVEN
        when(deltaUsersParserServiceMock.getUsers(ANY_NOTIFICATION)).thenReturn(emptyList());

        // WHEN
        mockMvc.perform(post("/webhook").content(ANY_NOTIFICATION));

        // THEN
        verify(dropboxClientService, never()).logChangedFiles(ArgumentMatchers.anyString());
    }

    @Test
    public void shouldLogChangedFilesIfThereAreNoChangedFiles() throws Exception {
        // GIVEN
        when(deltaUsersParserServiceMock.getUsers(ANY_NOTIFICATION)).thenReturn(asList(FIRST_USER));

        // WHEN
        mockMvc.perform(post("/webhook").content(ANY_NOTIFICATION));

        // THEN
        verify(dropboxClientService).logChangedFiles(FIRST_USER);
    }

}
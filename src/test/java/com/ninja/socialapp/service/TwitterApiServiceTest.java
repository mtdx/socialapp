package com.ninja.socialapp.service;

import com.ninja.socialapp.SocialappApp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import twitter4j.Twitter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the TwitterApiService.
 *
 * @see TwitterApiService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
@Transactional
public class TwitterApiServiceTest {

    @Autowired
    private TwitterErrorService twitterErrorService;

    @Autowired
    private TwitterAccountService twitterAccountService;

    @Mock
    private Twitter twitter;

    private TwitterApiService twitterApiService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testDemo() throws Exception {
        // Setup
        assertThat(true);
    }
}

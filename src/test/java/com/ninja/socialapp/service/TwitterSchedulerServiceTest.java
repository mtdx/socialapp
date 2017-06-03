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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the TwitterSchedulerService.
 *
 * @see TwitterSchedulerService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocialappApp.class)
@Transactional
public class TwitterSchedulerServiceTest {

    @Autowired
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

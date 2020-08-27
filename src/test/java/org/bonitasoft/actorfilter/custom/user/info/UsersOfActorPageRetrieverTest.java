package org.bonitasoft.actorfilter.custom.user.info;

import org.bonitasoft.engine.api.ProcessAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UsersOfActorPageRetrieverTest {

    private static final int MAX_PAGE_SIZE = 2;
    private static final long PROCESS_DEFINITION_ID = 100;
    private static final String ACTOR_NAME = "employee";
    @Mock(lenient = true)
    private ProcessAPI processAPI;
    private UsersOfActorPageRetriever retriever;

    @BeforeEach
    public void setUp() {
        retriever = new UsersOfActorPageRetriever(processAPI, PROCESS_DEFINITION_ID, ACTOR_NAME, MAX_PAGE_SIZE);
    }

    @Test
    void getMaxPageSize_should_return_the_max_page_size_supplied_in_the_constructor() {
        //when
        int maxPageSize = retriever.getMaxPageSize();

        //then
        assertThat(maxPageSize).isEqualTo(MAX_PAGE_SIZE);
    }


    @Test
    void nextPage_should_retrive_the_next_available_page_and_move_pointer_to_the_following_one() {
        //given
        when(processAPI.getUserIdsForActor(PROCESS_DEFINITION_ID, ACTOR_NAME, 0, MAX_PAGE_SIZE)).thenReturn(asList(1L, 2L));
        when(processAPI.getUserIdsForActor(PROCESS_DEFINITION_ID, ACTOR_NAME, MAX_PAGE_SIZE, MAX_PAGE_SIZE)).thenReturn(asList(3L, 4L));
        when(processAPI.getUserIdsForActor(PROCESS_DEFINITION_ID, ACTOR_NAME, 2 * MAX_PAGE_SIZE, MAX_PAGE_SIZE)).thenReturn(asList(5L));
        when(processAPI.getUserIdsForActor(PROCESS_DEFINITION_ID, ACTOR_NAME, 3 * MAX_PAGE_SIZE, MAX_PAGE_SIZE)).thenReturn(emptyList());

        //when
        List<Long> page1 = retriever.nextPage();
        List<Long> page2 = retriever.nextPage();
        List<Long> page3 = retriever.nextPage();
        List<Long> page4 = retriever.nextPage();

        //then
        assertThat(page1).containsExactly(1L, 2L);
        assertThat(page2).containsExactly(3L, 4L);
        assertThat(page3).containsExactly(5L);
        assertThat(page4).isEmpty();
    }

}

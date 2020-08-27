package org.bonitasoft.actorfilter.custom.user.info;

import org.bonitasoft.engine.api.APIAccessor;
import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.bonitasoft.engine.connector.EngineExecutionContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CustomUserInfoUserFilterTest {

    private static final String INFO_NAME_KEY = "customUserInfoName";

    private static final String INFO_VALUE_KEY = "customUserInfoValue";

    private static final String USE_PARTIAL_MATCH_KEY = "usePartialMatch";

    private static final String AUTO_ASSIGN_KEY = "autoAssign";

    private static final String ACTOR_NAME = "employee";

    private static final String INFO_NAME = "skills";

    private static final String INFO_VALUE = "java";

    private static final long PROCESS_DEFINITION_ID = 10;

    @Mock(lenient = true)
    private APIAccessor accessor;

    @Mock(lenient = true)
    private IdentityAPI identityAPI;

    @Mock(lenient = true)
    private ProcessAPI processAPI;

    @InjectMocks
    private CustomUserInfoUserFilter filter;

    @BeforeEach
    void setUp() {
        filter.setAPIAccessor(accessor);

        final EngineExecutionContext executionContext = new EngineExecutionContext();
        executionContext.setProcessDefinitionId(PROCESS_DEFINITION_ID);
        filter.setExecutionContext(executionContext);

        when(accessor.getIdentityAPI()).thenReturn(identityAPI);
        when(accessor.getProcessAPI()).thenReturn(processAPI);
    }

    private void addNameAndValueToFilter() {
        final Map<String, Object> parameters = new HashMap<>(2);
        parameters.put(INFO_NAME_KEY, INFO_NAME);
        parameters.put(INFO_VALUE_KEY, INFO_VALUE);
        filter.setInputParameters(parameters);
    }

    @Test
    void validateInputParameters_with_valid_inputs() throws Exception {
        // given
        addNameAndValueToFilter();

        // when
        filter.validateInputParameters();

        // then no exception
    }

    @Test
    void validateInputParameters_throws_ConnectorValidationException_if_customUserInfoName_is_not_set() {
        // given
        // only value is set
        filter = new CustomUserInfoUserFilter();
        filter.setInputParameters(Collections.singletonMap(INFO_VALUE_KEY, INFO_VALUE));

        // when
        assertThrows(ConnectorValidationException.class, () ->
                filter.validateInputParameters()
        );

        // then exception
    }

    @Test
    void validateInputParameters_throws_ConnectorValidationException_if_customUserInfoName_is_set_to_null_or_empty() {
        // given
        // only value has valid value
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(INFO_NAME_KEY, null);
        parameters.put(INFO_VALUE_KEY, INFO_VALUE);
        filter.setInputParameters(parameters);

        // when
        assertThrows(ConnectorValidationException.class, () ->
                filter.validateInputParameters()
        );

        // then exception
    }

    @Test
    void validateInputParameters_throws_ConnectorValidationException_if_customUserInfoValue_is_not_set() {
        // given
        // only name is set
        filter = new CustomUserInfoUserFilter();
        filter.setInputParameters(Collections.singletonMap(INFO_NAME_KEY, INFO_NAME));

        // when
        assertThrows(ConnectorValidationException.class, () ->
                filter.validateInputParameters()
        );

        // then exception
    }

    @Test
    void validateInputParameters_throws_ConnectorValidationException_if_customUserInfoValue_is_set_to_null_or_empty() {
        // given
        // only name has valid value
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put(INFO_NAME_KEY, INFO_NAME);
        parameters.put(INFO_VALUE_KEY, null);
        filter.setInputParameters(parameters);

        // when
        assertThrows(ConnectorValidationException.class, () ->
                filter.validateInputParameters()
        );

        //then exception
    }

    @Test
    void filter_should_return_all_users_with_given_user_info_when_all_users_are_actors() throws Exception {
        // given
        final List<Long> usersWithInfo = Arrays.asList(1L, 2L);
        final List<Long> usersOfActor = Arrays.asList(1L, 2L, 3L);
        when(identityAPI.getUserIdsWithCustomUserInfo(eq(INFO_NAME), eq(INFO_VALUE), eq(false), eq(0), anyInt())).thenReturn(usersWithInfo);
        when(processAPI.getUserIdsForActor(eq(PROCESS_DEFINITION_ID), eq(ACTOR_NAME), eq(0), anyInt())).thenReturn(usersOfActor);
        addNameAndValueToFilter();
        // when
        final List<Long> filteredUsers = filter.filter(ACTOR_NAME);

        // then
        assertThat(filteredUsers).containsExactlyElementsOf(usersWithInfo);
    }

    @Test
    void filter_should_return_only_users_with_given_user_info_and_actor() throws Exception {
        // given
        addNameAndValueToFilter();
        List<Long> usersWithInfo = Arrays.asList(1L, 2L, 3L, 7L);
        List<Long> usersOfActor = Arrays.asList(2L, 3L, 8L);
        when(identityAPI.getUserIdsWithCustomUserInfo(eq(INFO_NAME), eq(INFO_VALUE), eq(false), eq(0), anyInt())).thenReturn(usersWithInfo);
        when(processAPI.getUserIdsForActor(eq(PROCESS_DEFINITION_ID), eq(ACTOR_NAME), eq(0), anyInt())).thenReturn(usersOfActor);

        // when
        List<Long> filteredUsers = filter.filter(ACTOR_NAME);

        // then
        assertThat(filteredUsers).containsExactly(2L, 3L);
    }


    @Test
    void shouldAutoAssign_should_return_true_if_property_autoAssign_is_not_set() {
        //when
        boolean autoAssign = filter.shouldAutoAssignTaskIfSingleResult();

        //then
        assertThat(autoAssign).isTrue();
    }

    @Test
    void shoulAutoAssign_should_return_false_if_property_autoAssign_is_set_to_false() {
        //given
        filter.setInputParameters(Collections.singletonMap(AUTO_ASSIGN_KEY, false));

        //when
        boolean autoAssign = filter.shouldAutoAssignTaskIfSingleResult();

        //then
        assertThat(autoAssign).isFalse();
    }

    @Test
    void shoulAutoAssign_should_return_true_if_property_autoAssign_is_set_to_true() {
        //given
        filter.setInputParameters(Collections.singletonMap(AUTO_ASSIGN_KEY, true));

        //when
        boolean autoAssign = filter.shouldAutoAssignTaskIfSingleResult();

        //then
        assertThat(autoAssign).isTrue();
    }


    @Test
    void shouldUsePartialMatch_should_return_false_if_key_userPartialMatch_is_not_set() {
        //when
        Boolean usePartialMatch = filter.shouldUsePartialMatch();

        //then
        assertThat(usePartialMatch).isFalse();
    }

    @Test
    void shouldUsePartialMatch_should_return_true_if_key_userPartialMatch_is_set_to_true() {
        filter.setInputParameters(Collections.singletonMap(USE_PARTIAL_MATCH_KEY, true));
        //when
        Boolean usePartialMatch = filter.shouldUsePartialMatch();

        //then
        assertThat(usePartialMatch).isTrue();
    }

    @Test
    void shouldUsePartialMatch_should_return_false_if_key_userPartialMatch_is_set_to_false() {
        filter.setInputParameters(Collections.singletonMap(USE_PARTIAL_MATCH_KEY, false));
        //when
        Boolean usePartialMatch = filter.shouldUsePartialMatch();

        //then
        assertThat(usePartialMatch).isFalse();
    }

    @Test
    void shouldUsePartialMatch_should_return_false_if_key_userPartialMatch_is_set_to_null() {
        filter.setInputParameters(Collections.singletonMap(USE_PARTIAL_MATCH_KEY, null));
        //when
        Boolean usePartialMatch = filter.shouldUsePartialMatch();

        //then
        assertThat(usePartialMatch).isFalse();
    }

}

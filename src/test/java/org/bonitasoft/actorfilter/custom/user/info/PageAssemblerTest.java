package org.bonitasoft.actorfilter.custom.user.info;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class PageAssemblerTest {

    @Mock(lenient = true)
    private PageRetriever<Long> retriever;

    @InjectMocks
    private PageAssembler<Long> assembler;

    @SuppressWarnings("unchecked")
    @Test
    void getAllElementsShouldIterateAllPages() {
        //given
        when(retriever.nextPage()).thenReturn(Arrays.asList(1L, 2L, 3L), Arrays.asList(4L, 5L));
        when(retriever.getMaxPageSize()).thenReturn(3);

        //when
        List<Long> elements = assembler.getAllElements();

        //then
        assertThat(elements).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

}

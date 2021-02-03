package dariomorgrane.alfa;

import dariomorgrane.alfa.model.Operation;
import dariomorgrane.alfa.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AlfaWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationService mockService;

    private Operation operation;
    private final String gifLink = "gif link";
    private final String operationControllerUrlExample = "/operate?code=USD";

    void setupMockServiceResponse() {
        operation = new Operation();
        operation.setGifUrl(gifLink);
    }

    @BeforeEach
    void setupMockService() {
        setupMockServiceResponse();
        Mockito.when(mockService.setupInitialData(anyString()))
                .thenReturn(operation);
        Mockito.doNothing()
                .when(mockService)
                .setupRatesData(any(Operation.class));
        Mockito.doNothing()
                .when(mockService)
                .setupGifData(any(Operation.class));
    }

    @Test
    public void shouldReturnStatusFoundAndExpectedLocation() throws Exception {
        mockMvc.perform(get(operationControllerUrlExample))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(header().string("Location", gifLink));
    }

}

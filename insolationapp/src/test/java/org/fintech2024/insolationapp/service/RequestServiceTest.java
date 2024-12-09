package org.fintech2024.insolationapp.service;

import org.fintech2024.insolationapp.model.*;
import org.fintech2024.insolationapp.repository.RequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @InjectMocks
    private RequestService requestService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ResponseService responseService;

    @Mock
    private MlApiService mlApiService;

    @Test
    void testGetRequestsByUser() {
        // Arrange
        User user = new User();
        List<Request> mockRequests = List.of(new Request(), new Request());
        Mockito.when(requestRepository.findAllRequestsByUser(user)).thenReturn(mockRequests);

        // Act
        List<Request> result = requestService.getRequestsByUser(user);

        // Assert
        assertEquals(mockRequests.size(), result.size());
        Mockito.verify(requestRepository).findAllRequestsByUser(user);
    }

    @Test
    void testCreateRequest() {
        // Arrange
        User user = new User();
        RequestContent content = new RequestContent();
        String requestName = "Test Request";

        Request mockRequest = new Request();
        Mockito.when(requestRepository.save(Mockito.any(Request.class))).thenReturn(mockRequest);

        // Act
        Request result = requestService.createRequest(requestName, content, user);

        // Assert
        assertNotNull(result);
        Mockito.verify(requestRepository).save(Mockito.any(Request.class));
    }

    @Test
    void testProcessRequest_Success() throws Exception {
        // Arrange
        Request request = new Request();
        request.setId(1L);
        request.setContent(new RequestContent());
        request.setStatus(RequestStatus.NEW);

        ExternalApiResponse apiResponse = new ExternalApiResponse();
        apiResponse.setContent(new ResponseContent("Response Content"));
        apiResponse.setStatusCode(200);

        Mockito.when(mlApiService.sendRequest(request.getContent())).thenReturn(apiResponse);

        // Act
        requestService.processRequest(request);

        // Assert
        assertEquals(RequestStatus.COMPLETED, request.getStatus());
        Mockito.verify(responseService).saveResponse(Mockito.any(Response.class));
        Mockito.verify(requestRepository).save(request);
    }

    @Test
    void testProcessRequest_Error() throws Exception {
        // Arrange
        Request request = new Request();
        request.setId(1L);
        request.setContent(new RequestContent());
        request.setStatus(RequestStatus.NEW);

        ExternalApiResponse apiResponse = new ExternalApiResponse();
        apiResponse.setContent(new ResponseContent("Error Response"));
        apiResponse.setStatusCode(500);

        Mockito.when(mlApiService.sendRequest(request.getContent())).thenReturn(apiResponse);

        // Act
        requestService.processRequest(request);

        // Assert
        assertEquals(RequestStatus.ERROR, request.getStatus());
        Mockito.verify(responseService).saveResponse(Mockito.any(Response.class));
        Mockito.verify(requestRepository).save(request);
    }

    @Test
    void testProcessRequest_Failure() {
        // Arrange
        Request request = new Request();
        request.setId(1L);
        request.setContent(new RequestContent());
        request.setStatus(RequestStatus.NEW);

        Mockito.when(mlApiService.sendRequest(request.getContent())).thenThrow(new RuntimeException("API Error"));

        // Act
        requestService.processRequest(request);

        // Assert
        assertEquals(RequestStatus.FAILED, request.getStatus());
        Mockito.verify(requestRepository).save(request);
    }
}
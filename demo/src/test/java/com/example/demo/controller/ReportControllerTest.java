package com.example.demo.controller;

import com.example.demo.controller.ReportController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private ReportController reportController;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    public void testApproveReport() {
        mockServer.expect(MockRestRequestMatchers.requestTo("YOUR_HAZARDDATA_URL/api/hazarddata/add"))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                .andExpect(MockRestRequestMatchers.content().contentType(MediaType.MULTIPART_FORM_DATA))
                .andRespond(MockRestResponseCreators.withSuccess("Report added successfully!", MediaType.TEXT_PLAIN));

        ResponseEntity<String> response = reportController.approveReport(1L); // 예제 ID 사용
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Report approved and added to hazard data!");
    }
}

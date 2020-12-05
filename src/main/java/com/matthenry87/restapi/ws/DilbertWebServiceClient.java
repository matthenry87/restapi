package com.matthenry87.restapi.ws;

import com.matthenry87.restapi.dilbert.DailyDilbert;
import com.matthenry87.restapi.dilbert.DailyDilbertResponse;
import lombok.SneakyThrows;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;

public class DilbertWebServiceClient extends WebServiceGatewaySupport {

    @SneakyThrows
    DailyDilbertResponse dailyDilbert() {

        var today = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());

        var dailyDilbert = new DailyDilbert();
        dailyDilbert.setADate(today);

        return (DailyDilbertResponse) getWebServiceTemplate()
                .marshalSendAndReceive(dailyDilbert, new SoapActionCallback("http://gcomputer.net/webservices/DailyDilbert"));
    }

}

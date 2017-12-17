package com.springCamel.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.springCamel.model.GeocodeResponseFromJson;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class CamelRestIntegration extends RouteBuilder {

    @Value("${server.port}")
    private String serverPort;

    @Value("${springCamel.api.path}")
    private String contextPath;

    private final String API_URI = "https://maps.googleapis.com/maps/api/geocode";
    private final String API_KEY = "AIzaSyCA5BUWF7yszTu451QvZhBPf8LXxUhF6y4";
    private final String API_CONSUME_FORMAT = "json"; //xml

    @Override
    public void configure() throws Exception {

        restConfiguration()
                .contextPath(contextPath)
                .port(serverPort).
                enableCORS(true).
                apiContextPath("/api-doc")
                .apiProperty("api.title", "Test REST API")
                .apiProperty("api.version", "v1")
                .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json_xml);

        rest("/api")
                .id("api-route")
                .get("/location/{address}")
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .bindingMode(RestBindingMode.json_xml)
                .outType(GeocodeResponseFromJson.class)
                .to("direct:locationService");


        from("direct:locationService")
                .routeId("location-route")
                .removeHeaders("CamelHttp*")
                .streamCaching()
                .tracing()
                .log("address to search for ==> ${header.address}")
                .setHeader(Exchange.HTTP_URI, simple(API_URI + "/" + API_CONSUME_FORMAT + "?address=" + "${header.address}" + "&key=" + API_KEY))
                .to("https://maps.googleapis.com")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {

                        //To retrieve in JSON format
                        GeocodeResponseFromJson geocodeResponse = new ObjectMapper().readValue(exchange.getIn().getBody(String.class), GeocodeResponseFromJson.class);
                        exchange.getIn().setBody(geocodeResponse);

                        // to retrieve in XML format
//                        JAXBContext jaxbContext = JAXBContext.newInstance(GeocodeResponse.class);
//                        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//                        StringReader reader = new StringReader(exchange.getIn().getBody(String.class));
//                        GeocodeResponse geocodeResponse = (GeocodeResponse) jaxbUnmarshaller.unmarshal(reader);

                        // /  retrieve in XML format then transform to json (not complete) TODO fix the  issue here
//                        XmlMapper xmlMapper = new XmlMapper();
//                        byte[] outBytes = exchange.getIn().getBody(String.class).getBytes();
//                        JsonNode node = xmlMapper.readTree(outBytes);
//                        ObjectMapper jsonMapper = new ObjectMapper();
//                        String json = jsonMapper.writeValueAsString(node);
//                        GeocodeResponseFromJson geocodeResponse = new ObjectMapper().readValue(json, GeocodeResponseFromJson.class);
//                        exchange.getIn().setBody(geocodeResponse);
                    }
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200)).onException(Exception.class)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404)).handled(true);


    }

}

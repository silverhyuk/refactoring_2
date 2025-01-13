package com.david.chapter01;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * App
 */
public class App {

    public static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ClassLoader classLoader = App.class.getClassLoader();

            // plays.json 파일 로드
            InputStream playsStream = classLoader.getResourceAsStream("chapter01/plays.json");
            Map<String, Play> plays = objectMapper.readValue(playsStream, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Play.class));

            // invoices.json 파일 로드
            InputStream invoicesStream = classLoader.getResourceAsStream("chapter01/invoices.json");
            List<Invoice> invoices = objectMapper.readValue(invoicesStream, new TypeReference<>(){});

            // 각 Invoice 처리
            for (Invoice invoice : invoices) {
                String result = Statement.statement(invoice, plays);
                logger.info("\n{}", result);
            }
        } catch (IOException e) {
            logger.error("Error", e);
        }
    }

}

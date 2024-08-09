package org.juancatalan.edgepaircoverageplugin.DTO.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.juancatalan.edgepaircoverageplugin.DTO.MethodReportDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MethodReportJsonParser {
    public static List<MethodReportDTO> parseJSON(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] jsonData = Files.readAllBytes(Paths.get(filePath));
        return objectMapper.readValue(jsonData, objectMapper.getTypeFactory().constructCollectionType(List.class, MethodReportDTO.class));
    }
}

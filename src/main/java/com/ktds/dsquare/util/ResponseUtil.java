package com.ktds.dsquare.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class ResponseUtil {

    public static void writeJsonValue(HttpServletResponse response, Object value) throws IOException {
        if (response.isCommitted())
            return;

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(value));
    }

}

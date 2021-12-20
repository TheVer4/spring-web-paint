package com.example.springwebpaint;

import com.example.springwebpaint.domain.DrawingType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("test_user")
@TestPropertySource("/application-test.properties")
public class DrawingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(value = {"/create-user-before.sql", "/create-boards-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/remove-all-drawings.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/create-boards-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createDrawingTypeLine() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode nodes = new ObjectNode(mapper.getNodeFactory());
        nodes.put("drawing_type", DrawingType.LINE.toString());
        nodes.put("color", "#ABCDEF");
        nodes.put("coords", mapper.createArrayNode().add(0).add(0).add(150).add(150));
        String jsonString = mapper.writeValueAsString(nodes);
        MockMultipartHttpServletRequestBuilder action = (MockMultipartHttpServletRequestBuilder) multipart("/drawing/1")
                .param("json", jsonString)
                .with(csrf());
        this.mockMvc.perform(action)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(content().string(containsString("\"drawing_type\":\"LINE\",\"coords\":\"[0,0,150,150]\",\"color\":\"#ABCDEF\"")));
    }

    @Test
    @Sql(value = {"/create-user-before.sql", "/create-boards-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/remove-all-drawings.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/create-boards-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createDrawingTypeText() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode nodes = new ObjectNode(mapper.getNodeFactory());
        nodes.put("drawing_type", DrawingType.TEXT.toString());
        nodes.put("color", "#ABCDEF");
        nodes.put("text", "Some text");
        nodes.put("coords", mapper.createArrayNode().add(250).add(150));
        String jsonString = mapper.writeValueAsString(nodes);
        MockMultipartHttpServletRequestBuilder action = (MockMultipartHttpServletRequestBuilder) multipart("/drawing/1")
                .param("json", jsonString)
                .with(csrf());
        this.mockMvc.perform(action)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(content().string(containsString("\"drawing_type\":\"TEXT\",\"coords\":\"[250,150]\",\"color\":\"#ABCDEF\",\"text\":\"Some text\"")));
    }

    @Test
    @Sql(value = {"/create-user-before.sql", "/create-boards-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/remove-all-drawings.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/create-boards-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void fetchAllDrawings() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode nodesText = new ObjectNode(mapper.getNodeFactory());
        nodesText.put("drawing_type", DrawingType.TEXT.toString());
        nodesText.put("color", "#ABCDEF");
        nodesText.put("text", "Some text");
        nodesText.put("coords", mapper.createArrayNode().add(250).add(150));
        String jsonStringText = mapper.writeValueAsString(nodesText);
        MockMultipartHttpServletRequestBuilder actionCreateText = (MockMultipartHttpServletRequestBuilder) multipart("/drawing/1")
                .param("json", jsonStringText)
                .with(csrf());

        ObjectNode nodesLine = new ObjectNode(mapper.getNodeFactory());
        nodesLine.put("drawing_type", DrawingType.LINE.toString());
        nodesLine.put("color", "#ABCDEF");
        nodesLine.put("coords", mapper.createArrayNode().add(0).add(0).add(150).add(150));
        String jsonStringLine = mapper.writeValueAsString(nodesLine);
        MockMultipartHttpServletRequestBuilder actionCreateLine = (MockMultipartHttpServletRequestBuilder) multipart("/drawing/1")
                .param("json", jsonStringLine)
                .with(csrf());

        this.mockMvc.perform(actionCreateLine)
                .andDo(print())
                .andExpect(authenticated())
                .andDo(
                        resultLine -> {
                            String drawingJsonLine = resultLine.getResponse().getContentAsString();
                            this.mockMvc.perform(actionCreateText)
                                    .andDo(print())
                                    .andExpect(authenticated())
                                    .andDo(
                                            resultText -> {
                                                String drawingJsonText = resultText.getResponse().getContentAsString();
                                                this.mockMvc.perform(get("/drawing/1"))
                                                        .andDo(print())
                                                        .andExpect(authenticated())
                                                        .andExpect(content().string(containsString(drawingJsonLine)))
                                                        .andExpect(content().string(containsString(drawingJsonText)));
                                            }
                                    );
                        }
                );
    }

    @Test
    @Sql(value = {"/create-user-before.sql", "/create-boards-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/remove-all-drawings.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/create-boards-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void fetchSpecificDrawing() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode nodesText = new ObjectNode(mapper.getNodeFactory());
        nodesText.put("drawing_type", DrawingType.TEXT.toString());
        nodesText.put("color", "#ABCDEF");
        nodesText.put("text", "Some text");
        nodesText.put("coords", mapper.createArrayNode().add(250).add(150));
        String jsonStringText = mapper.writeValueAsString(nodesText);
        MockMultipartHttpServletRequestBuilder actionCreateText = (MockMultipartHttpServletRequestBuilder) multipart("/drawing/1")
                .param("json", jsonStringText)
                .with(csrf());
                this.mockMvc.perform(actionCreateText)
                        .andDo(print())
                        .andExpect(authenticated())
                        .andDo(
                                resultText -> {
                                    String drawingJsonText = resultText.getResponse().getContentAsString();
                                    int drawingId = getIdFromDrawingJson(drawingJsonText);
                                    this.mockMvc.perform(get("/drawing/1/" + drawingId))
                                            .andDo(print())
                                            .andExpect(authenticated())
                                            .andExpect(content().string(containsString(drawingJsonText)));
                                }
                        );

    }

    @Test
    @Sql(value = {"/create-user-before.sql", "/create-boards-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/remove-all-drawings.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/create-boards-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteSpecificDrawing() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode nodesText = new ObjectNode(mapper.getNodeFactory());
        nodesText.put("drawing_type", DrawingType.TEXT.toString());
        nodesText.put("color", "#ABCDEF");
        nodesText.put("text", "Some text");
        nodesText.put("coords", mapper.createArrayNode().add(250).add(150));
        String jsonStringText = mapper.writeValueAsString(nodesText);
        MockMultipartHttpServletRequestBuilder actionCreateText = (MockMultipartHttpServletRequestBuilder) multipart("/drawing/1")
                .param("json", jsonStringText)
                .with(csrf());
        this.mockMvc.perform(actionCreateText)
                .andDo(print())
                .andExpect(authenticated())
                .andDo(
                        resultText -> {
                            String drawingJsonText = resultText.getResponse().getContentAsString();
                            int drawingId = getIdFromDrawingJson(drawingJsonText);
                            this.mockMvc.perform(delete("/drawing/1/" + drawingId).with(csrf()))
                                    .andDo(print())
                                    .andExpect(authenticated())
                                    .andExpect(status().isOk())
                                    .andExpect(content().string("OK"));
                        }
                );

    }

    @Test
    @Sql(value = {"/create-user-before.sql", "/create-boards-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/remove-all-drawings.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/create-boards-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteAllDrawings() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode nodesText = new ObjectNode(mapper.getNodeFactory());
        nodesText.put("drawing_type", DrawingType.TEXT.toString());
        nodesText.put("color", "#ABCDEF");
        nodesText.put("text", "Some text");
        nodesText.put("coords", mapper.createArrayNode().add(250).add(150));
        String jsonStringText = mapper.writeValueAsString(nodesText);
        MockMultipartHttpServletRequestBuilder actionCreateText = (MockMultipartHttpServletRequestBuilder) multipart("/drawing/1")
                .param("json", jsonStringText)
                .with(csrf());

        ObjectNode nodesLine = new ObjectNode(mapper.getNodeFactory());
        nodesLine.put("drawing_type", DrawingType.LINE.toString());
        nodesLine.put("color", "#ABCDEF");
        nodesLine.put("coords", mapper.createArrayNode().add(0).add(0).add(150).add(150));
        String jsonStringLine = mapper.writeValueAsString(nodesLine);
        MockMultipartHttpServletRequestBuilder actionCreateLine = (MockMultipartHttpServletRequestBuilder) multipart("/drawing/1")
                .param("json", jsonStringLine)
                .with(csrf());

        this.mockMvc.perform(actionCreateLine)
                .andDo(print())
                .andExpect(authenticated())
                .andDo(
                        resultLine -> {
                            this.mockMvc.perform(actionCreateText)
                                    .andDo(print())
                                    .andExpect(authenticated())
                                    .andDo(
                                            resultText -> {
                                                this.mockMvc.perform(delete("/drawing/1/").with(csrf()))
                                                        .andDo(print())
                                                        .andExpect(authenticated())
                                                        .andExpect(status().isOk())
                                                        .andExpect(content().string("OK"));
                                            }
                                    );
                        }
                );
    }

    private int getIdFromDrawingJson(String drawingJson) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(drawingJson);
        return Integer.parseInt(jsonNode.get("id").asText());
    }
}

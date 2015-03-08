package cn.edu.uestc.acmicpc.web.oj.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.edu.uestc.acmicpc.db.condition.impl.ProblemCondition;
import cn.edu.uestc.acmicpc.db.dto.impl.problem.ProblemDto;
import cn.edu.uestc.acmicpc.db.dto.impl.user.UserDto;
import cn.edu.uestc.acmicpc.testing.ControllerTest;
import cn.edu.uestc.acmicpc.util.enums.AuthenticationType;
import cn.edu.uestc.acmicpc.util.enums.ProblemType;
import cn.edu.uestc.acmicpc.web.oj.controller.problem.ProblemController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;

import java.util.UUID;

/**
 * Mock test for {@link ProblemController}.
 */
public class ProblemControllerTest extends ControllerTest {

  @Autowired
  private ProblemController problemController;

  @Override
  @BeforeMethod
  public void init() {
    super.init();
    mockMvc = initControllers(problemController);
    session = new MockHttpSession(context.getServletContext(), UUID.randomUUID().toString());
  }

  @Test
  public void testVisitProblem() throws Exception {
    ProblemDto problemDto = ProblemDto.builder().setProblemId(1234).setTitle("test").
        setDescription("description test").setInput("1 2").setOutput("3").
        setSampleInput("SampleInput test").setSampleOutput("SampleOutput test").
        setHint("Hint Test").setSource("Source Test").setTimeLimit(1000).setMemoryLimit(65536)
        .setSolved(0).setTried(0).setIsSpj(Boolean.FALSE).setIsVisible(Boolean.TRUE)
        .setOutputLimit(8000).setJavaTimeLimit(3000).setJavaMemoryLimit(65536).setDataCount(1)
        .setDifficulty(1).setType(ProblemType.NORMAL).build();

    UserDto userDto = UserDto.builder().setType(AuthenticationType.NORMAL.ordinal()).build();
    Boolean visibleArg[] = {Boolean.TRUE, Boolean.FALSE};
    ProblemType problemTypeArg[] = {ProblemType.NORMAL, ProblemType.INTERNAL};
    Integer userTypeArg[] = {AuthenticationType.NORMAL.ordinal(),
        AuthenticationType.INTERNAL.ordinal(), AuthenticationType.ADMIN.ordinal()};
    for(int i = 0; i < visibleArg.length; i++) {
      for(int j = 0; j < problemTypeArg.length; j++) {
        for(int k = 0; k < userTypeArg.length; k++) {
          problemDto.setIsVisible(visibleArg[i]);
          problemDto.setType(problemTypeArg[j]);
          userDto.setType(userTypeArg[k]);
          when(problemService.getProblemDtoByProblemId(1234))
              .thenReturn(problemDto);
          Boolean permissionToVivit = Boolean.FALSE;
          if(userDto.getType() == AuthenticationType.ADMIN.ordinal()) {
            permissionToVivit = Boolean.TRUE;
          } else {
            if(problemDto.getIsVisible()) {
              if(problemDto.getType() == ProblemType.NORMAL) {
                permissionToVivit = Boolean.TRUE;
              }
              if(problemDto.getType() == ProblemType.INTERNAL &&
                  userDto.getType() == AuthenticationType.INTERNAL.ordinal()) {
                permissionToVivit = Boolean.TRUE;
              }
            }
          }
          if(permissionToVivit) {
            mockMvc.perform(post("/problem/data/{problemId}", 1234).
                sessionAttr("currentUser", userDto))
                .andExpect(request().sessionAttribute("currentUser", userDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is("success")))
                .andExpect(jsonPath("$.problem", notNullValue()))
                .andExpect(jsonPath("$.problem.dataCount", is(problemDto.getDataCount())))
                .andExpect(jsonPath("$.problem.description", is(problemDto.getDescription())))
                .andExpect(jsonPath("$.problem.difficulty", is(problemDto.getDifficulty())))
                .andExpect(jsonPath("$.problem.hint", is(problemDto.getHint())))
                .andExpect(jsonPath("$.problem.input", is(problemDto.getInput())))
                .andExpect(jsonPath("$.problem.isSpj", is(problemDto.getIsSpj())))
                .andExpect(jsonPath("$.problem.isVisible", is(problemDto.getIsVisible())))
                .andExpect(jsonPath("$.problem.javaMemoryLimit",
                    is(problemDto.getJavaMemoryLimit())))
                .andExpect(jsonPath("$.problem.javaTimeLimit", is(problemDto.getJavaTimeLimit())))
                .andExpect(jsonPath("$.problem.memoryLimit", is(problemDto.getMemoryLimit())))
                .andExpect(jsonPath("$.problem.output", is(problemDto.getOutput())))
                .andExpect(jsonPath("$.problem.outputLimit", is(problemDto.getOutputLimit())))
                .andExpect(jsonPath("$.problem.problemId", is(problemDto.getProblemId())))
                .andExpect(jsonPath("$.problem.sampleInput", is(problemDto.getSampleInput())))
                .andExpect(jsonPath("$.problem.sampleOutput", is(problemDto.getSampleOutput())))
                .andExpect(jsonPath("$.problem.solved", is(problemDto.getSolved())))
                .andExpect(jsonPath("$.problem.source", is(problemDto.getSource())))
                .andExpect(jsonPath("$.problem.timeLimit", is(problemDto.getTimeLimit())))
                .andExpect(jsonPath("$.problem.title", is(problemDto.getTitle())))
                .andExpect(jsonPath("$.problem.tried", is(problemDto.getTried())));
          } else {
            mockMvc.perform(post("/problem/data/{problemId}", 1234)
                .sessionAttr("currentUser", userDto))
                .andExpect(request().sessionAttribute("currentUser", userDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is("error")));
          }
        }
      }
    }
  }

  @Test
  public void testSearchProblem() throws Exception {
    UserDto userDto = UserDto.builder().setType(AuthenticationType.NORMAL.ordinal()).build();
    Integer userTypeArg[] = {AuthenticationType.NORMAL.ordinal(),
        AuthenticationType.INTERNAL.ordinal(), AuthenticationType.ADMIN.ordinal()};
    for(int i = 0; i < userTypeArg.length; i++) {
      userDto.setType(userTypeArg[i]);
      if(userDto.getType() == AuthenticationType.ADMIN.ordinal()) {
        mockMvc.perform(post("/problem/search/")
            .sessionAttr("currentUser", userDto)
            .sessionAttr("keyword", "a"))
            .andExpect(request().sessionAttribute("currentUser", userDto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", is("success")))
            .andExpect(jsonPath("$.pageInfo.totalItems.", is("4")));
        mockMvc.perform(post("/problem/search/").sessionAttr("currentUser", userDto)
            .sessionAttr("keyword", ""))
            .andExpect(request().sessionAttribute("currentUser", userDto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", is("success")))
            .andExpect(jsonPath("$.pageInfo.totalItems.",is("4")));
      } else if(userDto.getType() == AuthenticationType.NORMAL.ordinal()) {
        userDto.setType(AuthenticationType.NORMAL.ordinal());
        ProblemCondition problemCondition = new ProblemCondition();
        mockMvc.perform(post("/problem/search/")
            .contentType(APPLICATION_JSON_UTF8)
            .content(JSON.toJSONBytes(problemCondition))
            .sessionAttr("currentUser", userDto))
            .andExpect(request().sessionAttribute("currentUser", userDto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", is("success")))
            .andExpect(jsonPath("$.pageInfo.totalItems.", is("4")));
        mockMvc.perform(post("/problem/search/").sessionAttr("currentUser", userDto)
            .sessionAttr("keyword", ""))
            .andExpect(request().sessionAttribute("currentUser", userDto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", is("success")))
            .andExpect(jsonPath("$.pageInfo.totalItems.",is("1")));
      } else if(userDto.getType() == AuthenticationType.INTERNAL.ordinal()) {
        mockMvc.perform(post("/problem/search/").sessionAttr("currentUser", userDto)
            .sessionAttr("keyword", "a"))
            .andExpect(request().sessionAttribute("currentUser", userDto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", is("success")))
            .andExpect(jsonPath("$.pageInfo.totalItems.",is("4")));
        mockMvc.perform(post("/problem/search/").sessionAttr("currentUser", userDto)
            .sessionAttr("keyword", ""))
            .andExpect(request().sessionAttribute("currentUser", userDto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", is("success")))
            .andExpect(jsonPath("$.pageInfo.totalItems.",is("2")));
      }
    }
  }

}

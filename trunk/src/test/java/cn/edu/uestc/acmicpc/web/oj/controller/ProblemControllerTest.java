package cn.edu.uestc.acmicpc.web.oj.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cn.edu.uestc.acmicpc.db.condition.impl.ProblemCondition;
import cn.edu.uestc.acmicpc.db.dto.impl.problem.ProblemDto;
import cn.edu.uestc.acmicpc.db.dto.impl.problem.ProblemListDto;
import cn.edu.uestc.acmicpc.db.dto.impl.user.UserDto;
import cn.edu.uestc.acmicpc.testing.ControllerTest;
import cn.edu.uestc.acmicpc.util.enums.AuthenticationType;
import cn.edu.uestc.acmicpc.util.enums.ProblemType;
import cn.edu.uestc.acmicpc.web.dto.PageInfo;
import cn.edu.uestc.acmicpc.web.oj.controller.problem.ProblemController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Mock test for {@link ProblemController}.
 */
public class ProblemControllerTest extends ControllerTest{

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
        .setSolved(0).setTried(0).setIsSpj(false).setIsVisible(true)
        .setOutputLimit(8000).setJavaTimeLimit(3000).setJavaMemoryLimit(65536).setDataCount(1)
        .setDifficulty(1).setType(ProblemType.NORMAL).build();

    UserDto userDto = UserDto.builder().setType(AuthenticationType.NORMAL.ordinal()).build();
    Boolean visibleArg[] = {true, false};
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
          Boolean permissionToVivit = false;
          if(userDto.getType() == AuthenticationType.ADMIN.ordinal()) {
            permissionToVivit = true;
          } else {
            if(problemDto.getIsVisible()) {
              if(problemDto.getType() == ProblemType.NORMAL) {
                permissionToVivit = true;
              }
              if(problemDto.getType() == ProblemType.INTERNAL &&
                  userDto.getType() == AuthenticationType.INTERNAL.ordinal()) {
                permissionToVivit = true;
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
    ProblemCondition adminSearchCondition = new ProblemCondition();
    ProblemCondition internalSearchCondition = new ProblemCondition();
    ProblemCondition normalSearchCondition = new ProblemCondition();

    internalSearchCondition.isVisible = true;
    normalSearchCondition.isVisible = true;
    normalSearchCondition.type = ProblemType.NORMAL;
    when(problemService.count(any(ProblemCondition.class))).thenReturn(Long.valueOf(5));

    List<ProblemListDto> adminResult = new LinkedList<>();
    for (int i = 0; i < 5; i++) {
      adminResult.add(ProblemListDto.builder().setIsVisible(false).setProblemId(i).build());
    }
    when(problemService.getProblemListDtoList(eq(adminSearchCondition), any(PageInfo.class)))
        .thenReturn(adminResult);

    List<ProblemListDto> internalResult = new LinkedList<>();
    for (int i = 0; i < 5; i++) {
      internalResult.add(ProblemListDto.builder().setIsVisible(true).setProblemId(i).build());
    }
    when(problemService.getProblemListDtoList(eq(internalSearchCondition), any(PageInfo.class)))
        .thenReturn(internalResult);

    List<ProblemListDto> normalResult = new LinkedList<>();
    for (int i = 0; i < 5; i++) {
      normalResult.add(ProblemListDto.builder().setIsVisible(true).setType(ProblemType.NORMAL).setProblemId(i).build());
    }
   when(problemService.getProblemListDtoList(eq(normalSearchCondition), any(PageInfo.class)))
         .thenReturn(normalResult);

    List<Integer> triedProblems = new ArrayList<>();
    List<Integer> acceptedProblems = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      triedProblems.add(Integer.valueOf(i));
      if(i <= 2) {
        acceptedProblems.add(Integer.valueOf(i));
      }
    }
    when(statusService.findAllProblemIdsThatUserTried(any(Integer.class), anyBoolean())).thenReturn(triedProblems);
    when(statusService.findAllProblemIdsThatUserSolved(any(Integer.class), anyBoolean())).thenReturn(acceptedProblems);
    //different types of user to search problem
    for(int i = 0; i < userTypeArg.length; i++) {
      userDto.setType(userTypeArg[i]);
      if(userDto.getType() == AuthenticationType.ADMIN.ordinal()) {
        userDto.setType(AuthenticationType.ADMIN.ordinal());
        mockMvc.perform(post("/problem/search/")
            .contentType(APPLICATION_JSON_UTF8)
            .content(JSON.toJSONBytes(new ProblemCondition()))
            .sessionAttr("currentUser", userDto))
            .andExpect(request().sessionAttribute("currentUser", userDto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", is("success")))
            .andExpect(jsonPath("$.pageInfo.totalItems.", is(5)))
            .andExpect(jsonPath("$.list", hasSize(5)))
            .andExpect(jsonPath("$.list[0].problemId", is(0)))
            .andExpect(jsonPath("$.list[1].problemId", is(1)))
            .andExpect(jsonPath("$.list[2].problemId", is(2)))
            .andExpect(jsonPath("$.list[3].problemId", is(3)))
            .andExpect(jsonPath("$.list[4].problemId", is(4)))
            .andExpect(jsonPath("$.list[0].isVisible", is(false)))
            .andExpect(jsonPath("$.list[1].isVisible", is(false)))
            .andExpect(jsonPath("$.list[2].isVisible", is(false)))
            .andExpect(jsonPath("$.list[3].isVisible", is(false)))
            .andExpect(jsonPath("$.list[4].isVisible", is(false)));
            verify(problemService).getProblemListDtoList(eq(adminSearchCondition), any(PageInfo.class));
      } else if(userDto.getType() == AuthenticationType.NORMAL.ordinal()) {
        userDto.setType(AuthenticationType.NORMAL.ordinal());
        mockMvc.perform(post("/problem/search/")
            .contentType(APPLICATION_JSON_UTF8)
            .content(JSON.toJSONBytes(new ProblemCondition()))
            .sessionAttr("currentUser", userDto))
            .andExpect(request().sessionAttribute("currentUser", userDto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", is("success")))
            .andExpect(jsonPath("$.pageInfo.totalItems.", is(5)))
            .andExpect(jsonPath("$.list", hasSize(5)))
            .andExpect(jsonPath("$.list[0].problemId", is(0)))
            .andExpect(jsonPath("$.list[1].problemId", is(1)))
            .andExpect(jsonPath("$.list[2].problemId", is(2)))
            .andExpect(jsonPath("$.list[3].problemId", is(3)))
            .andExpect(jsonPath("$.list[4].problemId", is(4)))
            .andExpect(jsonPath("$.list[0].isVisible", is(true)))
            .andExpect(jsonPath("$.list[1].isVisible", is(true)))
            .andExpect(jsonPath("$.list[2].isVisible", is(true)))
            .andExpect(jsonPath("$.list[3].isVisible", is(true)))
            .andExpect(jsonPath("$.list[4].isVisible", is(true)));
      } else if(userDto.getType() == AuthenticationType.INTERNAL.ordinal()) {
        userDto.setType(AuthenticationType.INTERNAL.ordinal());
        mockMvc.perform(post("/problem/search/")
            .contentType(APPLICATION_JSON_UTF8)
            .content(JSON.toJSONBytes(new ProblemCondition()))
            .sessionAttr("currentUser", userDto))
            .andExpect(request().sessionAttribute("currentUser", userDto))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", is("success")))
            .andExpect(jsonPath("$.pageInfo.totalItems.", is(5)))
            .andExpect(jsonPath("$.list[0].problemId", is(0)))
            .andExpect(jsonPath("$.list[1].problemId", is(1)))
            .andExpect(jsonPath("$.list[2].problemId", is(2)))
            .andExpect(jsonPath("$.list[3].problemId", is(3)))
            .andExpect(jsonPath("$.list[4].problemId", is(4)))
            .andExpect(jsonPath("$.list[0].isVisible", is(true)))
            .andExpect(jsonPath("$.list[1].isVisible", is(true)))
            .andExpect(jsonPath("$.list[2].isVisible", is(true)))
            .andExpect(jsonPath("$.list[3].isVisible", is(true)))
            .andExpect(jsonPath("$.list[4].isVisible", is(true)));
      }
    }
  }
}

package com.sist.ehr;

import static com.sist.ehr.member.service.imple.UserServiceImple.MIN_LOGINCOUNT_FOR_SILVER;
import static com.sist.ehr.member.service.imple.UserServiceImple.MIN_RECCOMENDCOUNT_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import com.sist.ehr.cmn.DTO;
import com.sist.ehr.cmn.SearchVO;
import com.sist.ehr.member.service.Level;
import com.sist.ehr.member.service.UserService;
import com.sist.ehr.member.service.UserVO;
import com.sist.ehr.member.service.imple.UserDaoImple;



@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		                           "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
                                   })
public class TestControllerWeb {

	private final Logger  LOG = LoggerFactory.getLogger(TestControllerWeb.class);
	
	@Autowired
	WebApplicationContext  webApplicationContext;
	
	private List<UserVO> users;
	
	@Autowired
	UserService  userService;
	
	//브라우저 대신 Mock
	private MockMvc mockMvc;
	
	
	@Before
	public void setUp() {
		LOG.debug("*********************");
		LOG.debug("=setUp()=");
		LOG.debug("*********************");
		users = Arrays.asList(
					 new UserVO("j01_124","이상무1","1234",Level.BASIC,MIN_LOGINCOUNT_FOR_SILVER-1,0,"jamesol@paran.com","")
					,new UserVO("j02_124","이상무2","1234",Level.BASIC,MIN_LOGINCOUNT_FOR_SILVER,0,"jamesol@paran.com","")					
					,new UserVO("j03_124","이상무3","1234",Level.SILVER,MIN_LOGINCOUNT_FOR_SILVER+10,MIN_RECCOMENDCOUNT_FOR_GOLD-1,"jamesol@paran.com","")
					,new UserVO("j04_124","이상무4","1234",Level.SILVER,MIN_LOGINCOUNT_FOR_SILVER+10,MIN_RECCOMENDCOUNT_FOR_GOLD,"jamesol@paran.com","")
					,new UserVO("j05_124","이상무5","1234",Level.GOLD,MIN_LOGINCOUNT_FOR_SILVER+33,MIN_RECCOMENDCOUNT_FOR_GOLD+3,"jamesol@paran.com","")
				);
				
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		LOG.debug("=====================");
		LOG.debug("=webApplicationContext="+webApplicationContext);
		LOG.debug("=mockMvc="+mockMvc);
		LOG.debug("=userService="+userService);
		LOG.debug("=====================");		

	}
	
	//단건조회
	@Test
	public void doSelectOne() throws Exception {
		//url+param
		MockHttpServletRequestBuilder  createMesage 
		           = MockMvcRequestBuilders.post("/member/do_select_one.do")
		             .param("u_id", users.get(0).getU_id());		
		
		//MediaType.APPLICATION_JSON_UTF8 ==application/json;charset=UTF-8
		ResultActions  resultActions = mockMvc.perform(createMesage)
			.andExpect(status().is2xxSuccessful())	
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
		    .andExpect(MockMvcResultMatchers.jsonPath("$.u_id", is(users.get(0).getU_id())))
		    .andExpect(MockMvcResultMatchers.jsonPath("$.name", is(users.get(0).getName())))
		    ;
				
		String result = resultActions.andDo(print())
				.andReturn()
				.getResponse().getContentAsString();
		LOG.debug("=====================");
		LOG.debug("=result="+result);
		LOG.debug("=====================");  		
		
		
	}
	
	@Test
	@Ignore
	public void doDelete() throws Exception {
		//url+param
		MockHttpServletRequestBuilder  createMesage 
		           = MockMvcRequestBuilders.post ("/member/do_delete.do")
		             .param("u_id", users.get(0).getU_id());
		//MediaType.APPLICATION_JSON_UTF8 ==application/json;charset=UTF-8
		ResultActions  resultActions = mockMvc.perform(createMesage)
			.andExpect(status().is2xxSuccessful())	
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
		    .andExpect(MockMvcResultMatchers.jsonPath("$.msgId", is("1")));
		
		String result = resultActions.andDo(print())
				.andReturn()
				.getResponse().getContentAsString();
		LOG.debug("=====================");
		LOG.debug("=result="+result);
		LOG.debug("=====================");  		
		
	}
	
	@Test 
	@Ignore
	public void add() throws Exception {
		MockHttpServletRequestBuilder createMessage = 
				MockMvcRequestBuilders.post("/member/add.do")
				.param("u_id", users.get(0).getU_id())
				.param("name", users.get(0).getName())
				.param("passwd", users.get(0).getPasswd())
				.param("level", users.get(0).getLevel()+"")
				.param("login", users.get(0).getLogin()+"")
				.param("recommend", users.get(0).getRecommend()+"")
				.param("email", users.get(0).getEmail())
				;
		
		ResultActions  resultActions =mockMvc.perform(createMessage)
				       .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
				       .andExpect(MockMvcResultMatchers.jsonPath("$.msgId", is("1")))
				;
		String result = resultActions.andDo(print())
						.andReturn()
						.getResponse().getContentAsString();
		LOG.debug("=====================");
		LOG.debug("=result="+result);
		LOG.debug("=====================");  
	}
	
//	static imports: MockMvcRequestBuilders.*, MockMvcResultMatchers.*
//
//	 mockMvc.perform(get("/person/1"))
//	   .andExpect(status().isOk())
//	   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//	   .andExpect(jsonPath("$.person.name").value("Jason"));
//
//	 mockMvc.perform(post("/form"))
//	   .andExpect(status().isOk())
//	   .andExpect(redirectedUrl("/person/1"))
//	   .andExpect(model().size(1))
//	   .andExpect(model().attributeExists("person"))
//	   .andExpect(flash().attributeCount(1))
//	   .andExpect(flash().attribute("message", "success!"));
//	 
	
	
	
	
	@Test
	public void test() {
		LOG.debug("=====================");
		LOG.debug("=test()=");
		LOG.debug("=====================");
		
		
		LOG.debug("=====================");
		LOG.debug("=userService="+userService);
		LOG.debug("=====================");		
		
		assertThat(1, is(1));
		
	}
	

}

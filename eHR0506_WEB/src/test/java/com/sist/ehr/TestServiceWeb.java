package com.sist.ehr;

import static com.sist.ehr.member.service.imple.UserServiceImple.MIN_LOGINCOUNT_FOR_SILVER;
import static com.sist.ehr.member.service.imple.UserServiceImple.MIN_RECCOMENDCOUNT_FOR_GOLD;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

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
public class TestServiceWeb {

	private final Logger  LOG = LoggerFactory.getLogger(TestServiceWeb.class);
	
	@Autowired
	WebApplicationContext  webApplicationContext;
	
	private List<UserVO> users;
	
	@Autowired
	UserService  userService;
	

	
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
				


	}
	
	
	@Test
	public void add() {
		//-----------------------------------
		//1. 모두삭제
		//2. Level null,Level.GOLD
		//3. 등록
		//4. Level null == BASIC, GOLD ->GOLD
		//-----------------------------------		
		//1.
		for(UserVO vo :users) {
			userService.doDelete(vo);
		}
		
		//2.
		UserVO   userExistLevel  =  users.get(4);
		UserVO   userNoneLevel   =  users.get(0);
		userNoneLevel.setLevel(null);//Enum 값 Null처리
		
		this.userService.add(userExistLevel);
		this.userService.add(userNoneLevel);
		
		//3. 
		UserVO userExistLevelRead = (UserVO) userService.doSelectOne(userExistLevel);
		UserVO userNoneLevelRead = (UserVO) userService.doSelectOne(userNoneLevel);
		
		//4.
		assertThat(userNoneLevelRead.getLevel(), is(Level.BASIC));
		assertThat(userExistLevelRead.getLevel(), is(userExistLevel.getLevel()));
		
	}
	
	  /**
	   * 사용자 등업
	   * 1. 전체 사용자를 읽어 들인다.
	   * 2. 등업 대상자를 선별한다.
	   *    2.1. BASIC사용자 : 로그인CNT 50이상(=포함)이면 : SILVER
	   *    2.2. SILVER사용자: 추천CNT 30이상(=포함): GOLD
	   *    2.3. GOLD대상 아님.
	   * 3. 등업한다. 
	 * @throws Exception 
	   */
	@Test
	public void upgradeLevels() throws Exception {
		//------------------------------------------ 
		//1. 모두 삭제
		//2. 모두등록
		//3. 등업		
		//------------------------------------------
		
		//1.
		for(UserVO vo :users) {
			userService.doDelete(vo);
		}
		
		//2.
		for(UserVO vo :users) {
			userService.doInsert(vo);
		}		
		
		SearchVO searchUser=new SearchVO();
		searchUser.setPageSize(10);
		searchUser.setPageNum(1);
		   
		searchUser.setSearchDiv("10");
		searchUser.setSearchWord("_124");
		List<UserVO> userList = (List<UserVO>) userService.doRetrieve(searchUser);
		assertThat(userList.size(), is(5));
		
		
		UserVO user=new UserVO();
		user.setU_id("_124");
		//3.등업
		try {
			this.userService.upgradeLevels(user);
		}catch(Exception e) {
			LOG.debug("-----------------");
			LOG.debug("-Exception--"+e.getMessage());
			LOG.debug("-----------------");
		}
		//4.비교
//		checkLevel(this.users.get(1),Level.BASIC);
		
//		checkLevel(this.users.get(0),Level.BASIC);
//		checkLevel(this.users.get(1),Level.SILVER);
//		checkLevel(this.users.get(2),Level.SILVER);
//		checkLevel(this.users.get(3),Level.GOLD);
	}	
	
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

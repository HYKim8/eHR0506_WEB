package com.sist.ehr;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.sql.SQLException;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.sist.ehr.cmn.SearchVO;
import com.sist.ehr.member.service.Level;
import com.sist.ehr.member.service.UserVO;
import com.sist.ehr.member.service.imple.UserDaoImple;



@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		                           "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
                                   })
public class TestDaoWeb {

	private final Logger  LOG = LoggerFactory.getLogger(TestDaoWeb.class);
	
	@Autowired
	WebApplicationContext  webApplicationContext;

	UserVO user01;
	UserVO user02;
	UserVO user03;
	
	UserVO user04;	
	
	@Autowired
	UserDaoImple  dao;
	
	
	@Test
	public void addAndGet()throws SQLException {
		LOG.debug("=====================");
		LOG.debug("=addAndGet()=");
		LOG.debug("=====================");

		
		LOG.debug("**********"
				+ "********************");
		LOG.debug("=싱글톤  =");
		LOG.debug("=dao  ="+dao);
		LOG.debug("******************************");
	    

		dao.doDelete(user01);
		dao.doDelete(user02);
		dao.doDelete(user03);
		
		assertThat(dao.count(user04), is(0));
		
		
		int flag = dao.doInsert(user01);
		flag += dao.doInsert(user02);
		flag += dao.doInsert(user03);
		
		assertThat(flag, is(3));
		assertThat(dao.count(user04), is(3));
		
		UserVO vsUser01= (UserVO) dao.doSelectOne(user01);
		UserVO vsUser02= (UserVO) dao.doSelectOne(user02);
		UserVO vsUser03= (UserVO) dao.doSelectOne(user03);
		
		assertTrue(vsUser01.equals(user01));
		assertTrue(vsUser02.equals(user02));
		assertTrue(vsUser03.equals(user03));
		
	}
	

	
	@Before
	public void setUp() throws Exception {
		LOG.debug("^^^^^^^^^^^");
		LOG.debug("^WebApplicationContext^"+webApplicationContext);
		LOG.debug("^^^^^^^^^^^");
		user01=new UserVO("j01_124","이상무","1234",Level.BASIC,1,0,"jamesol@paran.com","");
		user02=new UserVO("j02_124","이상무","1234",Level.SILVER ,51,10,"jamesol@paran.com","");
		user03=new UserVO("j03_124","이상무","1234",Level.GOLD,52,31,"jamesol@paran.com","");
		
		user04=new UserVO("j0","이상무","1234",Level.BASIC,1,0,"jamesol@paran.com","");
		
	}

	@After
	public void tearDown() throws Exception {
		LOG.debug("^^^^^^^^^^^");
		LOG.debug("^tearDown^");
		LOG.debug("^^^^^^^^^^^");		
	}

	//단건조회
	@Test
	public void doSelectOne() {
		//------------------------------
		//1. 기존 데이터 삭제
		//2. 입력
		//3. 단건조회
		//4. 입력 data,조회 data비교
		//------------------------------
		dao.doDelete(user01);
		int flag = dao.doInsert(user01);
		assertThat(flag, is(1));
		
		UserVO vsVO = (UserVO) dao.doSelectOne(user01);
		checkSameUser(vsVO, user01);
	}
	//단건 삭제
	@Test
	public void doInsert() {
		//------------------------------
		//1. 기존 데이터 삭제
		//2. 입력
		//3. count로 건수 확인
		//------------------------------
		dao.doDelete(user01);
		dao.doDelete(user02);
		dao.doDelete(user03);
		
		int flag = dao.doInsert(user01);
		assertThat(flag, is(1));
		
		
	    int cnt = dao.count(user04);
	    assertThat(cnt, is(1));
		
		
	}
	
	
	
	
	@Test
	public void doRetrieve() {
		//삭제
		//---------------------------------------
		//-삭제
		//---------------------------------------		
		dao.doDelete(user01);
		dao.doDelete(user02);
		dao.doDelete(user03);		
		//3건 추가
		int flag = dao.doInsert(user01);
		flag += dao.doInsert(user02);
		flag += dao.doInsert(user03);
		
		assertThat(flag, is(3));		
		//검색
		SearchVO  inVO=new SearchVO(10, 1, "10", "_124");
		List<UserVO>  list = (List<UserVO>) dao.doRetrieve(inVO);
		
		for(UserVO outVO:list) {
			LOG.debug(outVO.toString());
		}
		
		assertThat(list.size(), is(3));
		
	}
	
	
	@Test
	public void doUpdate() {
		//----------------------------
		//1. 모두 삭제
		//2. 단건입력
		//3. 단건입력 user01 수정
		//3.1. update호출
		//3.2. 단건조회 user01
		//4.비교:단건입력 user01 수정==단건조회 user01
		
		//1.
		//---------------------------------------
		//-삭제
		//---------------------------------------		
		dao.doDelete(user01);
		dao.doDelete(user02);
		dao.doDelete(user03);
		
		//2.
		//---------------------------------------
		//-입력
		//---------------------------------------			
		int flag = dao.doInsert(user01);
		assertThat(flag, is(1));
		
		//user01=new UserVO("j01_124","이상무","1234",Level.BASIC,1,0,"jamesol@paran.com","");
		user01.setName(user01.getName()+"_u");
		user01.setPasswd(user01.getPasswd()+"_u");
		user01.setLevel(Level.SILVER);//basic -> silver
		user01.setLogin(10);
		user01.setRecommend(9);
		user01.setEmail(user01.getEmail()+"_u");
		
		//3.1. update호출
		flag = dao.doUpdate(user01);
		assertThat(flag, is(1));
		
		//3.2. 단건조회 user01
		UserVO vsVO =(UserVO) dao.doSelectOne(user01);
		checkSameUser(vsVO, user01);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test(timeout = 1500)
	public void count() throws ClassNotFoundException, SQLException {
		//---------------------------------------
		//-삭제
		//---------------------------------------		
		dao.doDelete(user01);
		dao.doDelete(user02);
		dao.doDelete(user03);
		
		
		assertThat(dao.count(user04), is(0));
		
		//2. 1건추가
		//2.1. 건수 (1)		
		dao.doInsert(user01);
		assertThat(dao.count(user04), is(1));
		
		dao.doInsert(user02);
		assertThat(dao.count(user04), is(2));	
		
		dao.doInsert(user03);
		assertThat(dao.count(user04), is(3));			
	}	
	
	@Test
	public void getAll() {
		//-------------------------------------
		//1. 삭제
		//2. getAll()조회 : list.size() == 0
		//3. insert
		//3.1 getAll()조회 : list.size() == 1
		//3. insert
		//3.1 getAll()조회 : list.size() == 2	
		//3. insert
		//3.1 getAll()조회 : list.size() == 3
		//4.1 getAll()조회 : 입력데이터와 건건이 비교.
		//-------------------------------------
		
		//1. 삭제
		dao.doDelete(this.user01);
		dao.doDelete(this.user02);
		dao.doDelete(this.user03);
		
		UserVO  user04=new UserVO();
		user04.setU_id("_124");
		List<UserVO> list=dao.getAll(user04);
		assertThat(list.size(), is(0));
		
		//3.insert
		dao.doInsert(user01);
		List<UserVO> list01=dao.getAll(user04);
		LOG.debug("==============");
		LOG.debug("=list01.size()="+list01.size());
		LOG.debug("==============");
		assertThat(list01.size(), is(1));		
		
		dao.doInsert(user02);
		List<UserVO> list02=dao.getAll(user04);
		LOG.debug("==============");
		LOG.debug("=list02.size()="+list02.size());
		LOG.debug("==============");
		assertThat(list02.size(), is(2));		
		
		dao.doInsert(user03);
		List<UserVO> list03=dao.getAll(user04);
		LOG.debug("==============");
		LOG.debug("=list03.size()="+list03.size());
		LOG.debug("==============");
		assertThat(list03.size(), is(3));	
		
		for(UserVO vsVO :list03) {
			LOG.debug("=vsVO="+vsVO);
		}
		
		checkSameUser(this.user01,list03.get(0));
		checkSameUser(this.user02,list03.get(1));
		checkSameUser(this.user03,list03.get(2));
	}
	
	public void checkSameUser(UserVO orgVO, UserVO vsVO) {
		assertTrue(orgVO.equals(vsVO));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	@Ignore
	public void test() {
		LOG.debug("=====================");
		LOG.debug("=test()=");
		LOG.debug("=====================");
		
		
		LOG.debug("=====================");
		LOG.debug("=dao()="+dao);
		LOG.debug("=====================");		
		
		assertThat(1, is(1));
		
	}
	
	
	//특정 예외가 발생하면 성공
	@Test(expected = EmptyResultDataAccessException.class)
	public void getFailure() throws ClassNotFoundException, SQLException {
		LOG.debug("=====================");
		LOG.debug("=count() dao="+dao);
		LOG.debug("=====================");		
		//---------------------------------------
		//-삭제
		//---------------------------------------		
		dao.doDelete(user01);
		dao.doDelete(user02);
		dao.doDelete(user03);		
		
		assertThat(dao.count(user04), is(0));		
		
		user04.setU_id("unknowId");
		dao.doSelectOne(user04);
		
		
	}
	
	

}

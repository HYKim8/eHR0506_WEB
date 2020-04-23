package com.sist.ehr.member.web;

import javax.inject.Qualifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.sist.ehr.cmn.DTO;
import com.sist.ehr.cmn.MessageVO;
import com.sist.ehr.member.service.UserService;
import com.sist.ehr.member.service.UserVO;

@Controller
public class MemberController {

	private final Logger  LOG = LoggerFactory.getLogger(MemberController.class);
	
	//@Qualifier("dummyMailSender") : root-context.xml bean id
	@Autowired
	UserService userService;
	
	@RequestMapping(value="member/do_select_one.do",method = RequestMethod.POST
		       ,produces = "application/json;charset=UTF-8")
    @ResponseBody	
	public String doSelectOne(UserVO user) {
		LOG.debug("1===================");
		LOG.debug("1=user="+user);
		LOG.debug("1===================");		
		
		UserVO outVO = (UserVO) userService.doSelectOne(user);
		LOG.debug("1.2===================");
		LOG.debug("1.2=outVO="+outVO);
		LOG.debug("1.2===================");		
		
		Gson gson=new Gson();
		String json = gson.toJson(outVO);
		
		LOG.debug("1.3===================");
		LOG.debug("1.3=json="+json);
		LOG.debug("1.3===================");		
		
		return json;
	}
	
	
	@RequestMapping(value="member/do_delete.do",method = RequestMethod.POST
			       ,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doDelete(UserVO user) {
		LOG.debug("1===================");
		LOG.debug("1=user="+user);
		LOG.debug("1===================");		
		
		int flag = 0;
		flag = userService.doDelete(user);
		
		MessageVO message=new MessageVO();
		message.setMsgId(String.valueOf(flag));
		LOG.debug("1.2===================");
		LOG.debug("1.2=flag="+flag); 
		LOG.debug("1.2===================");	
		
		//성공
		if(flag ==1) {
			message.setMsgMsg(user.getU_id()+"님이 삭제 되었습니다.");
		//실패	
		}else {
			message.setMsgMsg(user.getU_id()+"삭제 실패.");
		}
		
		
		//JSON
		Gson gson=new Gson();
		String json = gson.toJson(message);
		
		LOG.debug("1.3===================");
		LOG.debug("1.3=json="+json); 
		LOG.debug("1.3===================");		
		
		return json;	
	}
	
	
	
	
	
	@RequestMapping(value = "member/do_update.do",method = RequestMethod.POST
			,produces = "application/json;charset=UTF-8")
	@ResponseBody	
	public String doUpdate(UserVO user) {
		LOG.debug("1===================");
		LOG.debug("1=user="+user);
		LOG.debug("1===================");
		
		int  flag = userService.doUpdate(user);
		LOG.debug("1.2===================");
		LOG.debug("1.2=flag="+flag); 
		LOG.debug("1.2===================");
		
		//메시지 처리
		MessageVO message=new MessageVO();

		message.setMsgId(flag+"");
		//성공
		if(flag ==1) {
			message.setMsgMsg(user.getName()+"님이 수정 되었습니다.");
		//실패	
		}else {
			message.setMsgMsg(user.getName()+"님 등록 실패.");			
		}		
		
		//JSON
		Gson gson=new Gson();
		String json = gson.toJson(message);
		
		LOG.debug("1.3===================");
		LOG.debug("1.3=json="+json); 
		LOG.debug("1.3===================");		
		
		return json;
		
	}
	
	
	@RequestMapping(value = "member/add.do",method = RequestMethod.POST
			,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String add(UserVO user) {
		String url= "member/member_mng";
		LOG.debug("1===================");
		LOG.debug("1=user="+user);
		LOG.debug("1===================");
		
		int  flag = userService.add(user);
		
		LOG.debug("1.2===================");
		LOG.debug("1.2=flag="+flag); 
		LOG.debug("1.2===================");
		
		//메시지 처리
		MessageVO message=new MessageVO();

		message.setMsgId(flag+"");
		//성공
		if(flag ==1) {
			message.setMsgMsg(user.getName()+"님이 등록 되었습니다.");
		//실패	
		}else {
			message.setMsgMsg(user.getName()+"님 등록 실패.");			
		}
		
		//JSON
		Gson gson=new Gson();
		String json = gson.toJson(message);
		
		LOG.debug("1.3===================");
		LOG.debug("1.3=json="+json); 
		LOG.debug("1.3===================");		
		
		return json;
	}
	
	
}











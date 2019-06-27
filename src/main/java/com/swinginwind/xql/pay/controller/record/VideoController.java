package com.swinginwind.xql.pay.controller.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.swinginwind.xql.pay.entity.TMembers;
import com.swinginwind.xql.pay.entity.VideoFile;
import com.swinginwind.xql.pay.entity.VideoPermission;
import com.swinginwind.xql.pay.entity.VideoPermissionForm;
import com.swinginwind.xql.pay.entity.VideoType;
import com.swinginwind.xql.pay.service.UserService;
import com.swinginwind.xql.pay.service.VideoService;

@Controller
@RequestMapping("/video")
public class VideoController {
	
	@Autowired
	VideoService videoService;
	
	@Autowired
	UserService userService;
	
    @RequestMapping(value = "/list")
    public String videoList(){
    	return "video_list.html";
    }
    
    @RequestMapping(value = "/play/{id}")
    public ModelAndView videoPlay(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
    	int userId = -1;
    	ModelAndView mv = null;
    	TMembers memberT = (TMembers) request.getSession().getAttribute("userInfo");
    	if(memberT != null) {
    		userId = memberT.getUserid();
    		boolean permitted = videoService.isVideoFilePermitted(id, userId);
    		if(permitted) {
    			mv = new ModelAndView("video_play.html");
    	    	mv.addObject("video", videoService.selectVideoFileById(id));
    		}
    		else {
    			mv = new ModelAndView("/error/401.html");
    		}
    	}
    	else {
    		mv = new ModelAndView("dologin.html");
    	}
    	
    	return mv;
    }
	
    @RequestMapping(value = "/getVideoTypeByPid", method = RequestMethod.GET)
    @ResponseBody
    public List<VideoType> getVideoTypeByPid(int pid) {
    	return videoService.selectVideoTypeByPid(pid);
    }
    
    @RequestMapping(value = "/getVideoFileByPid", method = RequestMethod.GET)
    @ResponseBody
    public List<VideoFile> getVideoFileByPid(int pid, HttpServletRequest request) {
    	int userId = -1;
    	TMembers memberT = (TMembers) request.getSession().getAttribute("userInfo");
    	if(memberT != null)
    		userId = memberT.getUserid();
    	return videoService.selectVideoFileByPidPermitted(pid, userId);
    }
    
    @RequestMapping(value = "/videoPermitPage", method = RequestMethod.GET)
    public ModelAndView videoPermitPage() {
    	ModelAndView mv = null;
    	mv = new ModelAndView("video_permit.html");
    	return mv;
    }
    
    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    @ResponseBody
    public List<TMembers> userList() {
    	return userService.select(new TMembers());
    }
    
    @RequestMapping(value = "/videoTree", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> videoTree() {
    	return videoService.selectVideoTree();
    }
    
    @RequestMapping(value = "/videoPermission", method = RequestMethod.GET)
    @ResponseBody
    public List<VideoPermission> videoPermission(int userId) {
    	return videoService.selectVideoPermissionByUserId(userId);
    }
    
    @RequestMapping(value = "/submitVideoPermission", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitVideoPermission(@RequestBody VideoPermissionForm form) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("status", "success");
    	videoService.submitVideoPermission(form);
    	return result;
    }
    
    @RequestMapping(value = "/videoConfigPage", method = RequestMethod.GET)
    public ModelAndView videoConfigPage() {
    	ModelAndView mv = null;
    	mv = new ModelAndView("video_config.html");
    	return mv;
    }
    
    @RequestMapping(value = "/getVideoTypeAll", method = RequestMethod.GET)
    @ResponseBody
    public List<VideoType> getVideoTypeAll() {
    	return videoService.selectVideoTypeAll();
    }

}

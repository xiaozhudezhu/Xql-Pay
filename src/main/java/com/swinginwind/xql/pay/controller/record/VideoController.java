package com.swinginwind.xql.pay.controller.record;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.swinginwind.xql.pay.config.AppConfig;
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

	@Autowired
	AppConfig appConfig;

	@RequestMapping(value = "/list")
	public String videoList() {
		return "video_list.html";
	}

	@RequestMapping(value = "/play/{id}")
	public ModelAndView videoPlay(@PathVariable("id") Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		int userId = -1;
		ModelAndView mv = null;
		TMembers memberT = (TMembers) request.getSession().getAttribute("userInfo");
		if (memberT != null) {
			userId = memberT.getUserid();
			boolean permitted = videoService.isVideoFilePermitted(id, userId);
			if (permitted) {
				mv = new ModelAndView("video_play.html");
				mv.addObject("video", videoService.selectVideoFileById(id));
			} else {
				mv = new ModelAndView("/error/401.html");
			}
		} else {
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
		if (memberT != null)
			userId = memberT.getUserid();
		return videoService.selectVideoFileByPidPermitted(pid, userId);
	}

	@RequestMapping(value = "/videoPermitPage", method = RequestMethod.GET)
	public ModelAndView videoPermitPage(HttpServletRequest request) {
		ModelAndView mv = null;
		TMembers userInfo = (TMembers) request.getSession().getAttribute("userInfo");
		if(userInfo == null || !appConfig.getAdminIds().contains(userInfo.getUserid())) {
			mv = new ModelAndView("/error/401.html");;
		}
		else
			mv = new ModelAndView("video_permit.html");
		return mv;
	}

	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	@ResponseBody
	public List<TMembers> userList(HttpServletRequest request) {
		TMembers userInfo = (TMembers) request.getSession().getAttribute("userInfo");
		if(userInfo == null || !appConfig.getAdminIds().contains(userInfo.getUserid())) {
			return null;
		}
		return userService.select(new TMembers());
	}

	@RequestMapping(value = "/videoTree", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> videoTree() {
		return videoService.selectVideoTree();
	}

	@RequestMapping(value = "/videoPermission", method = RequestMethod.GET)
	@ResponseBody
	public List<VideoPermission> videoPermission(int userId, HttpServletRequest request) {
		TMembers userInfo = (TMembers) request.getSession().getAttribute("userInfo");
		if(userInfo == null || !appConfig.getAdminIds().contains(userInfo.getUserid())) {
			return null;
		}
		return videoService.selectVideoPermissionByUserId(userId);
	}

	@RequestMapping(value = "/submitVideoPermission", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitVideoPermission(@RequestBody VideoPermissionForm form, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", "success");
		TMembers memberT = (TMembers) request.getSession().getAttribute("userInfo");
		if (memberT != null && appConfig.getAdminIds().contains(memberT.getUserid())) {
			form.setOperateUserId(memberT.getUserid());
			form.setOperateUserName(memberT.getName());
			videoService.submitVideoPermission(form);
		}
		else {
			result.put("status", "401");
		}
		return result;
	}

	@RequestMapping(value = "/videoConfigPage", method = RequestMethod.GET)
	public ModelAndView videoConfigPage(HttpServletRequest request) {
		ModelAndView mv = null;
		TMembers userInfo = (TMembers) request.getSession().getAttribute("userInfo");
		if(userInfo == null || !appConfig.getAdminIds().contains(userInfo.getUserid())) {
			mv = new ModelAndView("/error/401.html");;
		}
		else
			mv = new ModelAndView("video_config.html");
		return mv;
	}

	@RequestMapping(value = "/getVideoTypeAll", method = RequestMethod.GET)
	@ResponseBody
	public List<VideoType> getVideoTypeAll() {
		return videoService.selectVideoTypeAll();
	}

	@RequestMapping(value = "/saveVideoFile", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveVideoFile(VideoFile videoFile, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		TMembers memberT = (TMembers) request.getSession().getAttribute("userInfo");
		if (memberT != null && appConfig.getAdminIds().contains(memberT.getUserid())) {
			Integer userId = memberT.getUserid();
			try {
				videoService.saveVideoFile(videoFile, userId);
				result.put("status", "success");
			} catch (IOException e) {
				result.put("status", "fail");
			}
		}
		else
			result.put("status", "401");
		return result;
	}
	
	@RequestMapping(value = "/deleteVideoFile", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteVideoFile(String ids, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		TMembers memberT = (TMembers) request.getSession().getAttribute("userInfo");
		if (memberT != null && appConfig.getAdminIds().contains(memberT.getUserid())) {
			videoService.deleteVideoFile(ids);
			result.put("status", "success");
		}
		else {
			result.put("status", "401");
		}
		return result;
	}

	@RequestMapping(value = "/screenshot/{id}")
	public void screenshot(@PathVariable("id") String id, HttpServletResponse response) {
		String path = appConfig.getFileDir() + "/screenshot/" + id + ".png";
		File destFile = new File(path);
		if (destFile.exists()) {
			InputStream bais;
			try {
				bais = new FileInputStream(destFile);
				byte[] buffer = new byte[10240];
				response.setContentType(MediaType.IMAGE_PNG_VALUE);
				// 设置文件大小, 客户端可以读取文件大小显示进度
				response.setContentLength((int) destFile.length());
				OutputStream out = response.getOutputStream();
				int length = 0;
				while ((length = bais.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				out.flush();
				bais.close();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@RequestMapping(value = "/videoPayPage", method = RequestMethod.GET)
	public ModelAndView videoPayPage(Integer videoId) {
		ModelAndView mv = null;
		mv = new ModelAndView("video_pay.html");
		if(videoId != null)
			mv.addObject("video", videoService.selectVideoFileById(videoId));
		return mv;
	}

}

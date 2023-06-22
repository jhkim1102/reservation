package com.example.reservation.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.reservation.dto.Reservation;
import com.example.reservation.service.ReservationService;
import com.example.reservation.util.MyUtil;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReservaitonController {

	
	  @Autowired
	  private ReservationService reservationService;
	 
	  @Autowired
	  MyUtil myUtil;
	 

	@RequestMapping(value = "/")
	public String index() {
		return "/index";
	}
	
	@RequestMapping(value = "/created", method = RequestMethod.GET)
	public String created() {
		return "bbs/created";
	}

	@RequestMapping(value = "/created", method = RequestMethod.POST)
	public String createdOK(Reservation reservation, HttpServletRequest request, Model model) {
		try {
			int maxNum = reservationService.maxNum();
			
			reservation.setNum(maxNum + 1);
			
			reservationService.insertData(reservation);
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시글을 작성중 에러가 발생했습니다");
			return "bbs/created";
		}
		
		return "redirect:/list";
	}
	
	@RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String list(Reservation reservation, HttpServletRequest request, Model model) {
		
		try {
			String pageNum = request.getParameter("pageNum");
			int currentPage = 1;
			
			if(pageNum !=null) currentPage = Integer.parseInt(pageNum);
			
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
			
			if(searchValue == null) {
				searchKey = "subject";
				searchValue = "";
			}else {
				if(request.getMethod().equalsIgnoreCase("Get")) {
					searchValue = URLDecoder.decode(searchValue, "UTF-8");
				}
			}
			
			int dataCount = reservationService.getDataCount(searchKey, searchValue);
			
			int numPerPage = 3;
			int totalPage = myUtil.getPageCount(numPerPage, dataCount);
			
			if(currentPage > totalPage) currentPage = totalPage;
			
			int start = (currentPage -1)*numPerPage+1;
			int end = currentPage*numPerPage;
			
			List<Reservation> lists = reservationService.getLists(searchKey, searchValue, start, end);
			
			String param = "";
			
			if(searchValue !=null && !searchValue.equals("")) {
				//검색어가 있다면
				param += "searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
			}
			
			String listUrl = "/list";
			
			if(!param.equals("")) listUrl += "?" + param;
			
			String pageIndexList = myUtil.pageIndexList(currentPage, totalPage, listUrl);
			
			String articleUrl = "/article?pageNum=" + currentPage;
			
			if(!param.equals("")) {
				articleUrl += "&" + param;
			}
			
			model.addAttribute("lists", lists);	
			model.addAttribute("articleUrl", articleUrl);	
			model.addAttribute("pageIndexList", pageIndexList);	
			model.addAttribute("dataCount", dataCount);	
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "리스트를 불러오는 중 에러가 발생했습니다");
		}
		
		return "bbs/list";
		
	}
		@RequestMapping(value = "/updated", method = RequestMethod.GET)
		public String updated(HttpServletRequest request, Model model) {
			try {
				int num = Integer.parseInt(request.getParameter("num"));
				String pageNum = request.getParameter("pageNum");
				String searchKey = request.getParameter("searchKey");
				String searchValue = request.getParameter("searchValue");
				
				if(searchValue !=null) {
					searchValue = URLDecoder.decode(searchValue, "UTF-8");
				}
				
				Reservation reservation = reservationService.getReadData(num);
				
				if(reservation == null) {
					return "redirect:/list?pageNum=" + pageNum;
				}
				
				String param = "pageNum=" + pageNum;
				
				if(searchValue !=null && !searchValue.equals("")) {
					//검색어가 있다면
					param += "searchKey=" + searchKey;
					param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
				}
				
				model.addAttribute("reservation", reservation);
				model.addAttribute("pageNum", pageNum);
				model.addAttribute("params", param);
				model.addAttribute("searchKey", searchKey);
				model.addAttribute("searchValue", searchValue);
				
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("errorMessage", "수정화면을 불러오는 중 에러가 발생했습니다");
			}
			
			return "bbs/updated";
		}
		
		@RequestMapping(value = "/updated_ok",  method = RequestMethod.POST)
		public String updatedOK(Reservation reservation, HttpServletRequest request, Model model) {
			String pageNum = request.getParameter("pageNum");
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
			String param = "?pageNum=" + pageNum;
			
			try {
				reservation.setContent(reservation.getContent().replaceAll("<br/>", "\r\n"));
				reservationService.updateData(reservation);
				

				if(searchValue !=null && !searchValue.equals("")) {
					param += "searchKey=" + searchKey;
					param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					param += "&errorMessage=" + URLEncoder.encode("게시글 수정 중 에러가 발생했습니다", "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
			}
				
		}
			
			return "redirect:/list" + param;
	}
			
	@RequestMapping(value = "/article", method = RequestMethod.GET)
	public String article(HttpServletRequest request, Model model) {
		
		try {
			int num = Integer.parseInt(request.getParameter("num"));
			String pageNum = request.getParameter("pageNum");
			String searchKey = request.getParameter("searchKey");
			String searchValue = request.getParameter("searchValue");
			
			if(searchValue != null) {
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
			
			Reservation reservation = reservationService.getReadData(num);
			
			if(reservation == null) {
				return "redirect:list?pageNum=" + pageNum;
			}
			
			int lineSu = reservation.getContent().split("\n").length;
			
			String param = "pageNum=" + pageNum;
			
			if(searchValue !=null && !searchValue.equals("")) {
				//검색어가 있다면
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); //컴퓨터의 언어로 인코딩
			}
			
			model.addAttribute("reservation",reservation);
			model.addAttribute("params",param);
			model.addAttribute("lineSu",lineSu);
			model.addAttribute("pageNum",pageNum);	
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "게시글을 불러오는 중 에러가 발생했습니다");
		}
		return "bbs/article";
			
	}
	
	@RequestMapping(value = "/deleted_ok", method= {RequestMethod.GET})
	public String deleteOK(HttpServletRequest request, Model model) {
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		String param = "?pageNum=" +pageNum;
		
		try {
			reservationService.deleteData(num);
			
			if(searchValue !=null && !searchValue.equals("")) {
				
				param += "&searchKey=" + searchKey;
				param += "&searchValue=" + URLEncoder.encode(searchValue, "UTF-8"); 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "redirect:/list" + param;
	
	
	
	}
}

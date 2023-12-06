package com.xyz.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.xyz.Repository.NotesRepo;
import com.xyz.Repository.UserRepo;
import com.xyz.entity.Notes;
import com.xyz.entity.Users;
import com.xyz.service.NotesService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepo ur;
	@Autowired
	private NotesService noteService;
	@Autowired
	private NotesRepo nr;
	@Autowired
	private BCryptPasswordEncoder bpc;
	
	@ModelAttribute
	public Users allObjPage(Model md,Principal p) {
		String em= p.getName();
		Users u= ur.findByEmail(em);
		md.addAttribute("uo", u);
		return u;
	}
	
	@GetMapping("/AddNotesPage")
	public String AddNotes() {
		return "AddNotes";
	}
	
	@GetMapping("/home")
	public String home() {
		return "index";
	}
	
	
	
//	@GetMapping("/ViewNotesPage")
//	public String getViewNotes(Model md,Principal p) {
//		Users u = allObjPage(md, p);
//		List<Notes> ll= noteService.getAllNotesByUser(u);
//		md.addAttribute("ll", ll);
//		return "ViewNotes";
//	}
	
	@GetMapping("/ViewNotesPage")
	public String getViewNotes(Model md,Principal p) {
//		Users u = allObjPage(md, p);
		
		return getPaginate(0,md,p,"date","asc");
	}
	@GetMapping(path = "/page/{pageIndex}")
	public String getPaginate(@PathVariable("pageIndex") int pageIndex,Model m,Principal p,
			@Param("sortFiled") String sortFiled,@Param("sortDirection") String sortDirection) {
		Users u = allObjPage(m, p);
		
		
		Sort st = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(sortFiled).ascending() : Sort.by(sortFiled).descending() ; 
		Pageable pageable = PageRequest.of(pageIndex, 4,st);
		
		Page<Notes> page= nr.findAllByUser(u,pageable);
		List<Notes> note= page.getContent();
		m.addAttribute("TotalElements", page.getTotalElements());
		m.addAttribute("TotalPages", page.getTotalPages());
		m.addAttribute("pageIndex", pageIndex);
		m.addAttribute("ll", note);
		
		m.addAttribute("sortFiled", sortFiled);
		m.addAttribute("sortDirection", sortDirection);
		m.addAttribute("reverseDirection", (sortDirection.equalsIgnoreCase("asc"))?"desc" :"asc");
		return "ViewNotes";
	}
	
	
	
	
	
	@PostMapping(path = "/addNotes")
	public String addNotes(@ModelAttribute Notes note,HttpSession hs,Principal p,Model m) {
		note.setDate(LocalDate.now());
		note.setUser(allObjPage(m, p));
		Notes n= noteService.saveNote(note);
		if(n!=null) {
			hs.setAttribute("msg", "Your Note Added Successfully...");
		}else {
			hs.setAttribute("msg", "Something Went Wrong On Server!!...");
		}
		return "redirect:/user/AddNotesPage";
	}
	@GetMapping(path = "/editNote/{id}")
	public String editNote(@PathVariable("id") int id,Model m) {
		Notes n = noteService.getNotesById(id);
		m.addAttribute("note", n);
		return "EditNotes";
	}
	
	@PostMapping(path = "/updateNote")
		public String updateNote(@ModelAttribute Notes notes,HttpSession hs,Model m,Principal p) {
		notes.setUser(allObjPage(m, p));
		notes.setDate(LocalDate.now());
		Notes n= noteService.saveNote(notes);
		if(n!=null) {
			hs.setAttribute("msg", "Your Note Updated Successfully...");
		}else {
			hs.setAttribute("msg", "Something Went Wrong On Server!!...");
		}
		return "redirect:/user/ViewNotesPage";
	}
	
	@GetMapping(path = "/deleteNote/{id}")
	public String deleteNote(@PathVariable("id") int id,HttpSession hs) {
		boolean f= noteService.deleteNote(id);
		if(f) {
			hs.setAttribute("msg", "Your Note Deleted Successfully...");
		}else {
			hs.setAttribute("msg", "Something Went Wrong On Server!!...");
		}
		return "redirect:/user/ViewNotesPage";
	}
	
	
	@PostMapping(path = "/searchNote")
	public String getSearchNote(@RequestParam("searchKey") String search,Model m,HttpSession hs,Principal p) {
		Users us = allObjPage(m, p);
		List<Notes> l= noteService.getAllNotesBySerch(search,us);
		if(l.isEmpty()) {
			hs.setAttribute("msg", "No Result Found !!! ");
			return "redirect:/user/ViewNotesPage";
		}
		m.addAttribute("ll", l);
		return "search";
	}

	
	
	@PostMapping(path = "/filterNote")
	public String getFilter(@RequestParam("category") String category,@RequestParam("date") String date,HttpSession hs,Model m,Principal p) {
//		System.out.println(category);
//		System.out.println(date);
		Users u = allObjPage(m, p);
		if(category.equalsIgnoreCase("co") && date.equalsIgnoreCase("")) {
			hs.setAttribute("msg", "No Match Found!!!");
			return "redirect:/user/ViewNotesPage";
			
		}else if(category.equalsIgnoreCase("co") || date.equalsIgnoreCase("")) {
			 if(category.equalsIgnoreCase("co")){
				 List<Notes> ll= noteService.getAllNotesByDate(date,u);
				 if(ll.isEmpty()) {
					 hs.setAttribute("msg", "No Match Found!!!");
					 return "redirect:/user/ViewNotesPage"; 
				 }
				m.addAttribute("ll", ll) ;
			 }else {
				 List<Notes> ll= noteService.getAllNotesByCategory(category,u);
				 if(ll.isEmpty()) {
					 hs.setAttribute("msg", "No Match Found!!!");
					 return "redirect:/user/ViewNotesPage"; 
				 }
					m.addAttribute("ll", ll) ;
			 }
			 return "filterPage";
		}else {
			List<Notes> ll = noteService.getAllNotesByCategoryAndDate(category, date,u);
			if(ll.isEmpty()) {
				 hs.setAttribute("msg", "No Match Found!!!");
				 return "redirect:/user/ViewNotesPage"; 
			 }
			m.addAttribute("ll", ll);
			return "filterPage";
		}
		
	}
	@GetMapping(path = "/changePasswordPage")
	public String getchangePasswordPage() {
		return "changePasswordPage";
	}
	
	
	@PostMapping(path = "/changePassword")
	public String changePassword(Principal p,@RequestParam("oldPassword") String oldPassword,@RequestParam("newpassword") String newpassword,HttpSession hs ) {
		String email= p.getName();
		 Users user= ur.findByEmail(email);
		boolean f= bpc.matches(oldPassword, user.getPassword());
		if(f) {
			user.setPassword(bpc.encode(newpassword));
			Users u= ur.save(user);
			if(u!=null) {
				hs.setAttribute("msg", "Your Password Changed Successfully!!!");
			}else {
				hs.setAttribute("msg", "Something Went Wrong On Server...");
			}
		}else {
			hs.setAttribute("msg", "Old Password is Incorrect!!!");
		}
		return "redirect:/user/changePasswordPage";
	}
	
	

}

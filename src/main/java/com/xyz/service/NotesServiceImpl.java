package com.xyz.service;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.xyz.Repository.NotesRepo;
import com.xyz.entity.Notes;
import com.xyz.entity.Users;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepo nr;
	
	
	@Override
	public Notes saveNote(Notes note) {
		// TODO Auto-generated method stub
		return nr.save(note);
	}

	@Override
	public List<Notes> getAllNotesByUser(Users user) {
		// TODO Auto-generated method stub
		List<Notes> ll= nr.findByUser(user);
		return ll;
	}

	@Override
	public Notes updateNote(Notes note) {
		// TODO Auto-generated method stub
		return nr.save(note);
	}

	@Override
	public boolean deleteNote(int id) {
		// TODO Auto-generated method stub
		Notes n= nr.findById(id).get();
		if(n!=null) {
			nr.delete(n);
			return true;
		}
		return false;
	}

	@Override
	public Notes getNotesById(int id) {
		// TODO Auto-generated method stub
		return  nr.findById(id).get() ;
	}

	@Override
	public List<Notes> getAllNotesBySerch(String search,Users user) {
		// TODO Auto-generated method stub
		String ss = "%"+search+"%";
		List<Notes> ll= nr.getNotesBySearch(ss,user);
		return ll;
	}

	@Override
	public List<Notes> getAllNotesByCategory(String category,Users user) {
		// TODO Auto-generated method stub
		return nr.getByCategory(category,user);
	}

	@Override
	public List<Notes> getAllNotesByDate(String date,Users user) {
		// TODO Auto-generated method stub
		LocalDate ld = LocalDate.parse(date);
		return nr.getByDate(ld,user);
	}

	@Override
	public List<Notes> getAllNotesByCategoryAndDate(String category, String date,Users users) {
		// TODO Auto-generated method stub
		LocalDate ld = LocalDate.parse(date);
		return nr.getByCategoryAndDate(category, ld,users);
	}

	
	
	
	
}

package com.xyz.service;

import java.util.List;

import com.xyz.entity.Notes;
import com.xyz.entity.Users;

public interface NotesService {

	public Notes saveNote(Notes note);
	public List<Notes> getAllNotesByUser(Users user);
	public Notes updateNote(Notes note);
	public boolean deleteNote(int id);
	public Notes getNotesById(int id);
	
	public List<Notes> getAllNotesBySerch(String search,Users user);
	
	public List<Notes> getAllNotesByCategory(String category,Users user);
	public List<Notes> getAllNotesByDate(String date,Users user);
	public List<Notes> getAllNotesByCategoryAndDate(String category, String date,Users user);
	
}

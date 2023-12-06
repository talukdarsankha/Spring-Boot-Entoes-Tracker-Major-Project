package com.xyz.Repository;

import java.time.LocalDate;
import java.util.List;import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xyz.entity.Notes;
import com.xyz.entity.Users;

public interface NotesRepo extends JpaRepository<Notes, Integer> {

	public List<Notes> findByUser(Users user);
	
	
	@Query("select u from Notes u Where (u.title like ?1 or u.description like ?1) and u.user=?2")
	public List<Notes> getNotesBySearch(String search,Users user);
	
	@Query("select u from Notes u where u.category=?1 and user=?2")
	public List<Notes> getByCategory(String category,Users user);
	
	@Query("select u from Notes u where u.date=?1 and u.user=?2")
	public List<Notes> getByDate(LocalDate date,Users user);
	@Query("select u from Notes u Where u.category=?1 and u.date=?2 and u.user=?3")
	public List<Notes> getByCategoryAndDate(String s1,LocalDate s2,Users user);
	
	
	
	
	public Page<Notes> findAllByUser(Users user,Pageable pg);
	
	
	
}

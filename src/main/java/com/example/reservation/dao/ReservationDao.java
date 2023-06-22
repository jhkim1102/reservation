package com.example.reservation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.reservation.dto.Reservation;

@Mapper
public interface ReservationDao {
	public int maxNum() throws Exception;
	
	public void insertData(Reservation reservation) throws Exception;
	
	public int getDataCount(String searchKey, String searchValue) throws Exception;
	
	public List<Reservation> getLists(String searchKey, String searchValue, int start, int end) throws Exception;
	
	public Reservation getReadData(int num) throws Exception;
	
	public void updateData(Reservation reservation) throws Exception;
	
	public void deleteData(int num) throws Exception;
	
	
}

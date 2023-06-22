package com.example.reservation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reservation.dao.ReservationDao;
import com.example.reservation.dto.Reservation;

@Service
public class ReservationImpl implements ReservationService{
	
	@Autowired
	private ReservationDao reservationMapper;

	@Override
	public int maxNum() throws Exception {
		return reservationMapper.maxNum();
	}

	@Override
	public void insertData(Reservation reservation) throws Exception {
		reservationMapper.insertData(reservation);
		
	}

	@Override
	public int getDataCount(String searchKey, String searchValue) throws Exception {
		return reservationMapper.getDataCount(searchKey, searchValue);
	}

	@Override
	public List<Reservation> getLists(String searchKey, String searchValue, int start, int end) throws Exception {
		return reservationMapper.getLists(searchKey, searchValue, start, end);
	}

	@Override
	public Reservation getReadData(int num) throws Exception {
		return reservationMapper.getReadData(num);
	}

	@Override
	public void updateData(Reservation reservation) throws Exception {
		reservationMapper.updateData(reservation);
		
	}

	@Override
	public void deleteData(int num) throws Exception {
		reservationMapper.deleteData(num);
		
	}
	
	
}

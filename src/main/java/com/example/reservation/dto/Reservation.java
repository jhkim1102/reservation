package com.example.reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reservation {
	private int num;
	private String name;
	private String pwd;
	private String email;
	private String subject;
	private String content;
	private String created;	
}


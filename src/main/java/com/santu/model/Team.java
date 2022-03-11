package com.santu.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Team")
public class Team {
	
	@Transient
	public static final String SEQUENCE_NAME = "team_sequence";

	@Id
	private long id;
	private String name;
	private String email;
	private String number;
	private String player1;
	private String player2;
	private String player3;
	private String player4;
	private long matchPlayed;
	private long matchWin;
	private long matchLoose;
	private long pointsGain;
	private long pointsLoose;
	private long pointsDiff;
	
}

package com.santu.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Document(collection = "Match")
public class Match {

	@Transient
	public static final String SEQUENCE_NAME = "match_sequence";

	@Id
	private long id;
	private long teamAId;
	private String teamAName;
	private int teamAScore;
	private long teamBId;
	private String teamBName;
	private int teamBScore;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	private String time;
	
	private String roomNumber;
	private String roomPassword;

}

package com.santu.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.santu.dao.MatchDao;
import com.santu.dao.TeamDao;
import com.santu.model.Match;
import com.santu.model.Team;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BGMIService {

	@Autowired
	private TeamDao teamDao;

	@Autowired
	private MatchDao matchDao;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${bgmi.email.test}")
	public String email3;

	@Value("${bgmi.base.link}")
	public String link;
	
	public void saveTeam(Team team) {
		team.setId(sequenceGeneratorService.generateSequence(Team.SEQUENCE_NAME));
		Team team1 = teamDao.save(team);
		
		String team1Name = team1.getName();
		
		String email1 = team1.getEmail();
		
		String name1 = team1.getPlayer1();
		
		String body= "Hi "+name1
				+",\n \n Congratulations! \n \n Your team "+team1Name+" has been successfully registered for BGMI-M24 Contest. "
						
						+ "\n Please use the bellow link for match update:- "
						+ "\n \n Point Table:- "+link+"team/pointTable"
						+ "\n Time Table :- "+link+"team/timeTable"
						+ " \n \n \n Thanks, \n BGMI M24 Contest Team ";

		String[] to = {email1, email3};
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(to);

			msg.setSubject("Registered Successfully");
			msg.setText(body);
			javaMailSender.send(msg);
		} catch (Exception ex) {
			log.info("failed to sent mail on "+to);
			log.error(ex.getMessage());
			log.error(ex.getCause());
		}
	}

	public List<Match> generteMatch() {
		List<Team> teamList = teamDao.findAll();
		List<Match> matchList = new ArrayList<Match>();
		// generate combination
		for (int i = 0; i < teamList.size(); i++) {
			for (int j = i; j < teamList.size(); j++) {
				if (i != j) {
					Match match = new Match();
					match.setTeamAId(teamList.get(i).getId());
					match.setTeamAName(teamList.get(i).getName());
					match.setTeamBId(teamList.get(j).getId());
					match.setTeamBName(teamList.get(j).getName());
					match.setDate(new Date(System.currentTimeMillis()));
					match.setTime("00:00");
					matchList.add(match);
				}
			}
		}
		Collections.shuffle(matchList);
		matchList.forEach(match -> match.setId(sequenceGeneratorService.generateSequence(Match.SEQUENCE_NAME)));
		return matchDao.saveAll(matchList);
	}

	public List<Match> getMatchList() {
		List<Match> allMatch = matchDao.findAll();
		Comparator<Match> compare = Comparator
                .comparing(Match::getDate)
                .thenComparing(Match::getTime);
		
		List<Match> sortedMatchList = allMatch.stream()
                .sorted(compare)
                .collect(Collectors.toList());
		
		sortedMatchList.forEach(m-> {
			m.setBothZero((m.getTeamAScore() == 0) && (m.getTeamBScore() == 0));
			m.setOrNotZero((m.getTeamAScore() != 0) || (m.getTeamBScore() != 0));
		});
		
		return sortedMatchList;
	}

	public Match getMatch(long id) {
		Match match = matchDao.findById(id).get();
		return match;
	}

	public void addDateTime(Match match) {
		Match newMatch = matchDao.findById(match.getId()).get();
		newMatch.setDate(match.getDate());
		newMatch.setTime(match.getTime());
		matchDao.save(newMatch);
	}

	public void addResult(Match match) {
		Match newMatch = matchDao.findById(match.getId()).get();
		newMatch.setTeamAScore(match.getTeamAScore());
		newMatch.setTeamBScore(match.getTeamBScore());
		matchDao.save(newMatch);
		Team teamA = teamDao.findById(newMatch.getTeamAId()).get();
		updatePoint(teamA, match.getTeamAScore(), match.getTeamBScore());
		Team teamB = teamDao.findById(newMatch.getTeamBId()).get();
		updatePoint(teamB, match.getTeamBScore(),match.getTeamAScore());
	}
	
	private void updatePoint(Team team, int gain, int loose) {
		team.setMatchPlayed(team.getMatchPlayed()+1);
		if(gain>loose) {
			team.setMatchWin(team.getMatchWin()+1);
		}else if (gain<loose) {
			team.setMatchLoose(team.getMatchLoose()+1);
		}
		team.setPointsGain(team.getPointsGain()+gain);
		team.setPointsLoose(team.getPointsLoose()+loose);
		team.setPointsDiff(team.getPointsDiff()+(gain-loose));
		teamDao.save(team);
	}

	public List<Team> getTeams() {
		List<Team> teamList = teamDao.findAll();
		Comparator<Team> compare = Comparator
                .comparing(Team::getMatchWin)
                .thenComparing(Team::getPointsGain)
        		.thenComparing(Team::getPointsDiff).reversed();
		
		List<Team> sortedTeamList = teamList.stream()
                .sorted(compare)
                .collect(Collectors.toList());
		
		return sortedTeamList;
	}

	public void startMatch(Match match) {
		
		Team team1 = teamDao.findById(match.getTeamAId()).get();
		Team team2 = teamDao.findById(match.getTeamBId()).get();
		
		String team1Name = team1.getName();
		String team2Name = team2.getName();
		
		String email1 = team1.getEmail();
		String email2 = team2.getEmail();
		
		String name1 = team1.getPlayer1();
		String name2 = team2.getPlayer1();
		
		String roomNumber = match.getRoomNumber();
		String roomPassword = match.getRoomPassword();
		String time = match.getTime();
		String body= "Hi "+name1+" and "+name2
				+",\n \n Please use the bellow Room Id and Password to join the match on or before "+time
				+" \n Match:- "+team1Name+" VS "+team2Name
				+" \n Room Id:- "+roomNumber
				+" \n Password:- "+roomPassword
				+" \n \n \n Thanks, \n BGMI M24 Contest Team";

		String[] to = {email1, email2, email3};
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setTo(to);

			msg.setSubject("Room Id & Password "+time);
			msg.setText(body);
			javaMailSender.send(msg);
		} catch (Exception ex) {
			log.info("failed to sent mail on "+to);
			log.error(ex.getMessage());
			log.error(ex.getCause());
		}
	}

	public void addMatch(Match match) {
		Optional<Team> findByIdA = teamDao.findById(match.getTeamAId());
		Optional<Team> findByIdB = teamDao.findById(match.getTeamBId());
		if(findByIdA.isPresent() && findByIdB.isPresent()) {
			Team teamA = new Team();
			Team teamB = new Team();
			teamA=findByIdA.get();
			teamB=findByIdB.get();
			match.setTeamAName(teamA.getName());
			match.setTeamBName(teamB.getName());
			match.setDate(new Date(System.currentTimeMillis()));
			match.setTime("00:00");
			match.setId(sequenceGeneratorService.generateSequence(Match.SEQUENCE_NAME));
			matchDao.save(match);
		}
	}

	public void removeResult(long id) {
		Match match = matchDao.findById(id).get();
		int teamAScore = match.getTeamAScore();
		int teamBScore = match.getTeamBScore();
		
		match.setTeamAScore(0);
		match.setTeamBScore(0);
		matchDao.save(match);
		
		Team teamA = teamDao.findById(match.getTeamAId()).get();
		removePoint(teamA, teamAScore, teamBScore);
		Team teamB = teamDao.findById(match.getTeamBId()).get();
		removePoint(teamB, teamBScore, teamAScore);
		
	}

	private void removePoint(Team team, int gain, int loose) {
		team.setMatchPlayed(team.getMatchPlayed()-1);
		if(gain>loose) {
			team.setMatchWin(team.getMatchWin()-1);
		}else if (gain<loose) {
			team.setMatchLoose(team.getMatchLoose()-1);
		}
		team.setPointsGain(team.getPointsGain()-gain);
		team.setPointsLoose(team.getPointsLoose()-loose);
		team.setPointsDiff(team.getPointsDiff()-(gain-loose));
		teamDao.save(team);
		
	}

	

}

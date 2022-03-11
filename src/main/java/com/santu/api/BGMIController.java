package com.santu.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santu.model.Match;
import com.santu.model.Team;
import com.santu.service.BGMIService;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/admin/bgmi/the")
public class BGMIController {

	@Autowired
	private BGMIService bGMIService;

	@GetMapping(value = "/startAdmin")
	public String start(Model model) {
		return "start";
	}
	@GetMapping(value = "/addTeamForm")
	public String addTeamForm(Model model) {
		Team team = new Team();
		model.addAttribute("team", team);
		return "add_team";
	}
	@PostMapping(value = "/addTeam")
	public String saveTeam(@ModelAttribute("team") Team team) {
		bGMIService.saveTeam(team);
		return "redirect:/admin/bgmi/the/startAdmin";
	}
	//auto generated
	@GetMapping(value = "/allMatch")
	public String generateMatch(Model model) {
		List<Match> matchList = bGMIService.generteMatch();
		model.addAttribute("matchList", matchList);
		return "redirect:/admin/bgmi/the/startAdmin";
	}
	@GetMapping(value = "/addMatchForm")
	public String addMatchForm(Model model) {
		Match match = new Match();
		model.addAttribute("match", match);
		List<Team> teamList = bGMIService.getTeams();
		model.addAttribute("teamList", teamList);
		return "add_match";
	}
	@PostMapping(value = "/addMatch")
	public String addMatch(@ModelAttribute("match") Match match) {
		bGMIService.addMatch(match);
		return "redirect:/admin/bgmi/the/startAdmin";
	}
	
	@GetMapping(value = "/matchTableAdmin")
	public String matchTableAdmin(Model model) {
		List<Match> matchList = bGMIService.getMatchList();
		model.addAttribute("matchList", matchList);
		return "match_admin";
	}
	@GetMapping(value = "/addDateTimeFrom/{id}")
	public String addDateTimeFrom(@PathVariable ( value = "id") long id, Model model) {
		Match match = bGMIService.getMatch(id);
		model.addAttribute("match", match);
		return "date_time";
	}
	@PostMapping(value = "/addDateTime")
	public String addDateTime(@ModelAttribute("match") Match match) {
		bGMIService.addDateTime(match);
		return "redirect:/admin/bgmi/the/matchTableAdmin";
	}
	
	@GetMapping(value = "/startMatchFrom/{id}")
	public String startMatchFrom(@PathVariable ( value = "id") long id, Model model) {
		Match match = bGMIService.getMatch(id);
		model.addAttribute("match", match);
		return "start_match";
	}
	@PostMapping(value = "/startMatch")
	public String startMatch(@ModelAttribute("match") Match match) {
		bGMIService.startMatch(match);
		return "redirect:/admin/bgmi/the/matchTableAdmin";
	}
	
	@GetMapping(value = "/addResultFrom/{id}")
	public String addResultFrom(@PathVariable ( value = "id") long id, Model model) {
		Match match = bGMIService.getMatch(id);
		model.addAttribute("match", match);
		return "add_result";
	}
	@PostMapping(value = "/addResult")
	public String addResult(@ModelAttribute("match") Match match) {
		bGMIService.addResult(match);
		return "redirect:/admin/bgmi/the/matchTableAdmin";
	}
	@GetMapping(value = "/removeResultFrom/{id}")
	public String removeResultFrom(@PathVariable ( value = "id") long id, Model model) {
		bGMIService.removeResult(id);
		Match match = bGMIService.getMatch(id);
		model.addAttribute("match", match);
		return "add_result";
	}
	@GetMapping(value = "/timeTable")
	public String timeTable( Model model) {
		List<Match> matchList = bGMIService.getMatchList();
		model.addAttribute("matchList", matchList);
		return "match_user";
	}
	@GetMapping(value = "/pointTable")
	public String pointTable( Model model) {
		List<Team> teamList = bGMIService.getTeams();
		model.addAttribute("teamList", teamList);
		return "point_table";
	}
}

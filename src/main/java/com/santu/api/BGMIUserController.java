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
@RequestMapping(value = "/team")
public class BGMIUserController {

	@Autowired
	private BGMIService bGMIService;

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

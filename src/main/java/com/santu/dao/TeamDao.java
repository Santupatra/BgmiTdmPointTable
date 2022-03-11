package com.santu.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.santu.model.Team;

@Repository
public interface TeamDao extends MongoRepository<Team, Long> {


	List<Team> findAllByMatchWin();

}

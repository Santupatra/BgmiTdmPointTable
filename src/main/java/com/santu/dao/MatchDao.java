package com.santu.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.santu.model.Match;

@Repository
public interface MatchDao extends MongoRepository<Match, Long> {

}

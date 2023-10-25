package com.lz.football_management.dao;


import com.lz.football_management.entity.Team;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TeamDao {
    List<Team> findAllTeams();

    int addTeam(Team team);

    Team findTeamById(int id);

    int updateTeam(Team team);

    List<Team> selectTeamsByName(@Param("name") String name);

    int deleteTeamById(int id);

}

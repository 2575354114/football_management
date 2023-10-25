package com.lz.football_management.dao;

import com.lz.football_management.entity.Player;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PlayerDao {

    List<Player> findAllPlayers();

    // 根据ID查询球员
    Player findPlayerById(int id);

    // 添加球员
    int addPlayer(Player player);

    // 更新球员信息
    int updatePlayer(Player player);

    // 根据ID删除球员
    int deletePlayerById(int id);


    //根据姓名查询球员

    public List<Player> selectPlayersByName(@Param("name") String name);



    /**
     * 查询所有球员，并包含所属队伍信息
     *
     * @return 所有球员与所属队伍信息的列表
     */
    List<Player> findAllPlayersWithTeam();


    List<Player> findPlayersByTeamId(int teamId);

    void deletePlayersByTeamId(int teamId);
}

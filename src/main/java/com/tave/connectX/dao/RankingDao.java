package com.tave.connectX.dao;

import com.tave.connectX.dto.ranking.GetRankingDto;
import com.tave.connectX.dto.ranking.ReturnRankingDto;
import com.tave.connectX.dto.ranking.UpdateRankingDto;
import com.tave.connectX.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class RankingDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RankingDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetRankingDto> getRankings() {
        String sql = "select row_number() over (order by p.points DESC, p.victory DESC, p.defeat ASC) as ranking, victory, defeat, draw, name, U.profile from Percentage as p" +
                "    join User U on U.user_idx = p.user_idx" +
                "            order by p.points DESC, p.victory DESC, p.defeat ASC";

        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            GetRankingDto getRankingDto = new GetRankingDto();
            getRankingDto.setRanking(rs.getInt("ranking"));
            getRankingDto.setVictory(rs.getInt("victory"));
            getRankingDto.setDefeat(rs.getInt("defeat"));
            getRankingDto.setDraw(rs.getInt("draw"));
            getRankingDto.setName(rs.getString("name"));
            getRankingDto.setPicture(rs.getString("profile"));
            return getRankingDto;
        });
    }

    public GetRankingDto getRanking(User user) {
        String sql = "SELECT ranking, victory, defeat, draw, name, profile" +
                " FROM (" +
                "    SELECT U.user_idx, ROW_NUMBER() OVER (ORDER BY p.points DESC, p.victory DESC, p.defeat ASC) AS ranking, victory, defeat, draw, name, U.profile" +
                "    FROM Percentage AS p" +
                "    JOIN User U ON U.user_idx = p.user_idx" +
                "    ORDER BY p.points DESC, p.victory DESC, p.defeat ASC" +
                " ) AS subquery" +
                " WHERE user_idx = ?";

        return this.jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            GetRankingDto getRankingDto = new GetRankingDto();
            getRankingDto.setRanking(rs.getInt("ranking"));
            getRankingDto.setVictory(rs.getInt("victory"));
            getRankingDto.setDefeat(rs.getInt("defeat"));
            getRankingDto.setDraw(rs.getInt("draw"));
            getRankingDto.setName(rs.getString("name"));
            getRankingDto.setPicture(rs.getString("profile"));
            return getRankingDto;
        }, user.getUserIdx());

    }

    public ReturnRankingDto updateRanking(UpdateRankingDto updateRankingDto) {
        String updateSql = "update Percentage" +
                " set victory = victory + ?, defeat = defeat + ?, draw = draw + ?, points = points + ?" +
                " where user_idx = ?";
        Object[] params = {updateRankingDto.getVictory(), updateRankingDto.getDefeat(), updateRankingDto.getDraw(), updateRankingDto.getPoint(), updateRankingDto.getUserIdx()};
        this.jdbcTemplate.update(updateSql, params);

        String selectSql = "select user_idx, victory, defeat, draw, points from Percentage where user_idx = ?";
        return this.jdbcTemplate.queryForObject(selectSql, (rs, rowNum) -> {
            ReturnRankingDto resultDto = new ReturnRankingDto();
            resultDto.setVictory(rs.getInt("victory"));
            resultDto.setDefeat(rs.getInt("defeat"));
            resultDto.setDraw(rs.getInt("draw"));
            resultDto.setPoint(rs.getInt("points"));
            return resultDto;
        }, updateRankingDto.getUserIdx());
    }

    public ReturnRankingDto initRanking(UpdateRankingDto updateRankingDto) {
        String insertSql = "insert into Percentage values(?,?,?,?,?);" ;
        Object[] params = {updateRankingDto.getUserIdx(),updateRankingDto.getVictory(), updateRankingDto.getDefeat(), updateRankingDto.getDraw(), updateRankingDto.getPoint()};
        this.jdbcTemplate.update(insertSql, params);

        String selectSql = "select user_idx, victory, defeat, draw, points from Percentage where user_idx = ?";
        return this.jdbcTemplate.queryForObject(selectSql, (rs, rowNum) -> {
            ReturnRankingDto resultDto = new ReturnRankingDto();
            resultDto.setVictory(rs.getInt("victory"));
            resultDto.setDefeat(rs.getInt("defeat"));
            resultDto.setDraw(rs.getInt("draw"));
            resultDto.setPoint(rs.getInt("points"));
            return resultDto;
        }, updateRankingDto.getUserIdx());
    }
}

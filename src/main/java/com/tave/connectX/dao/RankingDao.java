package com.tave.connectX.dao;

import com.tave.connectX.dto.ranking.GetRankingDto;
import com.tave.connectX.dto.ranking.ReturnRankingDto;
import com.tave.connectX.dto.ranking.UpdateRankingDto;
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


    public List<GetRankingDto> getRanking() {
        String sql = "select name from Percentage as p" +
                "    join User U on U.user_idx = p.user_idx" +
                "            order by p.points DESC, p.victory DESC, p.defeat ASC";

        return this.jdbcTemplate.query(sql, (rs, rowNum) -> {
            GetRankingDto getRankingDto = new GetRankingDto();
            getRankingDto.setName(rs.getString("name"));
            return getRankingDto;
        });
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
}

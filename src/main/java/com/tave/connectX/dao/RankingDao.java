package com.tave.connectX.dao;

import com.tave.connectX.entity.ranking.GetRankingDto;
import com.tave.connectX.entity.ranking.UpdateRankingDto;
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

    public void updateRanking(UpdateRankingDto updateRankingDto) {
        String sql = "update Percentage" +
                " set victory = victory + ?, defeat = defeat + ?, points = points + ?" +
                " where user_idx = ?";
        Object[] params = {updateRankingDto.getVictory(), updateRankingDto.getDefeat(), updateRankingDto.getPoint(), updateRankingDto.getUserIdx()};

        this.jdbcTemplate.update(sql, params);
    }
}

package com.example.demo.dao;

import com.example.demo.entity.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RelationDto {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public RelationDto(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addRelation(int text_main_id, int related_text_id) {
        String sql = "INSERT INTO `relation`(`text_main_id`, `related_text_id`) VALUES (?, ?), (?, ?)";
        return jdbcTemplate.update(sql, text_main_id, related_text_id, related_text_id, text_main_id);
    }

    public int delRelation(int textMainId, int relatedTextId) {
        String sql = "DELETE FROM `relation` WHERE (`text_main_id` = ? and `related_text_id` = ?) or (`text_main_id` = ? and `related_text_id` = ?) ";
        return jdbcTemplate.update(sql, textMainId, relatedTextId,
                relatedTextId, textMainId);
    }


    public int delByTextMainId(int textMainId) {
        String sql = "DELETE FROM `relation` WHERE `text_main_id` = ? or `related_text_id` = ?";
        return jdbcTemplate.update(sql, textMainId, textMainId);
    }

    public List<Relation> selectByTextMainId(int textMainId) {
        String sql = "SELECT `id`, `text_main_id`, `related_text_id` , `create_time` FROM `relation` where `text_main_id` = ?";
        return jdbcTemplate.query(sql, new RowMapper<Relation>() {
            @Override
            public Relation mapRow(ResultSet rs, int rowNum) throws SQLException {
                Relation relation = new Relation();
                relation.setId(rs.getInt("id"));
                relation.setTextMainId(rs.getInt("text_main_id"));
                relation.setRelatedTextId(rs.getInt("related_text_id"));
                relation.setCreateTime(rs.getTimestamp("create_time"));
                return relation;
            }
        }, textMainId);
    }
}

package com.example.demo.dao;

import com.example.demo.entity.TextLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TextLabelDto {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public TextLabelDto(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addTextLabel(TextLabel textLabel) {
        String sql = "INSERT INTO `text_label`(`text_main_id`, `label_id`) VALUES (?, ?)";
        return jdbcTemplate.update(sql, textLabel.getTextMainId(), textLabel.getLabelId());
    }

    public int delById(TextLabel textLabel) {
        String sql = "DELETE FROM `text_label` WHERE `id` = ? ";
        return jdbcTemplate.update(sql, textLabel.getId());
    }

    public int delByTextMainId(int textMainId) {
        String sql = "DELETE FROM `text_label` WHERE `text_main_id` = ? ";
        return jdbcTemplate.update(sql, textMainId);
    }

    public int delByTextMainIdAndLabelId(TextLabel textLabel) {
        String sql = "DELETE FROM `text_label` WHERE `text_main_id` = ? and `label_id` = ? ";
        return jdbcTemplate.update(sql, textLabel.getTextMainId(), textLabel.getLabelId());
    }

    public List<TextLabel> selectByTextMainId(int id) {
        String sql = "SELECT `id`, `text_main_id`, `label_id`  FROM `text_label` where `text_main_id` = ?";
        return jdbcTemplate.query(sql, new RowMapper<TextLabel>() {
            @Override
            public TextLabel mapRow(ResultSet rs, int rowNum) throws SQLException {
                TextLabel textLabel = new TextLabel();
                textLabel.setId(rs.getInt("id"));
                textLabel.setTextMainId(rs.getInt("text_main_id"));
                textLabel.setLabelId(rs.getInt("label_id"));
                return textLabel;
            }
        }, id);
    }

    public List<TextLabel> selectByLabelId(int labelId) {
        String sql = "SELECT `id`, `text_main_id`, `label_id`  FROM `text_label` where `label_id` = ?";
        return jdbcTemplate.query(sql, new RowMapper<TextLabel>() {
            @Override
            public TextLabel mapRow(ResultSet rs, int rowNum) throws SQLException {
                TextLabel textLabel = new TextLabel();
                textLabel.setId(rs.getInt("id"));
                textLabel.setTextMainId(rs.getInt("text_main_id"));
                textLabel.setLabelId(rs.getInt("label_id"));
                return textLabel;
            }
        }, labelId);
    }

    public List<TextLabel> selectByLabelIds(String labelIds) {
        String sql = "SELECT `id`, `text_main_id`, `label_id`  FROM `text_label` where `label_id` in (" + labelIds + ")";
        return jdbcTemplate.query(sql, new RowMapper<TextLabel>() {
            @Override
            public TextLabel mapRow(ResultSet rs, int rowNum) throws SQLException {
                TextLabel textLabel = new TextLabel();
                textLabel.setId(rs.getInt("id"));
                textLabel.setTextMainId(rs.getInt("text_main_id"));
                textLabel.setLabelId(rs.getInt("label_id"));
                return textLabel;
            }
        });
    }
}

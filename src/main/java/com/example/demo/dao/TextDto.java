package com.example.demo.dao;

import com.example.demo.entity.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class TextDto {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public TextDto(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Text> selectById(int id) {
        String sql = "select `id`, `title`, `content` ,`format_id`, `mark`, `create_time`  from `text` where id = ?";
        return jdbcTemplate.query(sql, new RowMapper<Text>() {
            @Override
            public Text mapRow(ResultSet rs, int rowNum) throws SQLException {
                Text text = new Text();

                text.setId(rs.getInt("id"));
                text.setTitle(rs.getString("title"));
                text.setContent(rs.getString("content"));
                text.setFormatId(rs.getInt("format_id"));
                text.setMark(rs.getInt("mark"));
                text.setCreateTime(rs.getTimestamp("create_time"));
                return text;
            }
        }, id);
    }

    public List<Text> selectByTitle(String likeContent) {
        String sql = "select `id`, `title`, `content` ,`format_id`, `mark`, `create_time`  from `text` where `title` like ?  LIMIT 1000 ";
        return jdbcTemplate.query(sql, new RowMapper<Text>() {
            @Override
            public Text mapRow(ResultSet rs, int rowNum) throws SQLException {
                Text text = new Text();

                text.setId(rs.getInt("id"));
                text.setTitle(rs.getString("title"));
                text.setContent(rs.getString("content"));
                text.setFormatId(rs.getInt("format_id"));
                text.setMark(rs.getInt("mark"));
                text.setCreateTime(rs.getTimestamp("create_time"));
                return text;
            }
        }, "%" + likeContent + "%");
    }

    public List<Text> selectByContent(String likeContent) {
        String sql = "select `id`, `title`, `content` ,`format_id`, `mark`, `create_time`  from `text` where `content` like ?  LIMIT 1000 ";
        return jdbcTemplate.query(sql, new RowMapper<Text>() {
            @Override
            public Text mapRow(ResultSet rs, int rowNum) throws SQLException {
                Text text = new Text();

                text.setId(rs.getInt("id"));
                text.setTitle(rs.getString("title"));
                text.setContent(rs.getString("content"));
                text.setFormatId(rs.getInt("format_id"));
                text.setMark(rs.getInt("mark"));
                text.setCreateTime(rs.getTimestamp("create_time"));
                return text;
            }
        }, "%" + likeContent + "%");
    }


    public List<Text> selectByIds(String textIds) {
        String sql = "select `id`, `title`, `content` ,`format_id`, `mark`, `create_time`  from `text` where `id` in ( " + textIds + " )";
        return jdbcTemplate.query(sql, new RowMapper<Text>() {
            @Override
            public Text mapRow(ResultSet rs, int rowNum) throws SQLException {
                Text text = new Text();

                text.setId(rs.getInt("id"));
                text.setTitle(rs.getString("title"));
                text.setContent(rs.getString("content"));
                text.setFormatId(rs.getInt("format_id"));
                text.setMark(rs.getInt("mark"));
                text.setCreateTime(rs.getTimestamp("create_time"));
                return text;
            }
        });
    }


    public int updateById(Text text) {
        String sql = "UPDATE `text` SET `title` = ? , `content` = ? WHERE `id` = ?";
        return jdbcTemplate.update(sql, text.getTitle(), text.getContent(), text.getId());
    }

    public int addText(Text text) {
        String sql = "INSERT INTO `text`(`title`, `content`) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                                    PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                    ps.setString(1, text.getTitle());
                                    ps.setString(2, text.getContent());
                                    return ps;
                                }
                            },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

    //删除text 记得删除干净Relation TextLabel关联表  Comment评论表
    public int delById(int id) {
        String sql = "DELETE FROM `text` WHERE `id` = ?";
        return jdbcTemplate.update(sql, id);
    }
}

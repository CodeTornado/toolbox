package com.example.demo.dao;

import com.example.demo.entity.Comment;
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
public class CommentDto {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public CommentDto(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addComment(Comment comment) {
        String sql = "INSERT INTO `comment`(`text_main_id`, `content`) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                                    PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                    ps.setInt(1, comment.getTextMainId());
                                    ps.setString(2, comment.getContent());
                                    return ps;
                                }
                            },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int updateById(Comment comment) {
        String sql = "UPDATE `comment` SET `content` = ? WHERE `id` = ?";
        return jdbcTemplate.update(sql, comment.getContent(), comment.getId());
    }

    public int delById(Comment comment) {
        String sql = "DELETE FROM `comment` WHERE `id` = ?";
        return jdbcTemplate.update(sql, comment.getId());
    }

    public int delByTextMainId(int textMainId) {
        String sql = "DELETE FROM `comment` WHERE `text_main_id` = ?";
        return jdbcTemplate.update(sql, textMainId);
    }

    public List<Comment> selectByTextMainId(int id) {
        String sql = "SELECT `id`, `text_main_id`, `content` , `create_time` FROM `comment` where `text_main_id` = ? ";
        return jdbcTemplate.query(sql, new RowMapper<Comment>() {
            @Override
            public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setTextMainId(rs.getInt("text_main_id"));
                comment.setContent(rs.getString("content"));
                comment.setCreateTime(rs.getTimestamp("create_time"));

                return comment;
            }
        }, id);
    }
}

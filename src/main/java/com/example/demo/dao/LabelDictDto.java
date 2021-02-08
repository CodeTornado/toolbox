package com.example.demo.dao;

import com.example.demo.entity.LabelDict;
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
public class LabelDictDto {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public LabelDictDto(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<LabelDict> selectById(LabelDict labelDict) {
        String sql = "select `id`, `name` from `label_dict` where id = ?";
        return jdbcTemplate.query(sql, new RowMapper<LabelDict>() {
            @Override
            public LabelDict mapRow(ResultSet rs, int rowNum) throws SQLException {
                LabelDict labelDict = new LabelDict();

                labelDict.setId(rs.getInt("id"));
                labelDict.setName(rs.getString("name"));
                return labelDict;
            }
        }, labelDict.getId());
    }

    public List<LabelDict> selectByName(String labelName) {
        String sql = "select `id`, `name` from `label_dict` where `name` = ?";
        return jdbcTemplate.query(sql, new RowMapper<LabelDict>() {
            @Override
            public LabelDict mapRow(ResultSet rs, int rowNum) throws SQLException {
                LabelDict labelDict = new LabelDict();

                labelDict.setId(rs.getInt("id"));
                labelDict.setName(rs.getString("name"));
                return labelDict;
            }
        }, labelName);
    }

    public List<LabelDict> selectByLikeName(String likeName) {
        String sql = "select `id`, `name` from `label_dict` where `name` like  ? ";
        return jdbcTemplate.query(sql, new RowMapper<LabelDict>() {
            @Override
            public LabelDict mapRow(ResultSet rs, int rowNum) throws SQLException {
                LabelDict labelDict = new LabelDict();

                labelDict.setId(rs.getInt("id"));
                labelDict.setName(rs.getString("name"));
                return labelDict;
            }
        }, "%" + likeName + "%");
    }

    public int addLabelDict(String labelName) {
        String sql = "INSERT INTO `label_dict`(`name`) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
                                @Override
                                public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                                    PreparedStatement ps = (PreparedStatement) conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                    ps.setString(1, labelName);
                                    return ps;
                                }
                            },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int updateById(LabelDict labelDict) {
        String sql = "UPDATE `label_dict` SET `name` = ? WHERE `id` = ?";
        return jdbcTemplate.update(sql, labelDict.getName(), labelDict.getId());
    }

    public int deleteByName(LabelDict labelDict) {
        String sql = "DELETE FROM `myroot`.`label_dict` WHERE `name` = ?";
        return jdbcTemplate.update(sql, labelDict.getName());
    }

    public List<LabelDict> selectByIds(String labelIds) {
        String sql = "select `id`, `name` from `label_dict` where id in (" + labelIds + ")";
        return jdbcTemplate.query(sql, new RowMapper<LabelDict>() {
            @Override
            public LabelDict mapRow(ResultSet rs, int rowNum) throws SQLException {
                LabelDict labelDict = new LabelDict();

                labelDict.setId(rs.getInt("id"));
                labelDict.setName(rs.getString("name"));
                return labelDict;
            }
        });
    }
}

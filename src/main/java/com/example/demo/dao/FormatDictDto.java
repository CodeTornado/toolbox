package com.example.demo.dao;

import com.example.demo.entity.FormatDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class FormatDictDto {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public FormatDictDto(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addFormatDict(FormatDict formatDict) {
        String sql = "INSERT INTO `format_dict`(`name`) VALUES (?)";
        return jdbcTemplate.update(sql, formatDict.getName());
    }

    public List<FormatDict> selectAll() {
        String sql = "select `id`, `name` from `format_dict`";
        return jdbcTemplate.query(sql, new RowMapper<FormatDict>() {
            @Override
            public FormatDict mapRow(ResultSet rs, int rowNum) throws SQLException {
                FormatDict formatDict = new FormatDict();

                formatDict.setId(rs.getInt("id"));
                formatDict.setName(rs.getString("name"));
                return formatDict;
            }
        });
    }

    public List<FormatDict> selectById(FormatDict formatDict) {
        String sql = "select `id`, `name` from `format_dict` where id = ?";
        return jdbcTemplate.query(sql, new RowMapper<FormatDict>() {
            @Override
            public FormatDict mapRow(ResultSet rs, int rowNum) throws SQLException {
                FormatDict formatDict = new FormatDict();

                formatDict.setId(rs.getInt("id"));
                formatDict.setName(rs.getString("name"));
                return formatDict;
            }
        }, formatDict.getId());
    }
}

package com.hillel.notesapp.util;

import java.sql.ResultSet;

public interface RowMapper<T> {

    T map(ResultSet resultSet);

}

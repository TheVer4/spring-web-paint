package com.example.springwebpaint.repos;

import com.example.springwebpaint.domain.Board;
import org.springframework.data.repository.CrudRepository;

public interface BoardRepo extends CrudRepository<Board, Long> {
    Board findById(Integer id);
//    Board findByUser_id(Integer user_id);
}

package com.example.springwebpaint.repos;

import com.example.springwebpaint.domain.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrawingRepo extends JpaRepository<Drawing, Long> {
    Iterable<Drawing> findByBoard_id(Integer board_id);

    Drawing findByBoard_idAndId(Integer board_id, Long drawing_id);
}

package cz.cvut.iarylser.dao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequest {
    private Long eventId;
    private Long userId;
}

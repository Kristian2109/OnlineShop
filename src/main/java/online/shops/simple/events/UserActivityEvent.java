package online.shops.simple.events;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityEvent {
    private String eventType;
    private Long userId;
    private String username;
    private LocalDateTime registrationDateTime;
} 
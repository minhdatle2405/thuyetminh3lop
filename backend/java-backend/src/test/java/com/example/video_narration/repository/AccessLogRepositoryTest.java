
package com.example.video_narration.repository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.video_narration.entity.AccessLog;

@DataJpaTest
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE
)
class AccessLogRepositoryTest {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Test
    void saveAndFindAccessLog() {
        AccessLog accessLog = new AccessLog();

        accessLog.setIpAddress("127.0.0.1");
        accessLog.setUserAgent("Chrome");

        AccessLog saved = accessLogRepository.saveAndFlush(accessLog);

        assertThat(saved.getAccessId()).isNotNull();

        AccessLog found = accessLogRepository
                .findById(saved.getAccessId())
                .orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getIpAddress()).isEqualTo("127.0.0.1");
        assertThat(found.getUserAgent()).isEqualTo("Chrome");
    }

    @Test
    void countActiveAccessLogs() {
        AccessLog accessLog = new AccessLog();

        accessLog.setIpAddress("127.0.0.1");
        accessLog.setUserAgent("Chrome");

        accessLogRepository.saveAndFlush(accessLog);

        long count = accessLogRepository.countByLastSeenAtAfter(
                LocalDateTime.now().minusMinutes(1)
        );

        assertThat(count).isEqualTo(1);
    }
    
@Test
void updateHeartbeat() {
    AccessLog accessLog = new AccessLog();
    accessLog.setIpAddress("127.0.0.1");
    accessLog.setUserAgent("Chrome");

    AccessLog saved = accessLogRepository.saveAndFlush(accessLog);

    LocalDateTime newTime = LocalDateTime.now();

    saved.setLastSeenAt(newTime);
    accessLogRepository.saveAndFlush(saved);

    AccessLog updated = accessLogRepository
            .findById(saved.getAccessId())
            .orElse(null);

    assertThat(updated).isNotNull();
    assertThat(updated.getLastSeenAt())
            .isEqualTo(newTime);
}

}

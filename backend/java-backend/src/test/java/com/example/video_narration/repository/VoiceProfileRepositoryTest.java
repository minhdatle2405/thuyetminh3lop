
package com.example.video_narration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.video_narration.entity.VoiceProfile;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class VoiceProfileRepositoryTest {

    @Autowired
    private VoiceProfileRepository voiceProfileRepository;

    @Test
    void saveAndFindVoiceProfile() {
        // 1. Tạo hồ sơ giọng đọc
        VoiceProfile voice = new VoiceProfile();
        voice.setVoiceName("Test Voice");
        voice.setLanguageCode("vi-VN");
        voice.setProviderName("Test Provider");
        voice.setProviderVoiceId("test-voice-001");
        voice.setIsActive(true);

        // 2. Lưu vào database
        VoiceProfile saved =
                voiceProfileRepository.saveAndFlush(voice);

        // 3. Kiểm tra dữ liệu đã lưu
        
        assertThat(saved.getVoiceId()).isNotNull();

        VoiceProfile found = voiceProfileRepository
            .findById(saved.getVoiceId())
            .orElseThrow();


        assertThat(found.getVoiceName()).isEqualTo("Test Voice");
        assertThat(found.getLanguageCode()).isEqualTo("vi-VN");
        assertThat(found.getProviderName()).isEqualTo("Test Provider");
        assertThat(found.getProviderVoiceId())
                .isEqualTo("test-voice-001");
        assertThat(found.getIsActive()).isTrue();

    }
}

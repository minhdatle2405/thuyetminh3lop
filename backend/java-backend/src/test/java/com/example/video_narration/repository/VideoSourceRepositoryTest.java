
package com.example.video_narration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.example.video_narration.entity.VideoSource;

@DataJpaTest
@org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase(
    replace = org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace.NONE
)
class VideoSourceRepositoryTest {

    @Autowired
    private VideoSourceRepository videoSourceRepository;

    @Test
    void saveAndFindVideoSource() {
        VideoSource video = new VideoSource();
        video.setSourceType(VideoSource.SourceType.YOUTUBE);
        video.setOriginalName("test-video");
        video.setSourceUrl("https://www.youtube.com/watch?v=example");

        VideoSource saved = videoSourceRepository.saveAndFlush(video);

        assertThat(saved.getVideoId()).isNotNull();

        VideoSource found = videoSourceRepository
                .findById(saved.getVideoId())
                .orElseThrow();

        assertThat(found.getOriginalName()).isEqualTo("test-video");
        assertThat(found.getSourceType())
                .isEqualTo(VideoSource.SourceType.YOUTUBE);
    }
}

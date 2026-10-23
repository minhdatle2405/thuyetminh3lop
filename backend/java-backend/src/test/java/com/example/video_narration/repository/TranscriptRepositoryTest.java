
package com.example.video_narration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.video_narration.entity.ProcessingConfig;
import com.example.video_narration.entity.Transcript;
import com.example.video_narration.entity.TranslationJob;
import com.example.video_narration.entity.VideoSource;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class TranscriptRepositoryTest {

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Autowired
    private TranslationJobRepository translationJobRepository;

    @Autowired
    private VideoSourceRepository videoSourceRepository;

    @Autowired
    private ProcessingConfigRepository processingConfigRepository;

    @Test
    void saveAndFindTranscript() {
        // Tạo nguồn video
        VideoSource video = new VideoSource();
        video.setSourceType(VideoSource.SourceType.UPLOAD);
        video.setOriginalName("test-video.mp4");
        video.setSourceFilePath("uploads/test-video.mp4");
        video = videoSourceRepository.saveAndFlush(video);

        // Tạo cấu hình xử lý
        ProcessingConfig config = new ProcessingConfig();
        config.setConfigName("Test Config");
        config.setSourceLanguage("English");
        config.setTargetLanguage("Vietnamese");
        config.setSubtitleFormat("SRT");
        config.setAudioFormat("MP3");
        config = processingConfigRepository.saveAndFlush(config);

        // Tạo công việc xử lý
        TranslationJob job = new TranslationJob();
        job.setVideoSource(video);
        job.setProcessingConfig(config);
        job.setStatus(TranslationJob.JobStatus.PENDING);
        job.setProgressPercent((short) 0);
        job = translationJobRepository.saveAndFlush(job);

        // Tạo transcript
        Transcript transcript = new Transcript();
        transcript.setTranslationJob(job);
        transcript.setLanguage("English");
        transcript.setFullText("Hello world. This is a test.");

        Transcript saved = transcriptRepository.saveAndFlush(transcript);

        // Kiểm tra dữ liệu
        assertThat(saved.getTranscriptId()).isNotNull();

        Transcript found = transcriptRepository
                .findById(saved.getTranscriptId())
                .orElseThrow();

        assertThat(found.getLanguage()).isEqualTo("English");
        assertThat(found.getFullText())
                .isEqualTo("Hello world. This is a test.");
        assertThat(found.getTranslationJob().getJobId())
                .isEqualTo(job.getJobId());
    }
}

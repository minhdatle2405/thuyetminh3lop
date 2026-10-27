
package com.example.video_narration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.video_narration.entity.ProcessingConfig;
import com.example.video_narration.entity.Transcript;
import com.example.video_narration.entity.TranscriptSegment;
import com.example.video_narration.entity.TranslationJob;
import com.example.video_narration.entity.VideoSource;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class TranscriptSegmentRepositoryTest {

    @Autowired
    private TranscriptSegmentRepository transcriptSegmentRepository;

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Autowired
    private TranslationJobRepository translationJobRepository;

    @Autowired
    private VideoSourceRepository videoSourceRepository;

    @Autowired
    private ProcessingConfigRepository processingConfigRepository;

    @Test
    void saveAndFindTranscriptSegment() {
        // 1. Tạo nguồn video
        VideoSource video = new VideoSource();
        video.setSourceType(VideoSource.SourceType.UPLOAD);
        video.setOriginalName("test-video.mp4");
        video.setSourceFilePath("uploads/test-video.mp4");
        video = videoSourceRepository.saveAndFlush(video);

        // 2. Tạo cấu hình xử lý
        ProcessingConfig config = new ProcessingConfig();
        config.setConfigName("Test Config");
        config.setSourceLanguage("English");
        config.setTargetLanguage("Vietnamese");
        config.setSubtitleFormat("SRT");
        config.setAudioFormat("MP3");
        config = processingConfigRepository.saveAndFlush(config);

        // 3. Tạo công việc xử lý
        TranslationJob job = new TranslationJob();
        job.setVideoSource(video);
        job.setProcessingConfig(config);
        job.setStatus(TranslationJob.JobStatus.PENDING);
        job.setProgressPercent((short) 0);
        job = translationJobRepository.saveAndFlush(job);

        // 4. Tạo transcript
        Transcript transcript = new Transcript();
        transcript.setTranslationJob(job);
        transcript.setLanguage("English");
        transcript.setFullText("Hello world.");
        transcript = transcriptRepository.saveAndFlush(transcript);

        // 5. Tạo một đoạn transcript
        TranscriptSegment segment = new TranscriptSegment();
        segment.setTranscript(transcript);
        segment.setSequenceNo(1);
        segment.setStartMs(0L);
        segment.setEndMs(2000L);
        segment.setOriginalText("Hello world.");

        TranscriptSegment saved =
                transcriptSegmentRepository.saveAndFlush(segment);

        // 6. Truy vấn lại và kiểm tra
        assertThat(saved.getTranscriptSegId()).isNotNull();

        TranscriptSegment found = transcriptSegmentRepository
                .findById(saved.getTranscriptSegId())
                .orElseThrow();

        assertThat(found.getSequenceNo()).isEqualTo(1);
        assertThat(found.getStartMs()).isEqualTo(0L);
        assertThat(found.getEndMs()).isEqualTo(2000L);
        assertThat(found.getOriginalText()).isEqualTo("Hello world.");
        assertThat(found.getTranscript().getTranscriptId())
                .isEqualTo(transcript.getTranscriptId());
    }
}

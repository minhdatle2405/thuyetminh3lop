
package com.example.video_narration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.video_narration.entity.ProcessingConfig;
import com.example.video_narration.entity.SubtitleSegment;
import com.example.video_narration.entity.Transcript;
import com.example.video_narration.entity.TranscriptSegment;
import com.example.video_narration.entity.Translation;
import com.example.video_narration.entity.TranslationJob;
import com.example.video_narration.entity.VideoSource;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class SubtitleSegmentRepositoryTest {

    @Autowired
    private SubtitleSegmentRepository subtitleSegmentRepository;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private TranslationJobRepository translationJobRepository;

    @Autowired
    private VideoSourceRepository videoSourceRepository;

    @Autowired
    private ProcessingConfigRepository processingConfigRepository;

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Autowired
    private TranscriptSegmentRepository transcriptSegmentRepository;

    @Test
    void saveAndFindSubtitleSegment() {
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

        // 4. Tạo bản dịch
        Translation translation = new Translation();
        translation.setTranslationJob(job);
        translation.setSourceLanguage("English");
        translation.setTargetLanguage("Vietnamese");
        translation.setStatus(Translation.TranslationStatus.COMPLETED);
        translation = translationRepository.saveAndFlush(translation);

        // 5. Tạo bản ghi transcript
        Transcript transcript = new Transcript();
        transcript.setTranslationJob(job);
        transcript.setLanguage("English");
        transcript.setFullText("Hello world.");
        transcript = transcriptRepository.saveAndFlush(transcript);

        // 6. Tạo một đoạn transcript
        TranscriptSegment transcriptSegment = new TranscriptSegment();
        transcriptSegment.setTranscript(transcript);
        transcriptSegment.setSequenceNo(1);
        transcriptSegment.setStartMs(0L);
        transcriptSegment.setEndMs(2000L);
        transcriptSegment.setOriginalText("Hello world.");
        transcriptSegment =
                transcriptSegmentRepository.saveAndFlush(transcriptSegment);

        // 7. Tạo phụ đề
        SubtitleSegment subtitle = new SubtitleSegment();
        subtitle.setTranslation(translation);
        subtitle.setTranscriptSegment(transcriptSegment);
        subtitle.setSequenceNo(1);
        subtitle.setStartMs(0L);
        subtitle.setEndMs(2000L);
        subtitle.setOriginalText("Hello world.");
        subtitle.setTranslatedText("Xin chào thế giới.");

        SubtitleSegment saved =
                subtitleSegmentRepository.saveAndFlush(subtitle);

        // 8. Kiểm tra dữ liệu đã lưu
        assertThat(saved.getSubtitleId()).isNotNull();

        SubtitleSegment found = subtitleSegmentRepository
                .findById(saved.getSubtitleId())
                .orElseThrow();

        assertThat(found.getSequenceNo()).isEqualTo(1);
        assertThat(found.getStartMs()).isEqualTo(0L);
        assertThat(found.getEndMs()).isEqualTo(2000L);
        assertThat(found.getOriginalText()).isEqualTo("Hello world.");
        assertThat(found.getTranslatedText())
                .isEqualTo("Xin chào thế giới.");
        assertThat(found.getTranslation().getTranslationId())
                .isEqualTo(translation.getTranslationId());
        assertThat(found.getTranscriptSegment().getTranscriptSegId())
                .isEqualTo(transcriptSegment.getTranscriptSegId());
    }
}

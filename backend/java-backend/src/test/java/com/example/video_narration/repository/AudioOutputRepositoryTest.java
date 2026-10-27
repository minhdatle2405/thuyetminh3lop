
package com.example.video_narration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.example.video_narration.entity.AudioOutput;
import com.example.video_narration.entity.ProcessingConfig;
import com.example.video_narration.entity.TranslationJob;
import com.example.video_narration.entity.VideoSource;
import com.example.video_narration.entity.VoiceProfile;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class AudioOutputRepositoryTest {

    @Autowired
    private AudioOutputRepository audioOutputRepository;

    @Autowired
    private TranslationJobRepository translationJobRepository;

    @Autowired
    private VideoSourceRepository videoSourceRepository;

    @Autowired
    private ProcessingConfigRepository processingConfigRepository;

    @Autowired
    private VoiceProfileRepository voiceProfileRepository;

    @Test
    void saveAndFindAudioOutput() {
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

        // 3. Tạo hồ sơ giọng đọc
        VoiceProfile voice = new VoiceProfile();
        voice.setVoiceName("Test Voice");
        voice.setLanguageCode("vi-VN");
        voice.setProviderName("Test Provider");
        voice.setProviderVoiceId("test-voice-001");

        voice = voiceProfileRepository.saveAndFlush(voice);

        // 4. Tạo công việc dịch
        TranslationJob job = new TranslationJob();
        job.setVideoSource(video);
        job.setProcessingConfig(config);
        job.setStatus(TranslationJob.JobStatus.PENDING);
        job.setProgressPercent((short) 0);

        job = translationJobRepository.saveAndFlush(job);

        // 5. Tạo đầu ra âm thanh
        AudioOutput audio = new AudioOutput();
        audio.setTranslationJob(job);
        audio.setVoiceProfile(voice);
        audio.setFileName("test-audio.mp3");
        audio.setFilePath("output/test-audio.mp3");
        audio.setFormat("MP3");
        audio.setDurationMs(60000L);

        AudioOutput saved = audioOutputRepository.saveAndFlush(audio);

        // 6. Kiểm tra dữ liệu đã lưu
        assertThat(saved.getAudioId()).isNotNull();

        AudioOutput found = audioOutputRepository
                .findById(saved.getAudioId())
                .orElseThrow();

        assertThat(found.getFileName()).isEqualTo("test-audio.mp3");
        assertThat(found.getFilePath()).isEqualTo("output/test-audio.mp3");
        assertThat(found.getFormat()).isEqualTo("MP3");
        assertThat(found.getDurationMs()).isEqualTo(60000L);
        assertThat(found.getTranslationJob().getJobId())
                .isEqualTo(job.getJobId());
        assertThat(found.getVoiceProfile().getVoiceId())
                .isEqualTo(voice.getVoiceId());
    }
}

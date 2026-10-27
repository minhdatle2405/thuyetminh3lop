
package com.example.video_narration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "VOICE_PROFILE",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_voice_provider_id",
            columnNames = {"provider_name", "provider_voice_id"}
        )
    }
)
public class VoiceProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voice_id")
    private Long voiceId;

    @Column(name = "voice_name", nullable = false, length = 100)
    private String voiceName;

    @Column(name = "language_code", nullable = false, length = 20)
    private String languageCode;

    @Column(name = "provider_name", nullable = false, length = 100)
    private String providerName;

    @Column(name = "provider_voice_id", length = 255)
    private String providerVoiceId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public VoiceProfile() {}

    public Long getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(Long voiceId) {
        this.voiceId = voiceId;
    }

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderVoiceId() {
        return providerVoiceId;
    }

    public void setProviderVoiceId(String providerVoiceId) {
        this.providerVoiceId = providerVoiceId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

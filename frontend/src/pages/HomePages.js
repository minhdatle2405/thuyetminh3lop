import React, { useState, useEffect } from 'react';
import * as narrationApi from '../services/api';
import ControlPanel from '../components/ControlPanel';
import AudioPlayer from '../components/AudioPlayer';
import Subtitles from '../components/Subtitles';

export default function HomePages() {
  const [isRunning, setIsRunning] = useState(false);
  const [statusMsg, setStatusMsg] = useState({ text: '', type: 'info' });
  const [videoUrl, setVideoUrl] = useState('');
  const [settings, setSettings] = useState({
    targetLanguage: 'vi',
    voice: 'Nam',
    showSubtitles: true,
    originalVolume: 50,
    narrationVolume: 80,
  });

  useEffect(() => {
    narrationApi.getSettings()
      .then((res) => {
        if (res.data?.data) {
          setSettings(res.data.data);
          if (typeof res.data.data.isRunning === 'boolean') {
            setIsRunning(res.data.data.isRunning);
          }
        }
      })
      .catch(() => {
        setStatusMsg({ text: '⚠️ Chưa kết nối được Backend (hãy chạy sever.js cổng 3001).', type: 'warn' });
      });
  }, []);

  const handleToggleRunning = async () => {
    try {
      if (!isRunning) {
        const res = await narrationApi.startNarration(settings);
        setIsRunning(true);
        setStatusMsg({ text: res.data.message || 'Đã khởi chạy thuyết minh!', type: 'success' });
      } else {
        const res = await narrationApi.stopNarration();
        setIsRunning(false);
        setStatusMsg({ text: res.data.message || 'Đã dừng thuyết minh.', type: 'info' });
      }
    } catch (err) {
      setStatusMsg({ text: err.response?.data?.message || 'Lỗi khi gửi yêu cầu lên server!', type: 'error' });
    }
  };

  const handleChangeSetting = async (field, value) => {
    setSettings((prev) => ({ ...prev, [field]: value }));

    if (isRunning) {
      try {
        if (field === 'targetLanguage') await narrationApi.updateLanguage(value);
        if (field === 'voice') await narrationApi.updateVoice(value);
        if (field === 'originalVolume') await narrationApi.updateOriginalVolume(value);
        if (field === 'narrationVolume') await narrationApi.updateNarrationVolume(value);
      } catch (err) {
        console.error('Lỗi cập nhật cấu hình tức thì:', err);
      }
    }
  };

  const handleToggleSubtitles = async (checked) => {
    setSettings((prev) => ({ ...prev, showSubtitles: checked }));
    if (isRunning) {
      try {
        await narrationApi.updateSubtitles(checked);
      } catch (err) {
        console.error(err);
      }
    }
  };

  return (
    <div style={{ maxWidth: '800px', margin: '0 auto', padding: '0 16px' }}>
      <div style={{ textAlign: 'center', marginBottom: '24px' }}>
        <h1 style={{ color: '#0f172a', margin: '0 0 8px 0' }}>Hệ Thống Thuyết Minh & Dịch Video 3 Lớp</h1>
        <p style={{ color: '#64748b', margin: 0 }}>Đồ án môn Công nghệ Phần mềm - Nhóm 9</p>
      </div>

      {/* Box tải video lên theo thực thể VIDEO_SOURCE */}
      <div style={{ backgroundColor: '#ffffff', borderRadius: '10px', padding: '20px', marginBottom: '20px', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
        <h3 style={{ margin: '0 0 12px 0', color: '#1e293b', fontSize: '18px' }}>📹 Nguồn Video Đầu Vào (Video Source)</h3>
        <div style={{ display: 'flex', gap: '10px' }}>
          <input 
            type="text" 
            placeholder="Dán URL video hoặc đường dẫn file (mp4, mkv)..." 
            value={videoUrl}
            onChange={(e) => setVideoUrl(e.target.value)}
            style={{ flex: 1, padding: '10px', borderRadius: '6px', border: '1px solid #cbd5e1', outline: 'none' }}
          />
          <button style={{ padding: '10px 18px', backgroundColor: '#3b82f6', color: '#fff', border: 'none', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer' }}>
            Nạp Video
          </button>
        </div>
      </div>

      {statusMsg.text && (
        <div style={{
          padding: '12px 16px',
          borderRadius: '8px',
          marginBottom: '20px',
          fontWeight: '500',
          backgroundColor: statusMsg.type === 'success' ? '#dcfce7' : statusMsg.type === 'error' ? '#fee2e2' : '#e0f2fe',
          color: statusMsg.type === 'success' ? '#15803d' : statusMsg.type === 'error' ? '#b91c1c' : '#0369a1'
        }}>
          {statusMsg.text}
        </div>
      )}

      <ControlPanel settings={settings} onChangeSetting={handleChangeSetting} />
      
      <AudioPlayer 
        isRunning={isRunning} 
        settings={settings} 
        onToggleRunning={handleToggleRunning} 
        onChangeSetting={handleChangeSetting} 
      />

      <Subtitles 
        showSubtitles={settings.showSubtitles} 
        onToggleSubtitles={handleToggleSubtitles} 
      />
    </div>
  );
}
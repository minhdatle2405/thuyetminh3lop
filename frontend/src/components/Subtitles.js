import React from 'react';

export default function Subtitles({ showSubtitles, onToggleSubtitles }) {
  // Dữ liệu mẫu minh họa theo các cột start_ms, original_text, translated_text trong CSDL
  const sampleSegments = [
    { start: '00:01', orig: 'Xin chào mọi người đến với bài thuyết trình đồ án.', trans: 'Hello everyone, welcome to the project presentation.' },
    { start: '00:05', orig: 'Hôm nay nhóm chúng tôi sẽ demo chức năng dịch video.', trans: 'Today our team will demonstrate the video translation feature.' },
    { start: '00:10', orig: 'Hệ thống đang thuyết minh bằng giọng AI tự động.', trans: 'The system is narrating with automatic AI voice.' }
  ];

  return (
    <div style={{ backgroundColor: '#ffffff', borderRadius: '10px', padding: '20px', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '2px solid #e2e8f0', paddingBottom: '8px', marginBottom: '16px' }}>
        <h3 style={{ margin: 0, color: '#1e293b', fontSize: '18px' }}>💬 Phụ Đề & Bản Dịch (Subtitles)</h3>
        <label style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer', fontSize: '14px', fontWeight: 'bold', color: '#2563eb' }}>
          <input 
            type="checkbox" 
            checked={showSubtitles} 
            onChange={(e) => onToggleSubtitles(e.target.checked)} 
          />
          Bật hiển thị phụ đề
        </label>
      </div>

      {showSubtitles ? (
        <div>
          <div style={{ backgroundColor: '#0f172a', color: '#38bdf8', padding: '14px', borderRadius: '8px', textAlign: 'center', fontWeight: '500', marginBottom: '16px' }}>
            🎬 Phụ đề realtime: [Hello everyone, welcome to the project presentation.]
          </div>
          <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '13px' }}>
            <thead>
              <tr style={{ backgroundColor: '#f8fafc', textAlign: 'left', borderBottom: '1px solid #cbd5e1' }}>
                <th style={{ padding: '8px' }}>Mốc thời gian</th>
                <th style={{ padding: '8px' }}>Văn bản gốc</th>
                <th style={{ padding: '8px' }}>Bản dịch AI</th>
              </tr>
            </thead>
            <tbody>
              {sampleSegments.map((row, idx) => (
                <tr key={idx} style={{ borderBottom: '1px solid #f1f5f9' }}>
                  <td style={{ padding: '8px', color: '#64748b' }}>{row.start}</td>
                  <td style={{ padding: '8px' }}>{row.orig}</td>
                  <td style={{ padding: '8px', color: '#059669', fontWeight: '500' }}>{row.trans}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ) : (
        <div style={{ color: '#94a3b8', textAlign: 'center', padding: '20px 0', fontStyle: 'italic' }}>
          Đã tắt hiển thị phụ đề.
        </div>
      )}
    </div>
  );
}
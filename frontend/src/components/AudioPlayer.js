import React from 'react';

export default function AudioPlayer({ isRunning, settings, onToggleRunning, onChangeSetting }) {
  return (
    <div style={{ backgroundColor: '#ffffff', borderRadius: '10px', padding: '20px', marginBottom: '20px', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
      <h3 style={{ margin: '0 0 16px 0', color: '#1e293b', fontSize: '18px', borderBottom: '2px solid #e2e8f0', paddingBottom: '8px' }}>
        🎛️ Bảng Điều Khiển Âm Lượng & Tác Vụ
      </h3>

      <div style={{ textAlign: 'center', marginBottom: '20px' }}>
        <button 
          onClick={onToggleRunning}
          style={{
            padding: '12px 32px',
            fontSize: '16px',
            fontWeight: 'bold',
            borderRadius: '8px',
            border: 'none',
            color: '#ffffff',
            cursor: 'pointer',
            backgroundColor: isRunning ? '#ef4444' : '#10b981',
            boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
            transition: '0.2s'
          }}
        >
          {isRunning ? '⏹️ DỪNG TIẾN TRÌNH (STOP)' : '▶️ BẮT ĐẦU THUYẾT MINH (START)'}
        </button>
      </div>

      <div style={{ marginBottom: '14px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px', fontSize: '14px', fontWeight: '500' }}>
          <span>Âm lượng giọng video gốc:</span>
          <b>{settings.originalVolume}%</b>
        </div>
        <input 
          type="range" 
          min="0" 
          max="100" 
          value={settings.originalVolume} 
          onChange={(e) => onChangeSetting('originalVolume', Number(e.target.value))}
          style={{ width: '100%', accentColor: '#3b82f6' }}
        />
      </div>

      <div>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '4px', fontSize: '14px', fontWeight: '500' }}>
          <span>Âm lượng giọng đọc AI:</span>
          <b>{settings.narrationVolume}%</b>
        </div>
        <input 
          type="range" 
          min="0" 
          max="100" 
          value={settings.narrationVolume} 
          onChange={(e) => onChangeSetting('narrationVolume', Number(e.target.value))}
          style={{ width: '100%', accentColor: '#10b981' }}
        />
      </div>
    </div>
  );
}
import React from 'react';

export default function ControlPanel({ settings, onChangeSetting }) {
  return (
    <div style={{ backgroundColor: '#ffffff', borderRadius: '10px', padding: '20px', marginBottom: '20px', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}>
      <h3 style={{ margin: '0 0 16px 0', color: '#1e293b', fontSize: '18px', borderBottom: '2px solid #e2e8f0', paddingBottom: '8px' }}>
        ⚙️ Cấu Hình Thuyết Minh & Dịch Thuật
      </h3>
      
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '16px' }}>
        <div>
          <label style={{ display: 'block', fontWeight: '600', marginBottom: '6px', color: '#475569', fontSize: '14px' }}>
            Ngôn ngữ đích (Target Language):
          </label>
          <select 
            value={settings.targetLanguage} 
            onChange={(e) => onChangeSetting('targetLanguage', e.target.value)}
            style={{ width: '100%', padding: '10px', borderRadius: '6px', border: '1px solid #cbd5e1', outline: 'none' }}
          >
            <option value="vi">Tiếng Việt (vi)</option>
            <option value="en">English (en)</option>
            <option value="ja">Japanese (ja)</option>
          </select>
        </div>

        <div>
          <label style={{ display: 'block', fontWeight: '600', marginBottom: '6px', color: '#475569', fontSize: '14px' }}>
            Giọng đọc AI (Voice Profile):
          </label>
          <select 
            value={settings.voice} 
            onChange={(e) => onChangeSetting('voice', e.target.value)}
            style={{ width: '100%', padding: '10px', borderRadius: '6px', border: '1px solid #cbd5e1', outline: 'none' }}
          >
            <option value="Nam">Nam (Giọng Nam - Chuẩn)</option>
            <option value="Marin">Marin (Giọng Nữ - Nhẹ nhàng)</option>
          </select>
        </div>
      </div>
    </div>
  );
}
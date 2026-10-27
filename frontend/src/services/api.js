import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:3001/api/narration',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getSettings = () => API.get('/settings');
export const startNarration = (settings) => API.post('/start', settings);
export const stopNarration = () => API.post('/stop');
export const updateLanguage = (targetLanguage) => API.put('/language', { targetLanguage });
export const updateVoice = (voice) => API.put('/voice', { voice });
export const updateSubtitles = (showSubtitles) => API.put('/subtitles', { showSubtitles });
export const updateOriginalVolume = (originalVolume) => API.put('/volume/original', { originalVolume });
export const updateNarrationVolume = (narrationVolume) => API.put('/volume/narration', { narrationVolume });

export default API;
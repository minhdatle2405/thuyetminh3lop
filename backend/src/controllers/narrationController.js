const narrationService = require('../sevices/narrationService');

const ALLOWED_LANGUAGES = ['vi', 'en', 'ja'];
const ALLOWED_VOICES = ['Marin', 'Nam'];
const ALLOWED_ACTIONS = ['START', 'STOP'];

function sendError(res, status, message) {
  return res.status(status).json({ success: false, message });
}

function sendOk(res, message, data) {
  return res.status(200).json({ success: true, message, data });
}

function readString(value, fieldName) {
  if (value == null || String(value).trim() === '') {
    return { error: `Thiếu ${fieldName}.` };
  }
  return { value: String(value).trim() };
}

function parseTargetLanguage(value) {
  const parsed = readString(value, 'targetLanguage');
  if (parsed.error) return parsed;
  const language = parsed.value.toLowerCase();
  if (!ALLOWED_LANGUAGES.includes(language)) {
    return {
      error: `targetLanguage không hợp lệ. Cho phép: ${ALLOWED_LANGUAGES.join(', ')}.`,
    };
  }
  return { value: language };
}

function parseVoice(value) {
  const parsed = readString(value, 'voice');
  if (parsed.error) return parsed;
  const voice = ALLOWED_VOICES.find(
    (item) => item.toLowerCase() === parsed.value.toLowerCase()
  );
  if (!voice) {
    return {
      error: `voice không hợp lệ. Cho phép: ${ALLOWED_VOICES.join(', ')}.`,
    };
  }
  return { value: voice };
}

function parseShowSubtitles(value) {
  if (typeof value === 'boolean') return { value };
  if (value === 'true' || value === 'false') return { value: value === 'true' };
  return { error: 'showSubtitles phải là true hoặc false.' };
}

function parseVolume(value, fieldName) {
  const number = Number(value);
  if (!Number.isInteger(number) || number < 0 || number > 100) {
    return { error: `${fieldName} phải là số nguyên từ 0 đến 100.` };
  }
  return { value: number };
}

function parseAction(value) {
  const parsed = readString(value, 'action');
  if (parsed.error) return parsed;
  const action = parsed.value.toUpperCase();
  if (!ALLOWED_ACTIONS.includes(action)) {
    return { error: 'action không hợp lệ. Chỉ nhận START hoặc STOP.' };
  }
  return { value: action };
}

function parseFullSettings(body, { requireAll }) {
  const settings = {};

  if (requireAll || body.targetLanguage !== undefined) {
    const language = parseTargetLanguage(body.targetLanguage);
    if (language.error) return language;
    settings.targetLanguage = language.value;
  }

  if (requireAll || body.voice !== undefined) {
    const voice = parseVoice(body.voice);
    if (voice.error) return voice;
    settings.voice = voice.value;
  }

  if (requireAll || body.showSubtitles !== undefined) {
    const subtitles = parseShowSubtitles(body.showSubtitles);
    if (subtitles.error) return subtitles;
    settings.showSubtitles = subtitles.value;
  }

  if (requireAll || body.originalVolume !== undefined) {
    const originalVolume = parseVolume(body.originalVolume, 'originalVolume');
    if (originalVolume.error) return originalVolume;
    settings.originalVolume = originalVolume.value;
  }

  if (requireAll || body.narrationVolume !== undefined) {
    const narrationVolume = parseVolume(body.narrationVolume, 'narrationVolume');
    if (narrationVolume.error) return narrationVolume;
    settings.narrationVolume = narrationVolume.value;
  }

  return { value: settings };
}

async function handleAction(req, res) {
  try {
    const action = parseAction(req.body?.action);
    if (action.error) return sendError(res, 400, action.error);

    if (action.value === 'START') {
      return startNarration(req, res);
    }
    return stopNarration(req, res);
  } catch (error) {
    return sendError(res, 500, `Lỗi xử lý tại Server: ${error.message}`);
  }
}

async function startNarration(req, res) {
  try {
    const settings = parseFullSettings(req.body || {}, { requireAll: true });
    if (settings.error) return sendError(res, 400, settings.error);

    const data = narrationService.startProcess(settings.value);
    return sendOk(res, 'Đã khởi chạy hệ thống thuyết minh thành công!', data);
  } catch (error) {
    return sendError(res, 500, `Lỗi xử lý tại Server: ${error.message}`);
  }
}

async function stopNarration(req, res) {
  try {
    const data = narrationService.stopProcess();
    return sendOk(res, 'Đã dừng hệ thống thuyết minh.', data);
  } catch (error) {
    return sendError(res, 500, `Lỗi xử lý tại Server: ${error.message}`);
  }
}

async function updateTargetLanguage(req, res) {
  try {
    const language = parseTargetLanguage(req.body?.targetLanguage);
    if (language.error) return sendError(res, 400, language.error);

    const data = narrationService.updateSettings({ targetLanguage: language.value });
    return sendOk(res, 'Đã cập nhật ngôn ngữ thuyết minh.', data);
  } catch (error) {
    return sendError(res, 500, `Lỗi xử lý tại Server: ${error.message}`);
  }
}

async function updateVoice(req, res) {
  try {
    const voice = parseVoice(req.body?.voice);
    if (voice.error) return sendError(res, 400, voice.error);

    const data = narrationService.updateSettings({ voice: voice.value });
    return sendOk(res, 'Đã cập nhật giọng đọc.', data);
  } catch (error) {
    return sendError(res, 500, `Lỗi xử lý tại Server: ${error.message}`);
  }
}

async function updateSubtitles(req, res) {
  try {
    const subtitles = parseShowSubtitles(req.body?.showSubtitles);
    if (subtitles.error) return sendError(res, 400, subtitles.error);

    const data = narrationService.updateSettings({ showSubtitles: subtitles.value });
    return sendOk(res, 'Đã cập nhật trạng thái phụ đề.', data);
  } catch (error) {
    return sendError(res, 500, `Lỗi xử lý tại Server: ${error.message}`);
  }
}

async function updateOriginalVolume(req, res) {
  try {
    const volume = parseVolume(req.body?.originalVolume, 'originalVolume');
    if (volume.error) return sendError(res, 400, volume.error);

    const data = narrationService.updateSettings({ originalVolume: volume.value });
    return sendOk(res, 'Đã cập nhật âm lượng giọng gốc.', data);
  } catch (error) {
    return sendError(res, 500, `Lỗi xử lý tại Server: ${error.message}`);
  }
}

async function updateNarrationVolume(req, res) {
  try {
    const volume = parseVolume(req.body?.narrationVolume, 'narrationVolume');
    if (volume.error) return sendError(res, 400, volume.error);

    const data = narrationService.updateSettings({ narrationVolume: volume.value });
    return sendOk(res, 'Đã cập nhật âm lượng giọng thuyết minh.', data);
  } catch (error) {
    return sendError(res, 500, `Lỗi xử lý tại Server: ${error.message}`);
  }
}

async function getSettings(req, res) {
  try {
    const data = narrationService.getSettings();
    return sendOk(res, 'Lấy cấu hình thuyết minh thành công.', data);
  } catch (error) {
    return sendError(res, 500, `Lỗi xử lý tại Server: ${error.message}`);
  }
}

module.exports = {
  handleAction,
  startNarration,
  stopNarration,
  updateTargetLanguage,
  updateVoice,
  updateSubtitles,
  updateOriginalVolume,
  updateNarrationVolume,
  getSettings,
};

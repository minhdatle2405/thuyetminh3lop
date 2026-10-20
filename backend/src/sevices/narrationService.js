const settings = {
  isRunning: false,
  targetLanguage: 'vi',
  voice: 'Nam',
  showSubtitles: true,
  originalVolume: 50,
  narrationVolume: 80,
};

function snapshot() {
  return { ...settings };
}

function startProcess(config) {
  Object.assign(settings, config, { isRunning: true });
  return snapshot();
}

function stopProcess() {
  settings.isRunning = false;
  return snapshot();
}

function updateSettings(partial) {
  Object.assign(settings, partial);
  return snapshot();
}

function getSettings() {
  return snapshot();
}

module.exports = {
  startProcess,
  stopProcess,
  updateSettings,
  getSettings,
};

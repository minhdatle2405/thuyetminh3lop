const express = require('express');
const narrationController = require('../controllers/narrationController');

const router = express.Router();

router.get('/settings', narrationController.getSettings);
router.post('/action', narrationController.handleAction);
router.post('/start', narrationController.startNarration);
router.post('/stop', narrationController.stopNarration);
router.put('/language', narrationController.updateTargetLanguage);
router.put('/voice', narrationController.updateVoice);
router.put('/subtitles', narrationController.updateSubtitles);
router.put('/volume/original', narrationController.updateOriginalVolume);
router.put('/volume/narration', narrationController.updateNarrationVolume);

module.exports = router;

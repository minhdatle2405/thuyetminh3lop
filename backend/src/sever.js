const express = require('express');
const cors = require('cors');
const narrationRoute = require('./routes/narrationRoute');

const app = express();
const PORT = process.env.PORT || 3001;

app.use(cors());
app.use(express.json());

app.use('/api/narration', narrationRoute);

app.get('/health', (req, res) => {
  res.json({ success: true, message: 'Backend is running.' });
});

app.listen(PORT, () => {
  console.log(`Backend listening on http://localhost:${PORT}`);
});

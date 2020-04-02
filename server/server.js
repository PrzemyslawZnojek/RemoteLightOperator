const path = require('path');
const express = require('express');
const http = require('http');
const socket = require('socket.io');

const Controller = require('./controller');
const fb = require('./firebase');
const auth = require('./middleware/authentication');

const PORT = 5000;

const app = express();
const server = http.createServer(app);
const io = socket(server);

const ctrl = new Controller({lamp_gpio: 0});

app.use("/static", express.static(path.join(__dirname, "public")));
app.use(express.json());
app.use(express.urlencoded());

app.set('views', './views');
app.set('view engine', 'pug');

app.get('/', auth, async (req, res) => {
  res.render('index', {
    ...ctrl.dumpAll(),
    user: fb.dumpUser(),
  });
});

app.get('/sensor/:mac', auth, (req, res) => {
  const sensorData = ctrl.dumpSensor(req.params.mac);

  if (!sensorData) {
    return res.status(404).send('Not Found');
  }

  res.render('sensor', sensorData);
});

app.get('/login', (req, res) => {
  res.render('login');
});

app.post('/login', async (req, res) => {
  const creds = req.body;

  if (!creds || !creds.email || !creds.password) {
    return res.status(400).send('Bad Request');
  }

  const {success, error} = await fb.signIn({
    email: creds.email,
    password: creds.password
  });

  if (!success) {
    return res.render('login', {error});
  }

  await fb.readUserConfiguration();
  fb.onUserConfigChange(userConfig => {
    ctrl.applyUserConfig(userConfig);
    io.emit('config-changed');
  });

  res.status(200).redirect('/');
});

app.get('/logout', auth, (req, res) => {
  fb.signOut();
  res.status(200).redirect('/login');
});

app.get('/api/version', (req, res) => {
  res.json({version: '0.1'});
});

app.post('/api/sensor_entry', (req, res) => {
  const data = req.body;
  
  if (!data || !data.mac || !data.measurement) {
    return res.status('400').send('Bad Request');
  }

  if (!ctrl.hasSensor(data.mac)) {
    ctrl.registerSensor({
      ip: req.ip,
      mac: data.mac
    });
  }

  ctrl.addSensorEntry(data);

  res.status(200).end();
});

server.listen(PORT, () => {
  console.log(`IoT server listening on port ${PORT}`);
});
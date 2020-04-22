const path = require('path');
const express = require('express');
const http = require('http');
const socket = require('socket.io');
const mqtt = require('mqtt');

const Config = require('./config');
const Controller = require('./controller');
const fb = require('./firebase');
const auth = require('./middleware/authentication');

const PORT = 5000;

const app = express();
const server = http.createServer(app);
const io = socket(server);

const cfg = new Config();
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
    config: cfg.dump()
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

  const userConfig = await fb.readUserConfiguration();
  await cfg.setupLocation();

  ctrl.applyConfig({
    ...userConfig,
    sunriseTime: cfg.sunriseTime,
    sunsetTime: cfg.sunsetTime,
  });

  fb.onUserConfigChange(userConfig => {

    ctrl.applyConfig({
      ...userConfig,
      sunriseTime: cfg.sunriseTime,
      sunsetTime: cfg.sunsetTime
    });

    io.emit('user-config-changed');
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

const client = mqtt.connect("mqtt://192.168.0.24", {
  port: 1883,
  keepalive: 60
});

client.on('connect', () => {
  console.log('Connected to esp8266');
  client.subscribe('esp8266');
});

client.on('message', (topic, payload) => {
  let data;

  try {
    data = payload.toJSON().data;

    if (!data) {
      return;
    }
  } catch (error) {
    console.error(error);
    return;
  }


  const mac = '1f:aa:03:4d:ef:c9';
  const color = {red: data.red, green: data.green, blue: data.blue};
  const brightness = data.clear_light;

  if (!ctrl.hasSensor(mac)) {
    ctrl.registerSensor({
      mac
    });
  }

  ctrl.addSensorEntry({mac, color, brightness});
});

server.listen(PORT, () => {
  console.log(`IoT server listening on port ${PORT}`);
});


io.on('connection', socket => {
  socket.on('auto-location', async () => {
    cfg.setLocationAutoMode(data => {
      ctrl.applyConfig({
        ...fb.getCurrentUserConfig(),
        sunriseTime: data.sunriseTime,
        sunsetTime: data.sunsetTime,
        onReady: data => io.emit('set-lamp-config', {
          ...data,
          sunriseTime: `${data.sunriseTime.getHours()}:${data.sunriseTime.getMinutes()}`,
          sunsetTime: `${data.sunsetTime.getHours()}:${data.sunsetTime.getMinutes()}`,
          monitorStartTime: `${data.monitorStartTime.getHours()}:${data.monitorStartTime.getMinutes()}`,
          monitorStopTime: `${data.monitorStopTime.getHours()}:${data.monitorStopTime.getMinutes()}`
        })
      });

      io.emit('set-config-data', {
        ...data,
        sunriseTime: `${data.sunriseTime.getHours()}:${data.sunriseTime.getMinutes()}`,
        sunsetTime: `${data.sunsetTime.getHours()}:${data.sunsetTime.getMinutes()}`
      });
    });
  });
  
  socket.on('manual-location', () => {
    cfg.setLocationManualMode();
  });

  socket.on('get-location-from-address', ({address}) => {
    cfg.setLocationFromAddress(address, data => {
      ctrl.applyConfig({
        ...fb.getCurrentUserConfig(),
        sunriseTime: data.sunriseTime,
        sunsetTime: data.sunsetTime,
        onReady: data => io.emit('set-lamp-config', {
          ...data,
          sunriseTime: `${data.sunriseTime.getHours()}:${data.sunriseTime.getMinutes()}`,
          sunsetTime: `${data.sunsetTime.getHours()}:${data.sunsetTime.getMinutes()}`,
          monitorStartTime: `${data.monitorStartTime.getHours()}:${data.monitorStartTime.getMinutes()}`,
          monitorStopTime: `${data.monitorStopTime.getHours()}:${data.monitorStopTime.getMinutes()}`
        })
      });
      io.emit('set-config-data', {
        ...data,
        sunriseTime: `${data.sunriseTime.getHours()}:${data.sunriseTime.getMinutes()}`,
        sunsetTime: `${data.sunsetTime.getHours()}:${data.sunsetTime.getMinutes()}`,
      });
    });
  });
})
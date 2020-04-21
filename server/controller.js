const Sensor = require('./sensor');
const Lamp = require('./lamp');

const SENSOR_TTL = 10;
const MONITORING_INTERVAL = 1000;

class Controller {
  constructor({lamp_gpio}) {
    this.sensorsMap = {};
    this.lamp = new Lamp(lamp_gpio);

    setInterval(this.monitorSensors.bind(this), MONITORING_INTERVAL);
  }

  monitorSensors() {
    Object.values(this.sensorsMap).forEach(sensor => sensor.decrementTTL());
  }

  hasSensor(mac) {
    return !!this.sensorsMap[mac];
  }

  registerSensor({ip, mac}) {
    this.sensorsMap[mac] = new Sensor({ip, mac});
  }

  addSensorEntry({mac, measurement}) {
    this.sensorsMap[mac].addEntry({measurement});

    const sensors = Object.values(this.sensorsMap);
    const sum = sensors.reduce((acc, sensor) => {
      return acc + sensor.getLastEntry().measurement;
    }, 0);

    this.lamp.adjust(sum / sensors.length);
  }

  applyConfig({irradiationTime, lightOptions, forcedState, sunriseTime, sunsetTime, onReady}) {
    const {red, green, blue, brightness} = (lightOptions || {});

    this.lamp.setConfig({
      forcedState,
      brightness,
      color: {red, green, blue},
      irradiationTime,
      sunriseTime,
      sunsetTime,
      onReady
    });
  }

  dumpAll() {
    return {
      lamp: this.lamp.dump(),
      sensors: Object.values(this.sensorsMap).map(sensor => sensor.dump())
    };
  }

  dumpSensor(mac) {
    if (!this.sensorsMap[mac]) {
      return null;
    }

    return this.sensorsMap[mac].dump();
  }
}

module.exports = Controller;
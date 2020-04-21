const request = require('request');

const THRESHOLD = 150;

class Lamp {
  static MODE_MANUAL = 'MANUAL';
  static MODE_AUTO = 'AUTO';

  constructor(gpio) {
    this.gpio = gpio;
    this.enabled = false;
    this.active = false;
    this.brightness = 1;
    this.color = {red: 0, green: 0, blue: 0};
    this.mode = Lamp.MODE_AUTO;

    this.schedule = null;

    this.monitorTimer = setInterval(() => {
      if (!this.schedule || !this.schedule.monitorStopTime || !this.schedule.monitorStartTime) {
        this.active = false;
        this.enabled = false;
        return;
      }

      const now = new Date();
      const {monitorStartTime, monitorStopTime} = this.schedule;


      let prevActive = this.active;

      if (now.getTime() <  monitorStartTime.getTime() || now > monitorStopTime.getTime()) {
        this.active = false;
      } else {
        this.active = true;
      }

      if (this.active !== prevActive) {
        this.sendToLamp();
      }
    }, 5000);
  }

  setMode(forcedState) {
    switch (forcedState) {
      case 'ON':
        this.mode = Lamp.MODE_MANUAL;
        this.enabled = true;
        break;

      case 'OFF':
        this.mode = Lamp.MODE_MANUAL;
        this.enabled = false;
        break;

      default:
        this.mode = Lamp.MODE_AUTO;
    }
  }

  setConfig({forcedState, color, brightness, irradiationTime, sunriseTime, sunsetTime, onReady}) {
    this.setMode(forcedState);

    this.color = color;

    if (this.mode === Lamp.MODE_MANUAL) {
      this.brightness = brightness;
    }

    let monitorStartTime = null;
    let monitorStopTime = null;

    const sunriseMs = sunriseTime.getTime();
    const sunsetMs = sunsetTime.getTime();
    const irradiationMs = irradiationTime * 60 * 1000;


    const dayTime = sunsetMs - sunriseMs;

    if (irradiationMs > dayTime) {
      monitorStartTime = sunriseTime;
      monitorStopTime = new Date(sunsetMs + irradiationMs);
    } else {
      const middle = Math.floor(sunriseMs + dayTime / 2);

      monitorStartTime = new Date(middle - irradiationMs / 2);
      monitorStopTime = new Date(middle + irradiationMs / 2);
    }

    if (forcedState === 'ON' && forcedState === 'OFF') {
      this.schedule = null;
    } else {
      this.schedule = {
        sunriseTime,
        sunsetTime,
        monitorStartTime,
        monitorStopTime,
      };
    }

    if (onReady) {
      onReady({
        ...this.schedule,
        enabled: this.enabled,
        mode: this.mode,
        brightness: this.brightness,
        color: this.color
      });
    }

    this.sendToLamp();
  }

  adjust(average_light) {
    if (!this.active) {
      return;
    }

    if (average_light > THRESHOLD) {
      this.enabled = false;
      this.brightness = 0;
    } else {
      this.enabled = true;
      this.brightness = 255 - average_light;
    }

    this.sendToLamp();
  }

  sendToLamp() {
    const body = {
      ...this.color,
      brightness: this.brightness
    };

    request('https://lintiest-siamese-8525.dataplicity.io/', {
      method: 'POST',
      json: true,
      body
    });
  }

  dump() {
    const {red, green, blue} = this.color;

    return {
      enabled: this.enabled,
      brightness: this.brightness,
      color: `rgb(${red}, ${green}, ${blue})`,
      mode: this.mode,
      schedule: this.schedule ? {
        sunriseTime: `${this.schedule.sunriseTime.getHours()}:${this.schedule.sunriseTime.getMinutes()}`,
        sunsetTime: `${this.schedule.sunsetTime.getHours()}:${this.schedule.sunsetTime.getMinutes()}`,
        monitorStartTime: `${this.schedule.monitorStartTime.getHours()}:${this.schedule.monitorStartTime.getMinutes()}`,
        monitorStopTime: `${this.schedule.monitorStopTime.getHours()}:${this.schedule.monitorStopTime.getMinutes()}`
      } : null,
    };
  }
}

module.exports = Lamp;
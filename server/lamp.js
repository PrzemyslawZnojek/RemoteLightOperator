class Lamp {
  static MODE_MANUAL = 'MANUAL';
  static MODE_AUTO = 'AUTO';

  constructor(gpio) {
    this.gpio = gpio;
    this.enabled = false;
    this.active = false;
    this.brightness = 1;
    this.color = {r: 0, b: 0, g: 0};
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


      if (now.getTime() <  monitorStartTime.getTime() || now > monitorStopTime.getTime()) {
        this.active = false;
      } else {
        this.active = true;
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

  setConfig({forcedState, irradiationTime, sunriseTime, sunsetTime, onReady}) {
    this.setMode(forcedState);

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
  }

  adjust(average_light) {
    if (!this.active) {
      return;
    }

    if (average_light > 50) {
      this.enabled = false;
    } else {
      console.log('DUPA', average_light, this.active);
      this.enabled = true;
    }
  }

  dump() {
    const {r, g, b} = this.color;

    const progress = (Date.now() - this.schedule.sunriseTime.getTime()) / (this.schedule.sunsetTime.getTime() - this.schedule.sunriseTime.getTime());

    return {
      enabled: this.enabled,
      brightness: this.brightness,
      color: `rgb(${r}, ${g}, ${b})`,
      mode: this.mode,
      schedule: this.schedule ? {
        sunriseTime: `${this.schedule.sunriseTime.getHours()}:${this.schedule.sunriseTime.getMinutes()}`,
        sunsetTime: `${this.schedule.sunsetTime.getHours()}:${this.schedule.sunsetTime.getMinutes()}`,
        monitorStartTime: `${this.schedule.monitorStartTime.getHours()}:${this.schedule.monitorStartTime.getMinutes()}`,
        monitorStopTime: `${this.schedule.monitorStopTime.getHours()}:${this.schedule.monitorStopTime.getMinutes()}`
      } : null,
      progress,
    };
  }
}

module.exports = Lamp;
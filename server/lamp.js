class Lamp {
  static MODE_MANUAL = 'MANUAL';
  static MODE_AUTO = 'AUTO';

  constructor(gpio) {
    this.gpio = gpio;
    this.enabled = false;
    this.brightness = 1;
    this.mode = Lamp.MODE_AUTO;
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

  dump() {
    return {
      enabled: this.enabled,
      brightness: this.brightness,
      mode: this.mode,
    };
  }
}

module.exports = Lamp;
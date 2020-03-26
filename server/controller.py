import threading
import time

from lamp import Lamp

LAMP_GPIO = 1

def every(delay, task):
  next_time = time.time() + delay
  while True:
    time.sleep(max(0, next_time - time.time()))
    try:
      task()
    except Exception:
      pass
    next_time += (time.time() - next_time) // delay * delay + delay

class Controller:
  '''Class responsible for managing devices'''

  def __init__(self):
    self.sensors = {}
    self.lamp = Lamp(LAMP_GPIO)

    self.monitor_thread = threading.Thread(target=lambda: every(1, self.monitor_sensors))
    self.monitor_thread.start()

  def monitor_sensors(self):
    for _, sensor in self.sensors.items():
      sensor.decrement_ttl()

  def add_sensor(self, sensor):
    self.sensors[sensor.mac] = sensor
  
  def get_sensor(self, mac):
    return self.sensors[mac] if mac in self.sensors else None

  def dump_data(self):
    data = {}
    data['sensors'] = {}

    for mac, sensor in self.sensors.items():
      data['sensors'][mac] = sensor.dump_data()

    data['lamp'] = self.lamp.dump_data()

    return data
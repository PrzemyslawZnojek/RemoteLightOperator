import time

MAX_TTL = 10

class Sensor:
  '''Class responsible for storing data about sensor'''

  def __init__(self, ip, mac):
    self.ip = ip
    self.mac = mac
    
    self.ttl = MAX_TTL
    self.alive = True
    self.entries = []

  def add_entry(self, measurement):
    self.entries.append((time.time(), measurement))
    self.ttl = MAX_TTL

  def decrement_ttl(self):
    if not self.alive:
      return

    self.ttl -= 1
    if self.ttl == 0:
      self.alive = False

  def dump_data(self):
    data = {
      'mac': self.mac,
      'ip': self.ip,
      'alive': self.alive,
      'ttl': self.ttl,
      'last_read_timestamp': time.strftime("%D %H:%M:%S", time.localtime(self.entries[-1][0])),
      'last_read_value': self.entries[-1][1],
      'last_100_reads_timestamps': [i for i, j in self.entries[-100:]],
      'last_100_reads_values': [j for i, j in self.entries[-100:]]
    }

    return data
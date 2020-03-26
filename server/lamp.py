
class Lamp:
  '''Class responsible for handling interaction with lamp'''

  def __init__(self, gpio):
    self.enabled = False
    self.brightness = 1

    self.gpio = gpio

  def adjust_brightness(self, brightness):
    self.brightness = brightness

  def on(self):
    self.enabled = True

  def off(self):
    self.enabled = False

  def dump_data(self):
    data = {
      'enabled': self.enabled,
      'brightness': self.brightness
    }

    return data
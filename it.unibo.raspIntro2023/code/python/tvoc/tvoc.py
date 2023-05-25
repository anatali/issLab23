#!/usr/bin/env python3

import time
import board
import busio
import adafruit_ccs811
 
i2c = busio.I2C(board.SCL, board.SDA)
ccs811 = adafruit_ccs811.CCS811(i2c)

# Wait for the sensor to be ready
while not ccs811.data_ready:
    pass
     
while True:
    # print("CO2: %1.0f PPM" % ccs811.eco2)
    # print("TVOC: %1.0f PPM" % ccs811.tvoc)
    # print("Temp: %0.1f C" % ccs811.temperature)
    # print("CO2: {} PPM, TVOC: {} PPB".format(ccs811.eco2, ccs811.tvoc))
    print(ccs811.tvoc)
    time.sleep(0.5)


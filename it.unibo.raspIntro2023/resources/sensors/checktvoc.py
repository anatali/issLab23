#!/usr/bin/env python3
'''
I2C VOC/eCO2 sensor

The CCS811 has a 'standard' hot-plate MOX sensor, as well as a small microcontroller that controls 
power to the plate, reads the analog voltage, and provides an I2C interface to read from.

a gas sensor that can detect a wide range of Volatile Organic Compounds (VOCs) 
(see https://en.wikipedia.org/wiki/Volatile_organic_compound
http://www.inive.org/medias/ECA/ECA_Report19.pdf
https://iaqscience.lbl.gov/voc-intro
https://leeduser.buildinggreen.com/forum/tvoc-or-not-tvoc
https://www.repcomsrl.com/wp-content/uploads/2017/06/Environmental_Sensing_VOC_Product_Brochure_EN.pdf
)
and is intended for indoor air quality monitoring.

It will return a Total Volatile Organic Compound (TVOC) reading 
and an equivalent carbon dioxide reading (eCO2) over I2C.


this sensor, like all VOC/gas sensors, has variability and to get precise measurements 
you will want to calibrate it against known sources!

Before use, you'll need to attach headers so you can plug in this sensor into a breadboard. 
Soldering is essential!
https://learn.adafruit.com/adafruit-guide-excellent-soldering

'''
import time
import board
import busio
import adafruit_ccs811
 
i2c    = busio.I2C(board.SCL, board.SDA)
ccs811 = adafruit_ccs811.CCS811(i2c)

# Wait for the sensor to be ready
while not ccs811.data_ready:
    pass
     
for k in range(6):
    print("CO2: %1.0f PPM" % ccs811.eco2)
    print("TVOC: %1.0f PPM" % ccs811.tvoc)
    # print("Temp: %0.1f C" % ccs811.temperature)   #NO MORE !!!!
    # print("CO2: {} PPM, TVOC: {} PPB".format(ccs811.eco2, ccs811.tvoc))
    #print(ccs811.tvoc)
    time.sleep(0.5)


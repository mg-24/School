#include <BMP085.h>
#include <Wire.h>
#define anemometer 2 // PIN 2
#define rain 3 // PIN 3
#define wind_speed 2.4 // hodnota za 1 hz - jedno cvaknutie
#define rain_fall 0.2794 // hodnota pri jednom preklopeni v Rain Gauge

volatile unsigned long anem_count = 0;
volatile unsigned long anem_last = 0;
volatile unsigned long anem_min = 0xffffffff;
volatile unsigned long rain_last = 0;
int wind_vane; // hodnota signalu z wind vain senzora
int humidity; // vlhkost vzduchu
int direct; // smer -> 1 - W, 2 - NW, 3 - N, 4 - SW, 5 - NE, 6 - S, 7 - SE, 8 - E
float radian; // radiany pri smere vetra
float rain_count; // pocitadlo mnozstva zrazok
const int measure_time = 5; // konstanta pri pocitani v ramci delay
int seconds;
int minutes;
int hours;
float wind = 0.0; // rychlost vetra
float temperature;
float pressure;
unsigned long time = 0; // celkovy prejdeny cas v milisekundach
BMP085 pressure_m; // zadefinovanie kniznice BMP085 pre tlak a teplotu

void setup() {
  time = millis();
  pinMode(anemometer, INPUT);
  pinMode(rain, INPUT_PULLUP);
  Serial.begin(38400);
  Wire.begin();
  pressure_m.bmp085Calibration(); 
}

void loop() {
  
  radian = windVane();
  anemometerRainGaugeMeasure();
  wind_vane = analogRead(A1); // hodnota signalu z wind vane senzora
  humidity = analogRead(A0); // hodnota so senzora vlhkosti vzduchu
  temperature = pressure_m.bmp085GetTemperature(); // ziskanie teploty
  pressure = pressure_m.bmp085GetPressure(); // ziskanie tlaku
  getHumidity(temperature);

    seconds = (time / 1000);
    minutes = (seconds / 60);
    hours = (minutes / 60);
    
    Serial.print("Measurements after ");
    Serial.print(seconds % 60);
    Serial.print(" seconds, ");
    Serial.print(minutes % 60);
    Serial.print(" minutes, ");
    Serial.print(hours);
    Serial.println(" hours");
    Serial.print("Average wind speed in 5 seconds: ");
    Serial.print(wind);       //Rychlost v km/h
    Serial.print(" km/h - ");
    Serial.print(wind / 3.6); //Rychlost v m/h
    Serial.println(" m/h");
    Serial.print("Humidity: ");
    Serial.print(humidity);
    Serial.println("%");
    Serial.print("Temperature: ");
    Serial.print(temperature, 2); //Teplota na dve desatinne miesta
    Serial.println("deg C");

    Serial.print("Pressure: ");
    Serial.print(pressure, 0); // Tlak
    Serial.print(" Pa - ");
    Serial.print((pressure / 100), 0);
    Serial.println(" HPa");

    if (wind > 0) {
      Serial.print("Wind direction: ");
    
      switch (direct) {
      case 1:
        Serial.println("West");
        break;
      case 2:
        Serial.println("North-West");
        break;
      case 3:
        Serial.println("North");
        break;
      case 4:
        Serial.println("South-West");
        break;
      case 5:
        Serial.println("North-East");
        break;
      case 6:
        Serial.println("South");
        break;
      case 7:
        Serial.println("South-East");
        break;
      case 8:
        Serial.println("East");
        break;
      default:
      break;
      }
    }
    
    Serial.print("Amount of rainfall: ");
    Serial.print(rain_count);
    Serial.println("mm");
    Serial.println("-------------------------------------------------------------");
    
}

void anemometerRainGaugeMeasure() {
  anem_count = 0;
  time = millis();
  attachInterrupt(digitalPinToInterrupt(anemometer), anemometerCount, RISING);
  attachInterrupt(digitalPinToInterrupt(rain), rainGaugeCount, FALLING);
  delay(1000 * measure_time);
  detachInterrupt(digitalPinToInterrupt(anemometer));
  detachInterrupt(digitalPinToInterrupt(rain));
  wind = (float)anem_count / (float)measure_time * wind_speed;
}

float windVane() {
  
  if (wind_vane <= 945 && wind_vane > 888) { // WEST
      direct = 1;
      return (4.71);     
    }
    
  if (wind_vane <= 888 && wind_vane > 786) { // NW
      direct = 2;
      return (5.50);     
    }
    
  if (wind_vane <= 786 && wind_vane > 631) { // N
      direct = 3;
      return (0);     
    }

  if (wind_vane <= 631 && wind_vane > 463) { // SW
      direct = 4;
      return (3.93);     
    }

  if (wind_vane <= 463 && wind_vane > 287) { // NE
      direct = 5;
      return (0.79);     
    } 

  if (wind_vane <= 287 && wind_vane > 184) { // S
      direct = 6;
      return (3.14);     
    }

  if (wind_vane <= 184 && wind_vane > 92) { // SE
      direct = 7;
      return (2.36);     
    }

  if (wind_vane <= 92 && wind_vane > 79) { // E
      direct = 8;
      return (1.57);     
    } 
}

void rainGaugeCount() {
    long thisTime = micros() - rain_last;
    rain_last = micros();
    if(thisTime > 500) {
      rain_count = rain_count + rain_fall;
    }
}

void anemometerCount() {
  long thisTime = micros() - anem_last;
  anem_last = micros();
  if(thisTime > 500) // takzvany "bounce" je kazdych 500 micro sekund a to by pocitalo navyse - treba ingnorovat
  {
    anem_count++;
    if(thisTime < anem_min) {
      anem_min = thisTime;
    }
 
  }
}

void getHumidity(float temperature) {
  if (temperature >= 23.1 && temperature < 30.1) {
    humidity = map(humidity, 200, 950, 90, 25);
    }
  if (temperature >= 20.1 && temperature < 23.1) {
    humidity = map(humidity, 300, 1000, 90, 25);
    }
  if (temperature >= 15.1 && temperature < 19.1) {
    humidity = map(humidity, 400, 1500, 90, 25);
    }
  if (temperature >= 10.1 && temperature < 15.1) {
    humidity = map(humidity, 470, 3000, 90, 25);
    }
  if (temperature >= 30.1 && temperature < 35.1) {
    humidity = map(humidity, 180, 880, 90, 25);
    }
  if (temperature >= 35.1 && temperature < 40.1) {
    humidity = map(humidity, 160, 780, 90, 25);
    }  
}

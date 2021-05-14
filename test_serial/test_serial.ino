int counter;
int MAX = 5;
int MIN = -5;

void setup() {
  // put your setup code here, to run once:
    Serial.begin(9600);
    pinMode(LED_BUILTIN, OUTPUT);

    counter = MIN;
}

void loop() {
  // put your main code here, to run repeatedly:

  if (counter < 0) {
    Serial.write("L");
//    Serial.println("-255");
//    Serial.print(String("-255").concat("\n"));
  }
  else {
    Serial.write("R");
//    Serial.println("255");
//    Serial.print(String("255").concat("\n"));
  }
  digitalWrite(LED_BUILTIN, HIGH);
  delay(200);
  digitalWrite(LED_BUILTIN, LOW);
  delay(200);

  counter += 1;

  if (counter > MAX) {
    counter = MIN;
  }
}

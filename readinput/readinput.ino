void setup() {
  // put your setup code here, to run once:

  pinMode(2, INPUT);
  pinMode(3, INPUT);

  Serial.begin(9600);

}

void loop() {
  // put your main code here, to run repeatedly:
  
  if (digitalRead(2) == LOW && digitalRead(3) == HIGH) {
    Serial.write("R");
  }
  else if (digitalRead(2) == HIGH && digitalRead(3) == LOW) {
    Serial.write("L");
  }
  else {
    Serial.write("0");
  }

  delay(16);
}

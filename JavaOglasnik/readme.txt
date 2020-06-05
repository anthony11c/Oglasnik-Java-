# JavaOglasnikProject (In Process)

Upute za pokretanje aplikacije (Koristeći Eclipse IDE)

1. Pokrenuti bazu podataka na način da odete u folder h2/bin/h2w.bat

2. Otvorit će se prozor u kojem odabirete redom sljedeće: 
	Saved Settings: Generic H2 (Server)
	Setting Name: Generic H2 (Server)
	Driver Class: org.h2.Driver
	JDBC URL: jdbc:h2:tcp://localhost/~/Desktop/JavaOglasnik/Java-NovaBaza
	User Name: student
	Password: student
	Sad se možete connectati...

3. Otvoriti projekt u Eclipse-u. Desni klik na folder projekta unutar Eclipse-a /Run As/Run Configurations

4. Odabrati Arguments tab te pod VM Arguments C/P sljedeće bez navodnika
"--module-path "C:\Users\#\Desktop\javafx-sdk-14.0.1\lib" --add-modules 
javafx.controls,javafx.fxml"

5. Putanja treba voditi do lib foldera unutar javafx-sdk (ono što ste skinuli iz koraka jedan).

6. Zatim unutar foldera projekta idemo do Main.java file-a na sljedeći način ->
  src/application/Main.java

7. Desnik klik na Main.java / Run As/ Java Application

8. To bi trebalo biti to. 

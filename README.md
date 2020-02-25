# SISTEMA DE MONITOREO CON SENSORES

#### COMO CORRER EL SISTEMA DE MONITOREO Y LA SIMULACIÓN
- En una terminal, correr el script que compila y levanta el monitor, pasandole por argumento los paramentros S y M.<br>
<br>Por ejemplo:
	
	`./start_monitor.sh 0.3 0.7`

- En otra terminal correr el script que simula el envio de mediciones de los sensores.<br>
En esta simulación los 4 sensores envian 30 mediciones cada uno, 2 por segundo.

	`./simulate_sensors.sh`

**Ubicación de los logs**<br>
Por default los logs se guardan en la carpeta `/tmp/monitor_logs`.<br>
Si se quiere cambiar la ubicación, modificar las variables _monitorfilename_ y _sensorsfilename_ del archivo `log4j2.yaml`.



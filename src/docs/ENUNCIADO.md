CENTRO UNIVERSITARIO DE OCCIDENTE  
DIVISIÓN DE CIENCIAS DE LA INGENIERÍA  
INTRODUCCIÓN A LA PROGRAMACION Y COMPUTACION 1  
PROYECTO 2  
SEGUNDO SEMESTRE 2026

# **Objetivos generales**

* Profundizar el conocimiento del lenguaje Java  
* Aplicar los conceptos recibidos en clase magistral y laboratorio.  
* Elaborar la lógica para la solución del problema planteado.

# **Objetivos específicos**

* Construir algoritmos para los requerimientos de la actividad.  
* Aplicar conocimientos avanzados de POO en Java.  
* Desarrollar interfaces gráficas con la biblioteca de Swing en Java.  
* Implementar y manejar adecuadamente hilos y archivos en Java.  
* Construir aplicaciones de escritorio complejas.  
* Implementar código limpio con buenas prácticas de programación.  
* Desarrollar manuales, diagramas y documentación adecuada.

# **Descripción de la actividad**

Uno de los clientes más importantes de Code & Bugs es un gran fan de la franquicia de juegos Plantas vs. Zombis, sin embargo, los zombis siempre fueron sus personajes preferidos,  por ello ha solicitado a los mejores programadores de Code & Bugs, construir un remake del juego llamado **Zombis vs. Plantas**, en el que él pueda estrategizar la partida desde el lado de los zombis y vencer a las plantas para lograr conseguir el cerebro protegido por la computadora.

## Inicio del juego

El juego funcionará obligatoriamente a través de una interfaz gráfica, que controlará el flujo completo de la interacción del usuario.

El sistema considera que múltiples jugadores pueden usar la misma máquina de modo que permite la creación de diferentes cuentas para guardar el progreso de cada jugador por separado. Al iniciar la aplicación el usuario deberá seleccionar una cuenta para poder continuar con el flujo de la aplicación, para ello podrá:

1. **Crear nueva cuenta:** Para ello se le pedirá su nombre o nickname de jugador, este debe ser único entre las cuentas registradas ya que será la forma de identificar cada cuenta. El nickname debe diferenciar entre mayúsculas y minúsculas, no admite caracteres especiales, solo alfanuméricos y debe tener al menos 1 carácter, 12 como máximo. Por cada cuenta creada se maneja una carpeta para las partidas guardadas y un archivo de estadísticas.

2. **Seleccionar cuenta existente:** La aplicación le presentará el listado de cuentas previamente creadas y el usuario podrá seleccionar cualquiera de ellas para continuar jugando, cargando automáticamente la información de la cuenta seleccionada.

## Menú principal

Una vez se haya seleccionado una cuenta, se le presentarán al usuario las siguientes opciones:

1. **Nueva partida:** Permite iniciar una nueva partida seleccionando un jardín y una dificultad.

2. **Reanudar partida guardada:** Muestra una lista de partidas que previamente el jugador pausó y guardó para ahora poder reanudar una de ellas, continuando exactamente en el estado en que se dejó.

3. **Estadísticas:** Muestra la *Sala de la fama* del juego, con estadísticas de la cuenta activa y el ranking global que compara las estadísticas principales de todas las cuentas registradas.

4. **Salir:** cierra la aplicación de manera controlada.

## Catálogo de plantas

1. ### Lanzaguisantes

   **Puntos salud:** 100  
   **Daño:** 20 por proyectil  
   **Cadencia:** 1 disparo por segundo  
   **Mecánica:** Evalúa si existe al menos un zombi en su carril, de ser así lanza un guisante que viaja horizontalmente sobre el carril hasta darle a un zombi o salir de la pantalla

2. ### Patatapum

   **Puntos salud:** 50  
   **Daño:** 100 por zombi en casilla  
   **Tiempo de preparación:** 10 segundos  
   **Mecánica:** Una vez plantada, Patatapum tiene que esperar el tiempo definido de preparación para poder ser activada, ambos estados (inactiva/activa) deben representarse gráficamente. Mientras está escondida, es vulnerable a que los zombis se la coman. Una vez activa, explota causando daño a todos los zombis que estén en la misma casilla que ella al momento de la explosión.

3. ### Hielaguisantes

   **Puntos salud:** 100  
   **Daño:** 15 por proyectil  
   **Cadencia:** 1 disparo cada 1.5 segundos  
   **Mecánica:** Similar al lanzaguisantes, lanza proyectiles congelantes. Al impactar a un zombi, además de restar HP, reduce la velocidad de movimiento del zombi en un 40% durante un periodo de 4 segundos. Si un zombi ya está bajo el efecto del congelamiento y vuelve a ser impactado por un guisante congelado no se ralentiza más, es decir, no perderá más velocidad pero si se reiniciará la duración del efecto, es decir, se empiezan a contar nuevamente los 4 segundos.

4. ### Nuez cáscara-rabias

   **Puntos salud:** 400  
   **Mecánica:** No ataca ni se mueve, solo bloquea el paso de los zombis (incluyendo los zombis saltadores de pértiga). Se debe modificar su apariencia gráfica según el porcentaje de vida restante: con salud entre 100 y 66% se ve intacta, entre 65 y 35% agrietada, con salud \< 35% se debe ver muy dañada.

5. ### Planta Carroñívora

   **Puntos salud:** 100  
   **Daño**: 200 (Solo si no logra tragar al zombi, es decir es un Zombistein)  
   **Mecánica:** Es una planta carnívora engulle a casi cualquier zombi entero que esté en su casilla o la casilla frente a ella, pero tarda 8 segundos en tragárselo, así que mientras digieren, otros zombis se las pueden comer. El zombi que tragan, básicamente muere al ser atrapado, no hay forma en que vuelva a salir de la planta y ya no puede hacer más daño. Debido a su tamaño y resistencia el Zombistein no puede ser tragado por esta planta por lo que solo le inflige daño.

6. ### Pinchohierba

   **Daño:** 10 cada 0.5 segundos  
   **Mecánica:** Es una planta que posee espinas en toda la parte superior las cuales infligen daño a los zombis que caminan sobre ella (que están en la misma casilla). No tiene puntos de salud ya que es inmune a los ataques de los zombis normales, estos no pueden comérsela. Solo puede ser destruida al ser aplastada por un Zombistein.

7. ### Bipetidora

   **Puntos de salud:** 100  
   **Daño:** 15 por proyectil  
   **Cadencia:** 1 disparo frontal cada segundo / 2 disparos traseros cada  segundo  
   **Mecánica:** Posee dos cabezas con capacidad de ataque en direcciones opuestas. Su cabeza frontal funciona como un Lanzaguisantes normal, disparando un proyectil hacia la zona de entrada de los zombis cuando detecta un enemigo en el carril. Su cabeza trasera entra en acción si un zombi logra rebasar la posición de la planta o aparece a sus espaldas ya sea por salto de garrocha, invocaciones o lanzamiento; en ese momento, la cabeza posterior dispara ráfagas dobles consecutivas hacia atrás para eliminar al zombi que ha traspasado a esta planta.

La velocidad de los proyectiles será definida por el desarrollador.

Al morir una planta esta desaparece del jardín y su casilla queda nuevamente disponible para ser ocupada por una nueva planta.

## Catálogo de Zombis

1. ### Zombi

   **Puntos salud:** 100  
   **Daño:** 15 por segundo  
   **Costo:** 50 cerebros  
   **Modificador de velocidad:** 1  
   **Mecánica:** Avanza continuamente, si llega a una planta, se detiene y empieza a comerla hasta morir o acabarla para seguir avanzando. Según su vida restante se debe modificar su apariencia gráfica: al 50% de su salud pierde su brazo.

2. ### Zombi caracono

   **Puntos salud:** 100 de base \+ 100 de escudo por el cono  
   **Daño:** 20 por segundo  
   **Costo:** 100 cerebros  
   **Modificador de velocidad:** 1  
   **Mecánica:** Usa el cono como primer escudo, es decir, mientras tenga el cono todo el daño entrante daña primero al cono, al agotarse ese escudo el cono se cae y el zombi continúa con las mecánicas de un zombi común.

3. ### Zombi saltador de Garrocha

   **Puntos salud:** 120  
   **Daño:** 15 por segundo  
   **Costo:** 100 cerebros  
   **Modificador de velocidad:** 1.7  
   **Mecánica:** Se desplaza rápidamente con una garrocha. Al detectar la primera planta en su camino, se muestra una animación de salto que le permite pasar por encima de la planta, hacia la siguiente casilla, a menos que esta sea una Nuez Cáscara-rabias. Después del salto (sin importar si logró adelantarse una casilla o no) pierde la garrocha y su velocidad se reduce a 1 para el resto de la partida, al igual que el zombi común, este pierde su brazo al llegar al 50% de su vida.

4. ### Zombi deportista

   **Puntos salud:** 100 de base \+ 150 de escudo por el casco  
   **Daño:** 10 cada 0.5 segundos  
   **Costo:** 150 cerebros  
   **Modificador de velocidad:** 2  
   **Mecánica:** Similar al zombi caracono, este tiene un casco de rugby que usa como escudo y que recibe todo el daño inicial hasta hasta acabar el escudo, luego continúa con las mecánicas de un zombi normal, excepto que con mayor velocidad.

5. ### Zombi Bailón

   **Puntos de salud**: 150  
   **Daño:** 15 por segundo  
   **Costo:** 150 cerebros  
   **Modificador de velocidad**: 0.8  
   **Mecánica**: Avanza continuamente por su carril. A cada 5 segundos, se detiene a bailar por 1 segundo, con ello ejecuta una invocación que genera a un Zombi Bailarín en cada una de las 4 casillas adyacentes disponibles (arriba, abajo, adelante y atrás). Cada uno de estos zombis bailarines invocados posee exactamente las mismas estadísticas y mecánica de juego que un zombi común, diferenciándose únicamente en su apariencia gráfica. Tras completar la invocación, el zombi bailón reanuda su avance o ataque normal.

6. ### Zombistein

   **Puntos salud:** 300  
   **Costo:** 350 cerebros  
   **Modificador de velocidad:** 0.7  
   **Mecánica:** No se detiene a comer ninguna planta ya que utiliza un garrote para aplastar e ignorar instantáneamente la vida de cualquier planta que tenga justo enfrente, es por ello que no tiene puntos de daño ya que nunca intentará comer una planta, directamente la aplastará al acercarse. Al llegar al 50% de su vida este arroja al Zombidito que lleva en su espalda por los aires para que este llegue justo antes de la penúltima casilla del carril. Se debe representar visualmente cuando una planta es aplastada.

7. ### Zombidito

   **Puntos salud:** 50  
   **Modificador de velocidad:** 1.7  
   **Mecánica:** Aparece montado sobre un Zombistein, es rápido pero muy débil, su objetivo es infiltrarse en las últimas casillas del jardín al ser lanzado por el Zombistein. Este no es un zombi invocable por el jugador ya que solo aparece junto a Zombistein, es por eso que no tiene un costo en cerebros.

Al morir un zombi estos desaparecen de la pantalla.

## Partida

Una partida consiste en atacar la casa defendida por las plantas posicionando zombis en los diferentes carriles del jardín. Los zombis inician al principio de cada carril en el borde y avanzan en línea recta, horizontalmente comiendo las plantas en su camino tratando de pasarlas todas para finalmente entrar a la casa, las plantas tienen diferentes mecanismos de defensa que dañan a los zombis a medida que estos avanzan por el jardín.

### Configuración inicial

Antes de iniciar una nueva partida, la aplicación debe presentar una pantalla de selección donde el jugador seleccionará:

* **Nivel a jugar:** El jardín se cargará por medio de un archivo JSON que contiene la distribución inicial de plantas y parámetros de configuración de la partida. La aplicación abrirá un explorador de archivos que permitirá seleccionar el archivo del nivel que desee el jugador.

* **Nivel de dificultad**: (Fácil/Medio/Difícil) Determina el comportamiento de la computadora para defenderse.

Durante el desarrollo de una partida, deben estar presentes en la pantalla los siguientes elementos:

* **Jardín**: Cuadrícula formada por carriles y columnas que representa el terreno sobre el que se desarrolla la partida. En el extremo derecho de la pantalla se ubica la Casa, y el extremo izquierdo es el punto de partida de los zombis, el tamaño y distribución del jardín dependerá del archivo JSON cargado.

* **Panel de control:** Debe estar ubicado en uno de los bordes de la pantalla, debe mostrar en todo momento:

  * **Cantidad de cerebros**: Cantidad actual de cerebros que posee el jugador para comprar zombis.

  * **Catálogo de zombis**: Iconos o botones de los tipos de zombis disponibles. Cada opción debe mostrar su costo en cerebros.

  * **Reloj de partida:** Muestra el progreso de la partida como un temporizador que cuenta minutos y segundos de forma regresiva hasta que acabe la partida.

  * **Pausar partida**: Deben detenerse todos los procesos, se congela el avance del temporizador de la partida y el movimiento de todas las entidades en el jardín, se pausa el disparo de las plantas y la toma de decisiones de la computadora, el jugador tampoco puede interactuar en el jardín mientras la partida esté pausada. Se muestra una ventana emergente que contiene las opciones:

    * **Reanudar**: Cierra la ventana emergente y se retoma el flujo normal del juego, reactivando todos los procesos pausados, todo continua como antes de detenerse.

    * **Guardar y salir:** Guarda persistentemente el estado de la partida usando archivos binarios para continuar la partida en otro momento y regresa al menú principal. Al guardar una partida se identificará con la fecha y hora en la que fue guardada, se almacenará en la carpeta específica del jugador.

  ### Jardín

Al arrancar la partida, el sistema lee la configuración en el archivo seleccionado, prepara la partida y dibuja el jardín, este se genera ya poblado con la distribución inicial de plantas definida en el archivo. 

La cantidad de carriles y de columnas del jardín no estará predefinida por la aplicación, sino que se determinará a partir del archivo JSON. Todos los carriles de un mismo archivo deben tener la misma cantidad de columnas; si el archivo contiene carriles con distinta cantidad de columnas entre sí, se considera un archivo inválido y debe rechazarse mostrando un mensaje de error al jugador.

El archivo debe contener un único objeto JSON con las siguientes propiedades:

* **duracionSegundos**: número entero con la duración total de la partida en segundos.  
* **cerebrosIniciales**: número entero con la cantidad inicial de cerebros para el jugador.  
* **jardin**: un arreglo con uno o más elementos, donde cada elemento representa un carril (del primero al último, de arriba hacia abajo). Cada elemento es a su vez un arreglo con una o más cadenas de texto, donde cada cadena representa una casilla del carril (de la primera a la última columna) y debe ser únicamente uno de los siguientes identificadores:

| Identificador en JSON | Tipo de planta equivalente |
| :---- | :---- |
| VACIO | Ninguna, casilla libre. |
| LANZAGUISANTES | Lanzaguisantes |
| PATATAPUM | Patatapum |
| HIELAGUISANTES | Hielaguisantes |
| NUEZ | Nuez cáscara-rabias |
| CARRONIVORA | Planta Carroñívora |
| PINCHOHIERBA | Pinchohierba |
| BIPETIDORA | Bipetidora |

Ejemplo de archivo válido:

| {  "duracionSegundos": 180,  "cerebrosIniciales": 150,  "jardin": \[    \["VACIO", "PATATAPUM", "VACIO", "NUEZ", "VACIO", "VACIO", "LANZAGUISANTES", "VACIO", "VACIO"\],    \["VACIO", "VACIO", "NUEZ", "VACIO", "VACIO", "CARRONIVORA", "VACIO", "VACIO", "HIELAGUISANTES"\],    \["PINCHOHIERBA", "NUEZ", "CARRONIVORA", "LANZAGUISANTES", "VACIO", "VACIO", "VACIO", "VACIO", "VACIO"\],    \["VACIO", "PATATAPUM", "VACIO", "PINCHOHIERBA", "LANZAGUISANTES", "VACIO", "VACIO", "VACIO", "VACIO"\],    \["VACIO", "VACIO", "NUEZ", "VACIO", "HIELAGUISANTES", "VACIO", "VACIO", "VACIO", "VACIO"\]  \]} |
| :---- |

Los archivos de entrada tendrán siempre esta estructura exacta, en este orden y con estas claves, solo cambia la cantidad de carriles y columnas del jardín. No es necesario que la solución soporte cualquier estructura válida de JSON, basta con este formato específico.

Este archivo no debe ser modificado por el programa, solo se usará para cargar la configuración inicial de una partida.

### Niveles de dificultad

* **Fácil**: La computadora no siembra plantas adicionales. El jugador ataca únicamente el jardín cargado inicialmente.

* **Medio**: La computadora evalúa el estado del jardín cada 10 segundos, selecciona de manera aleatoria un tipo de planta y la siembra en la primera casilla disponible más cercana a la casa.

* **Difícil**: Cada 5 segundos, la IA analiza todo el jardín y los zombis activos para ejecutar una única acción, esta acción es elegida según la primer coincidencia que encuentre en el siguiente orden de condiciones, considerando que si algo no se cumple o por alguna razón no puede ejecutar la acción (por ejemplo no hay casillas vacías en el carril), pasará a evaluar la siguiente condición

  * Si al menos un zombi se encuentra **a 4 o menos casillas de llegar a la casa**. La computadora prioriza sembrar una Nuez o Planta Carroñívora en la casilla libre más cercana antes del zombi en ese carril.   
  * Si detecta que algún zombi ha superado o traspasado alguna planta siembra una bipetidora en la primera casilla libre a la izquierda del zombi.  
  * Si la computadora detecta la presencia de zombis muy resistentes (Zombi caracono o Zombistein). Sembrará una Patatapum o una Planta Carroñívora en ese carril.  
  * Si detecta un zombi deportista siembra un Hielaguisantes para reducir la velocidad del avance.  
  * Si detecta un zombi saltador de Garrocha siembra una Nuez para hacerlo desperdiciar su salto.  
  * Si detecta que hay más de 3 zombis en un mismo carril siembra un Pinchohierba para causar daño a todos los zombis en el área.  
  * Si ninguna de las condiciones anteriores se cumple, siembra un Lanzaguisantes normal en el carril con menor cantidad de defensas o con casillas vacías hacia el fondo.

  ### Invocación de Zombis

El jugador usa cerebros como moneda, en el archivo JSON seleccionado al iniciar partida se define una cantidad inicial de cerebros que se le otorgan al jugador desde el inicio de la partida.

Además la partida contará con un número de procesos generadores de cerebros definido según la dificultad seleccionada: **Fácil \= 6 procesos, Medio \= 5, Difícil \= 4 procesos.** Cada generador ejecuta un ciclo continuo durante toda la partida, este ciclo inicia esperando un tiempo aleatorio de entre 5 y 15 segundos, al cumplirse esa espera, hace aparecer sobre el jardín un ícono de cerebro en una posición aleatoria.

Del mismo modo, cada vez que una planta es destruida esta dejará en su casilla entre 3 y 6 íconos de cerebro,

Cualquier ícono de cerebro permanecerá visible en pantalla durante **5 segundos**. Si el jugador hace clic sobre él dentro de ese lapso, el ícono desaparece inmediatamente y se le otorgan al jugador **25 cerebros**. Si transcurren los 5 segundos sin que el jugador haga clic sobre el ícono, este desaparece de la pantalla sin otorgar cerebros y el jugador pierde la oportunidad de conseguir estos cerebros. Una vez completado el ciclo de un generador, ya sea porque se recogió el cerebro o porque el ícono expiró, el generador inicia un nuevo ciclo de espera aleatoria, repitiendo el proceso durante el resto de la partida.

Para el funcionamiento correcto de la recolección de cerebros se debe permitir la superposición de los íconos de cerebro y estos deben colocarse sobre todos los demás elementos en el jardín para poder ser clickeables por el jugador, sin bloquear o interferir con los mecanismos de combate y movimiento de los demás elementos en el jardín.

En todo momento se debe visualizar en pantalla la cantidad actualizada de cerebros del jugador.

Para invocar a un zombi, el jugador selecciona un tipo de zombi del catálogo haciendo click, luego, del mismo modo selecciona el carril por el cual desea enviarlo, con ello, el zombi aparece en el extremo inicial de ese carril e inicia su ataque, se debe descontar el costo de cerebros. Si el jugador no tiene suficientes cerebros no podrá invocar al zombi y se le debe mostrar un mensaje de error.

### Comportamiento de las entidades

El movimiento de los zombis y proyectiles debe ser fluido y continuo, horizontalmente a través de cada carril, modificando su posición en pantalla a una velocidad constante según su tipo.

El sistema debe evaluar constantemente la distancia entre los elementos del mismo carril:

* Entre un proyectil y un zombi, ya que los proyectiles se mueven en dirección contraria a los zombis, se recomienda usar un cálculo de coordenadas, por ejemplo se considera que hubo un impacto cuando la coordenada en el eje x del proyectil es menor o igual a la coordenada en x del zombi. Al detectar un impacto de proyectil se aplica el daño y los efectos del proyectil sobre el zombi. Un proyectil solo afecta al primer zombi con el que colisiona, no puede afectar a múltiples zombis.  
* Un zombi detecta y empieza a atacar a una planta cuando la diferencia entre sus coordenadas horizontales es menor a un umbral de distancia definido por el desarrollador.

Para poder aplicar las reglas del juego que dependen de la ubicación en una casilla (las mecánicas de Patatapum, la Planta Carroñívora, la Nuez cáscara-rabias, Pinchohierba, el destino del salto del Zombi saltador de garrocha, Zombidito, Zombi bailón) cada zombi debe tener en todo momento una casilla actual claramente definida (no puede estar “en medio” de dos casillas). El desarrollador es libre de definir el mecanismo mediante el cual la posición visual de un zombi se traduce a esa casilla lógica. Podría ser por ejemplo, que haya una cantidad fija de "pasos" que el zombi da por casilla, donde la velocidad del zombi afecta el tiempo entre pasos y no el tamaño del paso; dividir la coordenada x entre el ancho de columna; mantener un contador de columna que se actualiza al completar el recorrido de una casilla; u otro mecanismo equivalente, el mecanismo elegido debe quedar documentado en el manual técnico.

### Hordas de zombis

Cada partida tendrá dos periodos de hordas de zombis.

Para calcular los tiempos en los que ocurren las hordas, la duración total de la partida (definida en el archivo JSON) se divide exactamente entre 7 intervalos de tiempo iguale:

$Duración\ periodo\ =\frac{Duración\ total\ de\ la\ partida}{7}$

Los bloques 4 y 7 serán periodos de hordas, el flujo de la partida será del siguiente modo

| Bloque 1 |  \> | Bloque 2 |  \> | Bloque 3 |  \> | Bloque 4 |  \> | Bloque 5 |  \> | Bloque 6 |  \> | Bloque 7 |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| Normal |  | Normal |  | Normal |  | **Horda 1** |  | Normal |  | Normal |  | **Horda final** |

Las condiciones en estos bloques serán las siguientes:

* **Bloques 1, 2, 3, 5 y 6 :** El costo de invocación de los zombis en el catálogo se mantiene en su valor base  
* **Bloque 4:** Primera Horda, al ingresar a este periodo de la partida la interfaz debe notificar que es periodo de horda, durante la duración de este bloque, todos los costos en cerebros del catálogo se reducen un 50% de modo que por ejemplo, un Zombi Común pasa de 50 a 25 cerebros.  
* **Bloque 7:** al comenzar el último periodo de la partida, se activa la horda final, en esta los costos de invocación se reducen un 75% respecto al valor base. Es la última oportunidad del jugador para romper la defensa de la computadora antes de que se le acabe el tiempo.

  ### Podadoras

Cada uno de los carriles del jardín cuenta con una podadora en el borde derecho, antes de las casillas sembrables, la podadora funciona como última defensa de la casa, están presentes desde el inicio de la partida, y deben representarse gráficamente en todo momento en su posición correspondiente.

Una podadora se activa automáticamente, en el momento en que algún zombi de su carril alcanza el borde derecho, es decir, está a punto de entrar a la casa, al activarse, la podadora arranca y avanza eliminando instantáneamente a todos los zombis que se encuentren en ese momento en su carril, sin importar su vida restante ni su tipo. Cada podadora solo puede activarse **una vez por partida**, una vez usada esta desaparece y ese carril queda sin esta defensa por el resto de la partida.

### Fin de la partida

Las condiciones  bajo las que se termina la partida son:

* **Victoria**: un zombi cruza el límite derecho del mapa en un carril cuya podadora ya fue utilizada anteriormente en la partida.   
* **Derrota:** Ocurre si el temporizador del nivel llega a 00:00 y ningún zombi logró invadir la casa, significa que la computadora ha defendido la casa exitosamente.

Al concluir una partida, el juego debe detener la acción e interponer una pantalla con el resumen:

* Resultado de la partida (victoria o derrota)  
* Tiempo total transcurrido.  
* Cantidad de plantas destruidas  
* Cantidad de zombis invocados

## Reanudar partida guardada

Cuando el usuario selecciona la opción ‘Reanudar Partida Guardada’ desde el menú principal el sistema abre una ventana que lista todas las partidas pausadas y guardadas previamente. El jugador selecciona una partida, la aplicación recupera los datos del archivo binario y reconstruye la partida restaurando:

* El tiempo restante en el temporizador.  
* La cantidad de cerebros del jugador.  
* El conjunto de zombis, plantas y proyectiles activos en el jardín, con sus valores guardados (vida restante, posición y estados).

Una vez reconstruida, la partida se mostrará en la pantalla de pausa, como si el jugador apenas hubiera pausado la partida mostrando el tablero reconstruido con la ventana emergente de pausa. El jugador deberá presionar Reanudar para dar marcha a la ejecución de los procesos de la partida.

## Estadísticas

Presenta el resumen de los resultados del jugador actual y permite compararlos contra los demás jugadores registrados. Toda la información de una cuenta debe almacenarse en un único archivo de texto plano (.txt), que sea legible al abrirlo con cualquier editor de texto y que también sea procesable desde Java. El archivo debe registrar las siguientes métricas:

* **Partidas Jugadas**: Total de partidas iniciadas y concluidas.  
* **Partidas Ganadas**: Cantidad de veces que se logró invadir la casa.  
* **Partidas Perdidas:** Derrotas por tiempo agotado o falta de recursos.  
* **Plantas Destruidas:** Total histórico de plantas eliminadas por los zombis del jugador a lo largo de todas sus partidas.  
* **Zombis invocados**: Total histórico de zombis invocados a lo largo de todas sus partidas.  
* **Cerebros gastados**: Cantidad total de cerebros usados.  
* **Mejor tiempo de victoria:** El menor tiempo en segundos, en el que el jugador logró llevar al menos un zombi a la casa.  
* **Podadoras activadas:** Número total de podadoras activadas a lo largo de todas las partidas.

La pantalla de estadísticas tendrá dos pestañas o paneles visuales, cada uno con las estadísticas:

* **Estadísticas de cuenta**: Muestra el resumen consolidado del jugador actualmente con la sesión abierta. Incluye:  
  * Porcentaje de Victorias  
  * Promedio de Plantas destruidas por Partida  
* **Ranking Global**:  
  * Top 5 invasores más rápidos: Lista de nicknames de los jugadores ordenados de menor a mayor tiempo de victoria entre todas las cuentas registradas.  
  * Top 5 mayores destructores: Lista de nicknames ordenada de mayor a menor según el total de plantas destruidas.  
  * Top 5  jugadores más exitosos: Lista de nicknames ordenada por la mayor cantidad de partidas ganadas.

# **Importante**

* Uso obligatorio del lenguaje de programación JAVA  
* Proyecto obligatorio para tener derecho al siguiente proyecto.  
* Aplicación de escritorio exclusivamente con interfaz gráfica en Java Swing.  
* Es válido utilizar algún IDE o cualquier editor de texto.  
* Las copias entre estudiantes obtendrán nota de CERO y se notificará a coordinación.  
* El uso de código extraído de internet o generado por Inteligencia Artificial sin que el estudiante pueda explicar detalladamente su funcionamiento se penalizará con nota de cero.  
* El estudiante debe ser capaz de defender y explicar la totalidad del código fuente en la calificación para que se tome como válido.

# **Limitaciones**

* No está permitido el uso de estructuras de datos dinámicas de Java Collections Framework (ArrayList, LinkedList, HashMap, HashSet, etc.). De ser necesario, implementar estructuras dinámicas propias, se pueden utilizar clases genéricas y arreglos nativos.  
* Prohibido el uso de dependencias externas como librerías de terceros mediante Maven, Gradle o .jar externos (Gson, Jackson, Apache Commons, Guava, etc.). Toda la solución debe compilar únicamente con el JDK estándar de Java  
* La lectura y parseo de archivos de texto (.txt y .json), deben programarse mediante con las librerías estándar del paquete java.io, no se puede utilizar librerías externas de parseo (Gson, Jackson, org.json, etc.). El estudiante debe implementar su propio mecanismo de lectura e interpretación del archivo JSON.  
* Prohibido el uso de Lombok, se deben definir explícitamente los métodos y constructores de cada clase.  
* No se permite crear atributos public static para usarlos como variables globales y compartir información entre clases. El uso de static debe limitarse a constantes, métodos de utilidad verdaderos (como funciones matemáticas de ayuda) o el método main.  
* Todos los atributos de clase deben declararse como private o protected. Ningún atributo de clase puede ser público.  
* La clase principal Main solo debe contener la lógica de arranque de la aplicación. No se aceptará que todo el juego esté codificado dentro del método main ni dentro de un único archivo monolítico.  
* La aplicación debe funcionar con interfaz gráfica de usuario usando exclusivamente Java Swing.  
* No se pueden utilizar herramientas de sincronización avanzadas. Específicamente, no se permite el uso de la clase **javax.swing.Timer**, las palabras reservadas **synchronized** y **volatile**, los métodos de control **wait()**, **notify()** y **notifyAll()**, ni ninguna de las clases o utilidades pertenecientes al paquete **\`java.util.concurrent** y sus subpaquetes.

# **Entrega**

La fecha de entrega es el 28 de octubre en la publicación correspondiente de Classroom. Los componentes a entregar son:

* Código fuente.  
* Código compilado (.jar ejecutable).  
* Manual técnico: Documento PDF que incluya:  
  * Diagrama de clases UML.  
  * Explicación de la arquitectura Orientada a Objetos, herencia e interfaces utilizadas.  
  * Algoritmos de flujo general del motor del juego, lógica de defensa de la computadora y descripción de los ciclo de vida de los hilos principales.  
  * Descripción de la estructura y formatos de los archivos de texto manejados.  
* Manual de usuario: Documento PDF con capturas del paso a paso de todo el flujo y funcionamiento de la aplicación.

Para la calificación el estudiante debe llevar impresa la hoja de calificación que se publicará en los siguientes días.


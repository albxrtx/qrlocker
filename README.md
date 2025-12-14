# 📱 QrLocker

## 📃 Descripción del proyecto

QRLocker es una aplicación Android desarrollada con Kotlin + Jetpack Compose que permite a los usuarios reservar taquillas mediante escaneo de códigos QR.
La app se conecta a un backend en Flask + Supabase mediante API REST, el cual gestiona la disponibilidad de las taquillas y almacena las reservas.

## 📺 Demo

https://www.youtube.com/shorts/ALMSwjPysa0

## ⭐ Características principales

🔍 Escaneo de códigos QR en tiempo real
Permite identificar automáticamente la taquilla asociada mediante el uso de la cámara del dispositivo, eliminando la introducción manual de datos.

🔄 Consulta instantánea del estado de la taquilla
Realiza peticiones GET al backend para comprobar si una taquilla está disponible u ocupada antes de permitir la reserva.

📅 Reserva de taquillas con fecha de fin personalizada
El usuario puede seleccionar la fecha y hora de finalización de la reserva, que se envía al backend mediante peticiones POST.

🔐 Prevención de reservas duplicadas
El sistema bloquea automáticamente las taquillas ya reservadas, garantizando la integridad de los datos y evitando conflictos.

🧭 Navegación clara e intuitiva entre pantallas
Implementación de un flujo de navegación sencillo mediante Navigation Compose, mejorando la experiencia de usuario.

🎨 Interfaz moderna y reactiva
UI desarrollada con Jetpack Compose, siguiendo un diseño limpio, responsive y orientado a usabilidad.

⚡ Gestión eficiente de operaciones asíncronas
Uso de Kotlin Coroutines para llamadas a red sin bloquear la interfaz de usuario.

🌐 Integración completa con API REST
Comunicación fluida entre frontend y backend mediante Retrofit, con manejo de errores y respuestas del servidor.

## 🛠️ Tecnologías Utilizadas

| Tecnología          | Uso                                  |
| ------------------- | ------------------------------------ |
| **Python+Flask**    | Lenguaje principal del backend       |
| **Jetpack Compose** | Framework UI para Android            |
| **Kotlin**          | Lenguaje principal de la app Android |
| **Retrofit**        | Conexión al backend desde Android    |
| **ZXing**           | Escaneo de códigos QR para Android   |
| **Render**          | Hosteo de la API REST                |
| **Supabase**        | Base de datos PostgreSQL             |

## ⚠️ Requisitos

- Android Studio (Última versión a ser posible)
- Dispositivo Android físico
- Cámara del dispositivo funcional
- Aceptar permisos de cámara
- Versión Android 8.0 o superior
- Conexión a internet

## 📂 Ejecución de la app

Clonar el repositorio

```bash
git clone https://github.com/albxrtx/qrlocker.git
```

Navegar a la carpeta del proyecto

```bash
cd .\qrlocker\app
```

Abrir el proyecto y ejecutar la app

💡 El backend se encuentra desplegado en producción, por lo que no es necesario realizar configuraciones adicionales para consumir la API.

# 📱 App de Fisioterapia

Aplicación móvil desarrollada en **Android Studio** con **Kotlin**, orientada a facilitar la gestión de pacientes, diagnósticos, valoraciones y tratamientos por parte de fisioterapeutas.

## ✅ Características principales

- Registro e inicio de sesión con autenticación por correo y Google.
- Gestión completa de pacientes (ficha, historial, tratamientos).
- Chat entre fisioterapeutas.
- Acceso a noticias del mundo de la fisioterapia desde API externa.
- Interfaz limpia, adaptada y con mensajes de error informativos.

## 🛠️ Tecnologías utilizadas

- **Lenguaje**: Kotlin
- **IDE**: Android Studio
- **Base de datos**: Firebase Realtime Database / Firestore / PostgreSQL (según funcionalidad)
- **Autenticación**: Firebase Authentication
- **Consumo de API externa**: Retrofit
- **Consumo de API propia**: Retrofit mediante conexión con Aplicación (Node.js 22.14.00) desplegada en Azure
- **Arquitectura**: Clean Architecture (Data, Domain, UI, Utils)
- **Otros**: Material Design Components, RecyclerView, ViewModel, LiveData

## 🚀 Despliegue

La aplicación se ha compilado en modo `release`, firmada con un keystore propio, y está lista para instalarse en dispositivos Android mediante el archivo `.apk` generado. Se puede encontrar el enlace a su descarga en el anexo dentro de la documentación del proyecto.


## 👥 Usuarios

- **Fisioterapeutas**: Acceso completo a gestión de pacientes, diagnósticos, valoraciones y tratamientos.

## 📄 Manual de usuario

El manual de usuario se incluye como anexo en la documentación del proyecto.

## 🔗 Enlaces útiles

- [Repositorio del proyecto (GitHub)](https://github.com/freduki98/FisioApp.git)
- [Api Rest (GitHub)](https://github.com/freduki98/ApiRestFulFisioApp.git)
- [Recursos utilizados para la base de datos (GitHub)](https://github.com/freduki98/PostgresDBFisioApp.git)

## 📃 Licencia

Este proyecto ha sido desarrollado como trabajo académico. No se distribuye públicamente como producto comercial.



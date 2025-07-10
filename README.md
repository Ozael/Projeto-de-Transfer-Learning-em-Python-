# 🧠 Projeto de Transfer Learning em Python

Este repositório contém a solução para o desafio de aplicar **Transfer Learning** usando Python e TensorFlow, proposto no curso da **Digital Innovation One (DIO)**.

O projeto é dividido em duas partes:
- Um **notebook Colab** com o treinamento de um modelo baseado no MNIST.
- Um **aplicativo Android nativo** que utiliza esse modelo `.tflite` para reconhecer números em tempo real via câmera.

---

O modelo foi treinado com o dataset **MNIST** usando a técnica de **Transfer Learning**

O app foi desenvolvido com **Jetpack Compose + CameraX** e utiliza o modelo `.tflite` para reconhecer números em tempo real através da câmera.

### 📷 Funcionalidades:
- Câmera ativa com **retângulo de guia visual**
- Reconhecimento de números (0–9) diretamente no dispositivo
- Atualização contínua do dígito detectado


### ⚙️ Tecnologias utilizadas:
- Kotlin
- Jetpack Compose
- CameraX
- TensorFlow Lite


### 🚀 Como testar:
1. Abra o projeto Android no Android Studio
2. Rode o app em um dispositivo ou emulador com câmera


## 📈 Resultados esperados

- Reconhecimento eficaz de números manuscritos ou impressos
- Baixo custo computacional (modelo leve e eficiente)
- Rodando 100% offline no dispositivo


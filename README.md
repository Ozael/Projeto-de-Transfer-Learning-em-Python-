# 🧠 Projeto de Transfer Learning em Python

Este projeto foi desenvolvido como parte de um desafio proposto pela **Digital Innovation One (DIO)**, com o objetivo de aplicar **Transfer Learning** para reconhecimento de dígitos numéricos usando Python e TensorFlow.

---

## 🔍 Visão Geral

A solução está dividida em duas partes:

1. 📓 Um **notebook Colab** com o treinamento de um modelo baseado no **dataset MNIST**.
2. 📱 Um **aplicativo Android nativo** que utiliza o modelo `.tflite` para reconhecer dígitos numéricos **em tempo real via câmera**.

---

## 🧠 Modelo

- Treinado com o **MNIST** para classificar dígitos de 0 a 9.
- Utiliza **Transfer Learning com MobileNetV2**.
- Exportado no formato **TensorFlow Lite (.tflite)** para uso em dispositivos móveis.

---

## 📷 Funcionalidades do App

- Visualização da câmera com um retângulo de guia central.
- Reconhecimento em tempo real de números manuscritos ou impressos.
- Exibição contínua do número detectado diretamente no app.

---

## ⚙️ Tecnologias Utilizadas

- **Kotlin**
- **Jetpack Compose**
- **CameraX**
- **TensorFlow Lite**

---

## 🚀 Como Testar

1. Abra o projeto Android no Android Studio.
2. Rode o app em um dispositivo físico ou emulador com câmera.
3. Posicione um número no centro da tela para reconhecer.

---

## 📈 Resultados Esperados

- Reconhecimento rápido e eficiente de dígitos.
- Baixo consumo de recursos (modelo leve).
- Funciona **100% offline**.

---

package dev.ozael.reconhecerdigitos

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.ozael.reconhecerdigitos.ui.theme.ReconhecerDigitosTheme
import org.tensorflow.lite.Interpreter
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class MainActivity : ComponentActivity() {

  private val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    if (!isGranted) {
      Toast.makeText(this, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
      != PackageManager.PERMISSION_GRANTED
    ) {
      requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    setContent {
      ReconhecerDigitosTheme {
        Box(modifier = Modifier.fillMaxSize()) {
          CameraPreviewWithRecognition()
        }
      }
    }
  }
}

@Composable
fun CameraPreviewWithRecognition() {
  val context = LocalContext.current
  val lifecycleOwner = LocalLifecycleOwner.current
  val previewView = remember { PreviewView(context) }
  var recognizedDigit by remember { mutableStateOf("Aguardando...") }

  val tfliteInterpreter = remember {
    Interpreter(loadModelFile(context, "mnist_model.tflite"))
  }

  LaunchedEffect(Unit) {
    val cameraProvider = ProcessCameraProvider.getInstance(context).get()

    val preview = Preview.Builder().build().also {
      it.setSurfaceProvider(previewView.surfaceProvider)
    }

    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    val imageAnalyzer = ImageAnalysis.Builder()
      .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
      .build()

    imageAnalyzer.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
      val bitmap = imageProxyToBitmap(imageProxy)
      if (bitmap != null) {
        val input = bitmapToFloatBuffer(bitmap) // Agora em RGB 224x224x3
        val output = Array(1) { FloatArray(10) }

        try {
          tfliteInterpreter.run(input, output)
          val maxIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
          recognizedDigit = if (maxIndex >= 0) maxIndex.toString() else "?"
        } catch (e: Exception) {
          Log.e("TFLite", "Erro ao rodar o modelo", e)
          recognizedDigit = "Erro"
        }
      }
      imageProxy.close()
    }

    try {
      cameraProvider.unbindAll()
      cameraProvider.bindToLifecycle(
        lifecycleOwner, cameraSelector, preview, imageAnalyzer
      )
    } catch (e: Exception) {
      Log.e("Camera", "Erro ao iniciar câmera", e)
    }
  }

  Box(modifier = Modifier.fillMaxSize()) {
    AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
    OverlayBox() // Aqui desenha o contorno
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .align(Alignment.BottomCenter)
    ) {
      Text(
        text = "Número reconhecido: $recognizedDigit",
        fontSize = 24.sp
      )
    }
  }
}

// Carrega o modelo TFLite da pasta assets
fun loadModelFile(context: android.content.Context, modelName: String): MappedByteBuffer {
  val fileDescriptor = context.assets.openFd(modelName)
  val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
  val fileChannel = inputStream.channel
  return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
}

// Converte imagem da câmera (YUV) para Bitmap RGB
fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
  val yBuffer = image.planes[0].buffer
  val uBuffer = image.planes[1].buffer
  val vBuffer = image.planes[2].buffer

  val ySize = yBuffer.remaining()
  val uSize = uBuffer.remaining()
  val vSize = vBuffer.remaining()

  val nv21 = ByteArray(ySize + uSize + vSize)
  yBuffer.get(nv21, 0, ySize)
  vBuffer.get(nv21, ySize, vSize)
  uBuffer.get(nv21, ySize + vSize, uSize)

  val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
  val out = ByteArrayOutputStream()
  yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, out)
  val imageBytes = out.toByteArray()
  return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

// Redimensiona e normaliza bitmap RGB para input shape (1, 224, 224, 3)
fun bitmapToFloatBuffer(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
  val input = Array(1) { Array(224) { Array(224) { FloatArray(3) } } }
  val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

  for (y in 0 until 224) {
    for (x in 0 until 224) {
      val pixel = resizedBitmap.getPixel(x, y)
      val r = (pixel shr 16 and 0xFF) / 255f
      val g = (pixel shr 8 and 0xFF) / 255f
      val b = (pixel and 0xFF) / 255f
      input[0][y][x][0] = r
      input[0][y][x][1] = g
      input[0][y][x][2] = b
    }
  }
  return input
}

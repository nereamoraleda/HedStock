package es.cursos.android.ejercicios.stocksnma.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.CaptureActivity


// Clase para permitir la rotación de la pantalla Scanner
class PortraitCaptureActivity : CaptureActivity()


//class BarcodeScanContract : ActivityResultContract<Unit?, String?>() {
//    override fun createIntent(context: Context, input: Unit?): Intent {
//        val integrator = IntentIntegrator(context as Activity)
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
//        integrator.setPrompt("Escanea el código de barras")
//        integrator.setCameraId(0)
//        integrator.setBeepEnabled(true)
//        integrator.setBarcodeImageEnabled(true)
//        return integrator.createScanIntent()
//    }
//
//    override fun parseResult(resultCode: Int, intent: Intent?): String? {
//        val result: IntentResult = IntentIntegrator.parseActivityResult(resultCode, intent)
//        return result.contents
//    }
//}



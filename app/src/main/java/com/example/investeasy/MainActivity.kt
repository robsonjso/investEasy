package com.example.investeasy

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        // Referências aos campos de input e botões
        val aporteLayout = findViewById<TextInputLayout>(R.id.aporte_input)
        val aporteInput = aporteLayout.editText as TextInputEditText

        val mesesLayout = findViewById<TextInputLayout>(R.id.meses_input)
        val mesesInput = mesesLayout.editText as TextInputEditText

        val jurosLayout = findViewById<TextInputLayout>(R.id.juros_input)
        val jurosInput = jurosLayout.editText as TextInputEditText

        val resultadoInvestimento = findViewById<TextView>(R.id.resultado_investimento)
        val resultadoRendimentos = findViewById<TextView>(R.id.resultado_rendimentos)

        val calcularBtn = findViewById<Button>(R.id.calcular)
        val limparBtn = findViewById<Button>(R.id.limpar)

        // Inicialmente os TextViews devem estar invisíveis
        resultadoInvestimento.visibility = View.GONE
        resultadoRendimentos.visibility = View.GONE

        // Ação para calcular
        calcularBtn.setOnClickListener {
            // Normaliza a entrada substituindo ',' por '.'
            val aporteText = aporteInput.text.toString().replace(',', '.')
            val mesesText = mesesInput.text.toString()
            val jurosText = jurosInput.text.toString().replace(',', '.')

            val aporte = aporteText.toDoubleOrNull() ?: 0.0
            val meses = mesesText.toIntOrNull() ?: 0
            val jurosPercentual = jurosText.toDoubleOrNull() ?: 0.0

            // Converter juros percentual para decimal
            val jurosMensal = jurosPercentual / 100

            if (aporte > 0 && meses > 0 && jurosMensal > 0) {
                // Cálculo do valor final e dos rendimentos
                val montanteFinal = calcularInvestimento(aporte, meses, jurosMensal)
                val totalAportes = aporte * meses
                val rendimentos = montanteFinal - totalAportes

                // Exibir resultados e tornar os TextViews visíveis
                resultadoInvestimento.text = formatarMoeda(montanteFinal)
                resultadoRendimentos.text = formatarMoeda(rendimentos)
                resultadoInvestimento.visibility = View.VISIBLE
                resultadoRendimentos.visibility = View.VISIBLE
            }
        }

        // Ação para limpar os campos
        limparBtn.setOnClickListener {
            aporteInput.text?.clear()
            mesesInput.text?.clear()
            jurosInput.text?.clear()
            resultadoInvestimento.visibility = View.GONE
            resultadoRendimentos.visibility = View.GONE
        }
    }

    // Função para calcular o montante final com juros compostos e aportes mensais
    private fun calcularInvestimento(aporte: Double, meses: Int, jurosMensal: Double): Double {
        return aporte * (Math.pow(1 + jurosMensal, meses.toDouble()) - 1) / jurosMensal
    }

    // Função para formatar valores em moeda
    private fun formatarMoeda(valor: Double): String {
        val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return formatador.format(valor)
    }
}

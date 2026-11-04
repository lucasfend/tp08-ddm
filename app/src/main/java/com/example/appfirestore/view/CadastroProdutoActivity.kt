package com.example.appfirestore.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appfirestore.databinding.ActivityCadastroProdutoBinding
import com.google.firebase.firestore.FirebaseFirestore

class CadastroProdutoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastroProdutoBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroProdutoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.btnCadastrarProduto.setOnClickListener {
            cadastrarProduto()
        }

        binding.btnVerListaProdutos.setOnClickListener {
            val intent = Intent(this, ListaProdutosActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cadastrarProduto() {
        val nome = binding.editNomeProduto.text.toString().trim()
        val categoria = binding.editCategoriaProduto.text.toString().trim()
        val precoTexto = binding.editPrecoProduto.text.toString().trim().replace(",", ".")

        binding.tilNomeProduto.error = null
        binding.tilCategoriaProduto.error = null
        binding.tilPrecoProduto.error = null

        if (nome.isEmpty()) {
            binding.tilNomeProduto.error = "Informe o nome do produto"
            return
        }

        if (categoria.isEmpty()) {
            binding.tilCategoriaProduto.error = "Informe a categoria"
            return
        }

        val preco = precoTexto.toDoubleOrNull()
        if (preco == null || preco < 0.0) {
            binding.tilPrecoProduto.error = "Informe um preço válido"
            return
        }

        binding.progressCadastro.visibility = View.VISIBLE
        binding.btnCadastrarProduto.isEnabled = false

        // Dados conforme o enunciado: Nome, Categoria e Preço
        val dados = hashMapOf(
            "Nome" to nome,
            "Categoria" to categoria,
            "Preço" to preco
        )

        // Coleção: Produtos | Documento: Autogerados (.add gera o ID automaticamente)
        db.collection("Produtos")
            .add(dados)
            .addOnSuccessListener { documentReference ->
                binding.progressCadastro.visibility = View.GONE
                binding.btnCadastrarProduto.isEnabled = true

                Toast.makeText(
                    this,
                    "Produto cadastrado! ID: ${documentReference.id}",
                    Toast.LENGTH_SHORT
                ).show()

                limparCampos()
            }
            .addOnFailureListener { e ->
                binding.progressCadastro.visibility = View.GONE
                binding.btnCadastrarProduto.isEnabled = true

                Toast.makeText(
                    this,
                    "Erro ao cadastrar produto: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun limparCampos() {
        binding.editNomeProduto.text?.clear()
        binding.editCategoriaProduto.text?.clear()
        binding.editPrecoProduto.text?.clear()
        binding.editNomeProduto.requestFocus()
    }
}

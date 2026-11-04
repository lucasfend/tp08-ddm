package com.example.appfirestore.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appfirestore.adapter.ProdutoAdapter
import com.example.appfirestore.databinding.ActivityListaProdutosBinding
import com.example.appfirestore.model.Produto
import com.google.firebase.firestore.FirebaseFirestore

class ListaProdutosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaProdutosBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: ProdutoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        adapter = ProdutoAdapter()
        binding.recyclerProdutos.layoutManager = LinearLayoutManager(this)
        binding.recyclerProdutos.adapter = adapter

        binding.btnAtualizarLista.setOnClickListener {
            recuperarProdutos()
        }

        binding.btnVoltarCadastro.setOnClickListener {
            finish()
        }

        recuperarProdutos()
    }

    private fun recuperarProdutos() {
        binding.progressLista.visibility = View.VISIBLE
        binding.txtListaVazia.visibility = View.GONE

        db.collection("Produtos")
            .get()
            .addOnSuccessListener { result ->
                binding.progressLista.visibility = View.GONE

                val produtos = mutableListOf<Produto>()
                for (document in result) {
                    val nome = document.getString("Nome")
                        ?: document.getString("nome")
                        ?: "Sem nome"

                    val categoria = document.getString("Categoria")
                        ?: document.getString("categoria")
                        ?: "Sem categoria"

                    val preco = document.getDouble("Preço")
                        ?: document.getDouble("Preco")
                        ?: document.getDouble("preco")
                        ?: document.getString("Preço")?.replace(",", ".")?.toDoubleOrNull()
                        ?: 0.0

                    produtos.add(
                        Produto(
                            id = document.id,
                            nome = nome,
                            categoria = categoria,
                            preco = preco
                        )
                    )
                }

                adapter.atualizarLista(produtos)

                if (produtos.isEmpty()) {
                    binding.txtListaVazia.visibility = View.VISIBLE
                } else {
                    binding.txtListaVazia.visibility = View.GONE
                }
            }
            .addOnFailureListener { e ->
                binding.progressLista.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Erro ao recuperar produtos: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}

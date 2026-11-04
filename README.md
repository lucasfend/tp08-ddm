# TP08 - Cadastro e Listagem de Produtos com Cloud Firestore

Projeto Android em Kotlin utilizando **Cloud Firestore** e **ViewBinding**.

## Estrutura no Firestore
- **Coleção:** `Produtos`
- **Documento:** Autogerados (`db.collection("Produtos").add(dados)`)
- **Dados:**
  - `Nome` (`String`)
  - `Categoria` (`String`)
  - `Preço` (`Double`)

## Telas
1. **Tela de Cadastro de Produtos (`CadastroProdutoActivity`)**:
   - Campos para `Nome`, `Categoria` e `Preço`.
   - Salva o produto na coleção `Produtos` com documento autogerado.
   - Botão para abrir a tela de listagem.
2. **Tela de Lista de Produtos (`ListaProdutosActivity`)**:
   - Recupera todos os documentos da coleção `Produtos` (`db.collection("Produtos").get()`) e exibe em um `RecyclerView` usando `ProdutoAdapter`.

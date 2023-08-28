package com.example.springapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoRepository produtoRepo;

    @Autowired
    public ProdutoController(ProdutoRepository produtoRepo) {
        this.produtoRepo = produtoRepo;
    }

    @GetMapping("/selecionar")
    public List<Produto> listarProdutos() {
        return produtoRepo.findAll();
    }

    @PostMapping("/inserir")
    public ResponseEntity<String> inserirProduto (@RequestBody Produto produto) {
        produtoRepo.save(produto);
        return ResponseEntity.ok("Produto inserido com sucesso");
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<String> excluirProduto(@PathVariable Long id) {
        produtoRepo.deleteById(id);
        return ResponseEntity.ok("Produto ecluído com sucesso");
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarProduto(@PathVariable Long id,
                                                   @RequestBody  double preco) {
        Optional<Produto> produtoExistente = produtoRepo.findById(id);
        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            produto.setPreco(preco);
            return ResponseEntity.ok("Produto atualizado com sucesso");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/selecionar/{id}")
    public ResponseEntity<Produto> getProdutoId(@PathVariable Long id) {
        if (produtoRepo.existsById(id)) {
            Produto produto = produtoRepo.findById(id).orElse(null);
            return ResponseEntity.ok(produto);
        } else {
          return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/inserirForm")
    public ResponseEntity<String> inserirProduto(@RequestParam String nome,
                                                 @RequestParam String descricao,
                                                 @RequestParam double preco,
                                                 @RequestParam int quantidade) {
        Produto novoProduto = new Produto(nome,descricao,preco,quantidade);
        try {
            produtoRepo.save(novoProduto);
            return ResponseEntity.ok("Produto inserido com sucesso");
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao inserir o produto");
        }
    }
}

package com.example.produtos.service;

import com.example.produtos.exception.ProdutoNotFoundException;
import com.example.produtos.model.Produto;
import com.example.produtos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProdutoService {

    private final ProdutoRepository repository;

    @Transactional(readOnly = true)
    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    public Produto criar(Produto produto) {
        return repository.save(produto);
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto existente = buscarPorId(id);
        existente.setNome(produtoAtualizado.getNome());
        existente.setDescricao(produtoAtualizado.getDescricao());
        existente.setPreco(produtoAtualizado.getPreco());
        existente.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
        existente.setCategoria(produtoAtualizado.getCategoria());
        return repository.save(existente);
    }

    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        repository.delete(produto);
    }
}

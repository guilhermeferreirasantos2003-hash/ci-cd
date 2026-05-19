package com.example.produtos;

import com.example.produtos.model.Produto;
import com.example.produtos.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdutoRepository repository;

    private Produto produtoBase() {
        return Produto.builder()
                .nome("Notebook Dell")
                .descricao("Notebook para desenvolvimento")
                .preco(new BigDecimal("4500.00"))
                .quantidadeEstoque(10)
                .categoria("Eletrônicos")
                .build();
    }

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("POST /api/produtos - Deve criar produto com sucesso")
    void deveCriarProduto() throws Exception {
        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoBase())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("Notebook Dell")))
                .andExpect(jsonPath("$.categoria", is("Eletrônicos")));
    }

    @Test
    @Order(2)
    @DisplayName("GET /api/produtos - Deve listar produtos")
    void deveListarProdutos() throws Exception {
        repository.save(produtoBase());

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Notebook Dell")));
    }

    @Test
    @Order(3)
    @DisplayName("GET /api/produtos/{id} - Deve buscar produto por ID")
    void deveBuscarPorId() throws Exception {
        Produto salvo = repository.save(produtoBase());

        mockMvc.perform(get("/api/produtos/{id}", salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(salvo.getId().intValue())))
                .andExpect(jsonPath("$.nome", is("Notebook Dell")));
    }

    @Test
    @Order(4)
    @DisplayName("GET /api/produtos/{id} - Deve retornar 404 para ID inexistente")
    void deveRetornar404() throws Exception {
        mockMvc.perform(get("/api/produtos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro", is("Recurso não encontrado")));
    }

    @Test
    @Order(5)
    @DisplayName("PUT /api/produtos/{id} - Deve atualizar produto")
    void deveAtualizarProduto() throws Exception {
        Produto salvo = repository.save(produtoBase());
        salvo.setNome("Notebook Dell Atualizado");
        salvo.setPreco(new BigDecimal("5000.00"));

        mockMvc.perform(put("/api/produtos/{id}", salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salvo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Notebook Dell Atualizado")))
                .andExpect(jsonPath("$.preco", is(5000.00)));
    }

    @Test
    @Order(6)
    @DisplayName("DELETE /api/produtos/{id} - Deve deletar produto")
    void deveDeletarProduto() throws Exception {
        Produto salvo = repository.save(produtoBase());

        mockMvc.perform(delete("/api/produtos/{id}", salvo.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/produtos/{id}", salvo.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    @DisplayName("POST /api/produtos - Deve retornar 400 para dados inválidos")
    void deveValidarCamposObrigatorios() throws Exception {
        Produto invalido = Produto.builder()
                .nome("")
                .preco(new BigDecimal("-1"))
                .build();

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos", notNullValue()));
    }

    @Test
    @Order(8)
    @DisplayName("GET /api/produtos?categoria= - Deve filtrar por categoria")
    void deveFiltrarPorCategoria() throws Exception {
        repository.save(produtoBase());
        Produto outro = produtoBase();
        outro.setNome("Mouse Logitech");
        outro.setCategoria("Periféricos");
        repository.save(outro);

        mockMvc.perform(get("/api/produtos").param("categoria", "Periféricos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].categoria", is("Periféricos")));
    }
}

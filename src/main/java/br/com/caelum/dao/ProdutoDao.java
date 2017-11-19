package br.com.caelum.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.com.caelum.model.Loja;
import br.com.caelum.model.Produto;

@Repository
public class ProdutoDao {

	@PersistenceContext
	private EntityManager em;

	public List<Produto> getProdutos() {
		return em.createQuery("from Produto", Produto.class).getResultList();
	}

	public Produto getProduto(Integer id) {
		Produto produto = em.find(Produto.class, id);
		return produto;
	}

	public List<Produto> getProdutos(String nome, Integer categoriaId, Integer lojaId) {

		/**
		 * "EntityManager" return a instance of "CriteriaBuilder", to uncouple the
		 * implementation of "JPA". And other methods.
		 */
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		/**
		 * "CriteriaQuery" is strongly typed. "CreateQuery" need a type. The type
		 * specified is the return of query.
		 */
		CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		/**
		 * The method "From" need the "Entitys" that has the values. This method
		 * return a "Interface" of "Root" that represent a "Entity". From of
		 * "Root" is possible to get the "Path" of "Properties".
		 */
		Root<Produto> root = criteriaQuery.from(Produto.class);
		Path<String> pathNomeProduto = root.<String>get("nome");
		Path<Integer> pathLojaId = root.<Loja>get("loja").<Integer>get("id");
		Path<Integer> pathCategoria = root.join("categorias").<Integer>get("id");
		/**
		 * The "CriteriaBuilder" building the "Predicates" to using in the
		 * "Where" of the "CriteriaQuery".
		 */
		Predicate conjuncao = criteriaBuilder.conjunction();
		if (!nome.isEmpty())
			conjuncao = criteriaBuilder.and(criteriaBuilder.like(pathNomeProduto, "%" + nome + "%"));
		if (categoriaId != null)
			conjuncao = criteriaBuilder.and(conjuncao, criteriaBuilder.equal(pathCategoria, categoriaId));
		if (lojaId != null)
			conjuncao = criteriaBuilder.and(conjuncao, criteriaBuilder.equal(pathLojaId, lojaId));
		
		/**
		 * Cast of "toArray" to "Predicate[]"
		 * */
		criteriaQuery.where(conjuncao);

		/**
		 * The last action is exchange the "CriteriaQuery" to a "TypedQuery".
		 */
		TypedQuery<Produto> typeQuery = em.createQuery(criteriaQuery);
		
		/**
		 * Enable query cache.
		 * */
		typeQuery.setHint("org.hibernate.cacheable", "true");
		
		return typeQuery.getResultList();
	}

	public void insere(Produto produto) {
		if (produto.getId() == null)
			em.persist(produto);
		else
			em.merge(produto);
	}

}

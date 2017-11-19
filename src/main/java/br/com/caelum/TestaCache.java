package br.com.caelum;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import br.com.caelum.model.Produto;

public class TestaCache {

	public static void main(String[] args) {

		ApplicationContext ctx = new AnnotationConfigApplicationContext(JpaConfigurator.class);
		
		EntityManagerFactory emf = ctx.getBean(EntityManagerFactory.class);
				
		EntityManager em = emf.createEntityManager();
		EntityManager em2 = emf.createEntityManager();
		
		Produto produto = em.find(Produto.class, 1);
		Produto produto2 = em2.find(Produto.class, 1);
		
		System.out.println(produto.getDescricao());
		System.out.println(produto2.getDescricao());
		
		em.close();
		em2.close();
		emf.close();
	}

}

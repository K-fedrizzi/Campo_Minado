package com.br.campo_minado.modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import com.br.campo_minado.exececao.ExplosaoException;

public class Tabuleiro {
 
	private int linhas;
	private int colunas;
	private int minas;
	
	private final List<Campo> campos = new ArrayList<Campo>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	public void abrirCampo(int linha, int coluna) {
		try {
			campos.parallelStream()
			  .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			  .findFirst()
			  .ifPresent(c -> c.abrir());	
		} catch (ExplosaoException e) {
			campos.forEach(c -> c.setAberto(true));
			throw e;
		}	  	
	}
	public void AlternarMarcacao(int linha, int coluna) {
		campos.parallelStream()
			  .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			  .findFirst()
			  .ifPresent(c -> c.alternarMarcacao());
			  
		
	}

	private void gerarCampos() { // gerar os campos do tabuleiro
		for(int linha = 0; linha < linhas; linha++) {
			for(int coluna = 0; coluna < colunas; coluna++) {
				campos.add(new Campo(linha, coluna));
			}
		}
		
	}
	
	private void associarVizinhos() { 
		for (Campo campo1 : campos) {
			for (Campo campo2 : campos) {
				campo1.adicionarVizinho(campo2);
			}
		}
		
	}
	
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		do {
			
			int aleatorio = (int) (Math.random() * campos.size());
			minasArmadas = campos.stream().filter(minado).count();
			campos.get(aleatorio).minar();
		}while(minasArmadas < minas); 
	}
	
	public boolean objetivoAlcancado() {// verificar se todos os campos tem objetivo alcançado
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new  StringBuilder();
		
		sb.append("  ");	
		for (int c = 0; c < colunas; c++){
			sb.append(" ");
			sb.append(c);
			sb.append(" ");
		}
		sb.append("\n");
		
		int i = 0 ;
		for(int l = 0; l < linhas; l++) {
		
			sb.append(l);
			sb.append(" ");
			
		
			for(int c = 0; c < colunas; c++) {
				sb.append(" ");
				sb.append(campos.get(i));
				sb.append(" ");
				i++;
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
}

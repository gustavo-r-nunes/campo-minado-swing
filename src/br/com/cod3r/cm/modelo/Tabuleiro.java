package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Tabuleiro implements CampoObservador {
	
	private final int linhas;
	private final int colunas;
	private final int minas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<Boolean>> observadores = new ArrayList<>();
	
	

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarOsVizinhos();
		sortearMinas();
	}
	
	public void forEach(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	
	public void registrarObservador(Consumer<Boolean> observador) {
		observadores.add(observador);
	}
	
	public void notificarObservadores(boolean resultado) {
		observadores.stream().forEach(o -> o.accept(resultado));
	}
	
	public void abrirCampos(int linha, int coluna) {
		
			campos.parallelStream()
			   .filter(c -> c.getLinha() == linha)
			   .filter(c -> c.getColuna() == coluna)
			   .findFirst()
			   .ifPresent(c -> c.abrir());
		
				
	}
	
	private void mostrarMinas() {
		
		campos.stream()
		.filter(c -> c.isMinado())
		.filter(c -> !c.isMarcado())
		.forEach(c-> c.setAberto(true));
	}
	
	public void marcarCampos(int linha, int coluna) {
		campos.parallelStream()
		   .filter(c -> c.getLinha() == linha)
		   .filter(c -> c.getColuna() == coluna)
		   .findFirst()
		   .ifPresent(c -> c.alternarMarcacao());
				
	}


	private void gerarCampos() {
		for(int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				Campo campo = new Campo(i, j);
				campo.registrarObservador(this);
				campos.add(campo);
				
				
			}
		}
	}
	
	private void associarOsVizinhos() {
		for(Campo c1: campos) {
			for(Campo c2: campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void sortearMinas() {
		long minasArmadas = 0;
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(c -> c.isMinado()).count();
		} while(minasArmadas < minas);
	}
	
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}

	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if(evento == CampoEvento.EXPLODIR) {
			System.out.println("Perdeu");
			mostrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
			System.out.println("Ganhou");
			notificarObservadores(true);
		}
		
	}
	
	public int getLinhas() {
		return this.linhas;
	}
	
	public int getColunas() {
		return this.colunas;
	}
	
	
	
}


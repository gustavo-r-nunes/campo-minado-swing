package br.com.cod3r.cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.cod3r.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {
	
	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		setLayout(new GridLayout(tabuleiro.getLinhas(), tabuleiro.getColunas()));
		
		int total = tabuleiro.getColunas() * tabuleiro.getLinhas();
		
		tabuleiro.forEach(c -> add(new BotaoCampo(c)));	
		
		tabuleiro.registrarObservador(e -> {
			SwingUtilities.invokeLater(() -> {
			if (e == true) {
				JOptionPane.showMessageDialog(this, "Você Ganhou!!!");
		 	} else {
				JOptionPane.showMessageDialog(this, "Você Perdeu");
			}
			
			tabuleiro.reiniciar();
			});
			
		
		});
	}
	

}
 
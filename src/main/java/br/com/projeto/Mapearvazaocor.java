package br.com.projeto;
import javafx.scene.paint.Color;


public class Mapearvazaocor {

    public Color mapearVazaoParaCor(double vazao, double vazaoMaxima) {
        // Normaliza a vazão (0.0 a 1.0)
        double intensidade = vazao / vazaoMaxima;
        
        // Gradiente Simples: Azul (baixa) -> Verde (média) -> Vermelho (alta)
        // O HSB (Hue, Saturation, Brightness) permite mapear a matiz (Hue)
        // 240 (azul) a 0 (vermelho)
        
        // Invertemos para que a intensidade alta (1.0) seja Matiz 0 (vermelho)
        double hue = 240 * (1.0 - intensidade); 
        
        return Color.hsb(hue, 1.0, 1.0); // Saturação e Brilho máximos
    }
}

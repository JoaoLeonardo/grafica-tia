// "variáveis" (recebidas do vertex shader via input)
varying vec4 v_color; // RGBA
varying vec2 v_texCoord0; // Coordenadas da textura

uniform sampler2D u_sampler2D; // usado para "extrair" (amostra) os pixels da textura

void main() {
    // extrai a cor do pixel (texture2D é uma função built-in) e a transforma de acordo com o input
    gl_FragColor = texture2D(u_sampler2D, v_texCoord0) * vec4(255.0, 255.0, 0.0, 1.0);
}
// "variáveis" (recebidas do vertex shader via input)
varying vec4 v_color; // RGBA
varying vec2 v_texCoord0; // coordenadas da textura

uniform sampler2D u_sampler2D; // usado para "extrair" (amostra) os pixels da textura

uniform vec2 u_glowSource; // coordenadas origem da luz

void main() {
    vec4 shadow_color = vec4(255, 0, 0, 0.5);
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);
    gl_FragColor = frag_color * shadow_color;
}

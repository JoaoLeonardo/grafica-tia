// atributos do vértice (constantes)
attribute vec4 a_position; // posição dos três vértices (alocado para posição 0, metadata)
attribute vec4 a_color; // RGBA (alocado na posição 1)
attribute vec2 a_texCoord0; // Coordenadas da textura (alocado na posição 2)

// uniformes (variáveis, podem ser setadas via input)
uniform mat4 u_projTrans; // projeção

// "variáveis" (enviadas para o fragment shader via output)
varying vec4 v_color; // RGBA
varying vec2 v_texCoord0; // Coordenadas da textura

void main() {
    // passa os inputs para o fragment shader
    v_color = a_color;
    v_texCoord0 = a_texCoord0;
    // projeta a posição
    gl_Position = u_projTrans * a_position;
}
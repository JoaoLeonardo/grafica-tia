// "variáveis" (recebidas do vertex shader via input)
varying vec4 v_color; // RGBA
varying vec2 v_texCoord0; // coordenadas da textura

uniform sampler2D u_sampler2D; // usado para "extrair" (amostra) os pixels da textura
uniform vec4 u_glowColor; // cor do efeito
uniform vec2 u_glowSource; // coordenadas origem da luz

bool isSource(vec4 frag_color) {
    float y = (0.349*frag_color.r) + (0.114*frag_color.g) + (0.537*frag_color.b);
    return y >= 0.1f;
}

float fragAlpha(vec2 fragCoord, vec2 sourceCoord) {
    float distance = distance(gl_FragCoord, sourceCoord);
    return distance <= 8 ? 1.0 - distance/10 : 0.0;
}

void main() {
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);

    if (isSource(frag_color)) {
        gl_FragColor = frag_color;
    } else {
        vec4 frag_color = u_glowColor;
        frag_color.a = fragAlpha(gl_FragCoord, u_glowSource);
        gl_FragColor = frag_color;
    }
}

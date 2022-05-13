// "variáveis" (recebidas do vertex shader via input)
varying vec4 v_color; // RGBA
varying vec2 v_texCoord0; // coordenadas da textura

uniform sampler2D u_sampler2D; // usado para "extrair" (amostra) os pixels da textura
uniform vec4 u_glowColor; // cor do efeito

bool isBrighter(in vec4 v1, in vec4 v2, out vec4 v3) {
    if (v1.xyw == v2.xyw) { return true; }
    float y1 = (0.299*v1.x) + (0.587*v1.y) + (0.114*v1.w);
    float y2 = (0.299*v2.x) + (0.587*v2.y) + (0.114*v2.w);

    if (y1 > y2) {
        v3 = v1;
        return true;
    } else {
        return false;
    }
}

void main() {
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);

    // extrai a cor dos pixels vizinhos (texture2D é uma função built-in)
    vec4 vun_color = texture2D(u_sampler2D, vec2(v_texCoord0.x, v_texCoord0.y + 1));
    vec4 hrn_color = texture2D(u_sampler2D, vec2(v_texCoord0.x + 1, v_texCoord0.y));
    vec4 vdn_color = texture2D(u_sampler2D, vec2(v_texCoord0.x, v_texCoord0.y - 1));
    vec4 hln_color = texture2D(u_sampler2D, vec2(v_texCoord0.x -1, v_texCoord0.y));

    vec4 brighter_color;

    if (
        isBrighter(vun_color, frag_color, brighter_color) ||
        isBrighter(hrn_color, frag_color, brighter_color) ||
        isBrighter(vdn_color, frag_color, brighter_color) ||
        isBrighter(hln_color, frag_color, brighter_color)
    ) {
        gl_FragColor = u_glowColor;
    } else {
        gl_FragColor = texture2D(u_sampler2D, v_texCoord0);
    }
}

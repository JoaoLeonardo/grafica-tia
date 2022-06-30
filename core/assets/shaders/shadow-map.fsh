varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

uniform vec2 u_resolution; // resolução da textura
uniform float u_lookup_range; // distância vertical do vizinho a ser verificado

void main() {
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);

    if (frag_color.a > 0.75) {
        frag_color = vec4(0.0, 0.0, 0.0, 0.0);
    } else {
        vec4 nb_color = texture2D(u_sampler2D, v_texCoord0 + vec2(0, u_lookup_range) / u_resolution);
        if (nb_color.a > 0.75) {
            frag_color = vec4(0.0, 0.0, 0.0, 1.0);
        }
    }

    gl_FragColor = frag_color;
}

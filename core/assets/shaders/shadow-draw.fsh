varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

uniform vec2 u_resolution; // resolução da textura

float fragAlpha(vec2 fragCoord, vec2 sourceCoord) {
    float distance = distance(fragCoord, sourceCoord);
    return distance > 10 ? distance * 0.001 : 0.0;
}

void main() {
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);

    for (float i = 0.0; i<= MAX_RANGE; i++) {
        vec2 n_coord = vec2(v_texCoord0.x, v_texCoord0.y + i);

        if (n_coord.y > u_resolution.y) {
            gl_FragColor = frag_color;
            return;
        }

        vec4 n_color = texture2D(u_sampler2D, n_coord);

        if (n_color.a > 0.75) {
            gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
            return;
        }
    }

    gl_FragColor = frag_color;
}
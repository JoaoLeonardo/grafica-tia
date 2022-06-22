varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

uniform vec2 u_lightSource; // coordenadas origem da luz

vec2 calcDeltaToLight() {
    return vec2(u_lightSource.x > v_texCoord0.x ? 1 : -1, u_lightSource.y > v_texCoord0.y ? 1 : -1);
}

bool isOcclusion(vec4 color) {
    return color.a > 0.5;
}

void main() {
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);

    if (isOcclusion(frag_color)) {
        gl_FragColor = vec4(255, 0.0, 0.0, 1.0);
    } else {
        vec2 delta = calcDeltaToLight();

        for (float i = 1.0; i <= 30.0; i += 1.0) {
            vec2 n_texCoord = vec2(v_texCoord0.x + delta.x + i, v_texCoord0.y + delta.y + i);
            vec4 n_color = texture2D(u_sampler2D, n_texCoord);

            if (isOcclusion(n_color)) {
                gl_FragColor = vec4(255, 0.0, 0.0, 1.0);
                return;
            }
        }

        gl_FragColor = v_color;
    }
}
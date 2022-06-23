varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

uniform vec2 u_lightSource; // coordenadas da origem da luz
//uniform vec2 u_resolution; // coordenadas da origem da luz

vec2 calcDeltaToLight() {
    return vec2(u_lightSource.x > v_texCoord0.x ? 1 : -1, u_lightSource.y > v_texCoord0.y ? 1 : -1);
}

bool isOcclusion(vec4 color) {
    return (color.a) < 1;
}

float fragAlpha(vec2 fragCoord, vec2 sourceCoord) {
    float distance = distance(fragCoord, sourceCoord);
    return distance > 10 ? distance * 0.001 : 0.0;
}

void main() {
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);
    if (isOcclusion(frag_color)) {
        gl_FragColor = vec4(255, 0, 0, 1);
    } else {
        frag_color.a = fragAlpha(gl_FragCoord, u_lightSource);
        gl_FragColor = frag_color;
    }
}
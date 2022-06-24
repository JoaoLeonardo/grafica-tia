#define MAX_RANGE 2.0

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

uniform vec2 u_resolution; // resolução da textura

vec4 sampleN(vec2 coord) {
    return (
        texture2D(u_sampler2D, v_texCoord0) +
        texture2D(u_sampler2D, v_texCoord0 + coord) +
        texture2D(u_sampler2D, v_texCoord0 + coord)
    ) / 3.0;
}

void main() {
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);

    for (float i = 1.0; i <= MAX_RANGE; i++) {
        vec4 c_u = sampleN(vec2(0.0, i) / u_resolution);
        vec4 c_r = sampleN(vec2(i, 0.0) / u_resolution);
        vec4 c_d = sampleN(vec2(0.0, -i-1) / u_resolution);
        vec4 c_l = sampleN(vec2(-i, 0.0) / u_resolution);

        frag_color.a = (c_u.a * c_d.a * c_r.a * c_l.a) / 2;

        if (frag_color.a <= 0.1) {
            break;
        }
    }

    gl_FragColor = frag_color;
}

// https://thebookofshaders.com/edit.php?log=180419042235
// https://stackoverflow.com/questions/9779415/how-to-get-pixel-information-inside-a-fragment-shader
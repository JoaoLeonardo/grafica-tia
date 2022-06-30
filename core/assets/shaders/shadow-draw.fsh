#define MAX_RANGE 2

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

uniform vec2 u_resolution; // resolução da textura

vec4 sampleNb(float i) {
    return (
        texture2D(u_sampler2D, v_texCoord0 + vec2(0, i) / u_resolution) +
        texture2D(u_sampler2D, v_texCoord0 + vec2(i, 0) / u_resolution) +
        texture2D(u_sampler2D, v_texCoord0 + vec2(0, -i) / u_resolution) +
        texture2D(u_sampler2D, v_texCoord0 + vec2(-i, 0) / u_resolution)
    ) / 4;
}

void main() {
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);

    if (frag_color.a > 0.0) {
        vec4 sample_med = sampleNb(MAX_RANGE);
        frag_color.a = sample_med.a - 0.5;
    }

    gl_FragColor = frag_color;
}

// https://thebookofshaders.com/edit.php?log=180419042235
// https://stackoverflow.com/questions/9779415/how-to-get-pixel-information-inside-a-fragment-shader
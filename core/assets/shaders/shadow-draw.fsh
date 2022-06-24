varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

uniform vec2 u_resolution; // resolução da textura

void main() {
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);

    vec2 onePixel = vec2(1.0, 1.0) / u_resolution;

    frag_color = (
        texture2D(u_sampler2D, v_texCoord0) +
        texture2D(u_sampler2D, v_texCoord0 + vec2(onePixel.x, 0.0)) +
        texture2D(u_sampler2D, v_texCoord0 + vec2(-onePixel.x, 0.0))
    ) / 3.0;

    // TODO: Gradiente (de acordo com os 4 vizinhos??)

    if (frag_color.a >= 0.75) {
        frag_color.a = 0.5;
    }

    gl_FragColor = frag_color;
}

// https://thebookofshaders.com/edit.php?log=180419042235
// https://stackoverflow.com/questions/9779415/how-to-get-pixel-information-inside-a-fragment-shader
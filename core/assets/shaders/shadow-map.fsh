#define MAX_RANGE 30

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

uniform vec2 u_resolution; // resolução da textura

void main() {
    float dst = 1.0;

    for (float y=0.0; y<u_resolution.y; y+=1.0) {
        float d = y/u_resolution.y;
        vec2 coord = vec2(v_texCoord0.x, dst);
        vec4 data = texture2D(u_sampler2D, coord);

        if (data.r > 0.0) {
            dst = min(dst, d);
        }
    }

    gl_FragColor = vec4(dst);
}

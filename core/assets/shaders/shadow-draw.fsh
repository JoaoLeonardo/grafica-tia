#define PI 3.14

varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sample2D;
uniform vec2 u_resolution;

float sampleMap(vec2 coord, float r) {
    return step(r, texture2D(u_sample2D, coord).r);
}

void main(void) {
    vec2 norm = v_texCoord0.st * 2.0 - 1.0;
    float theta = atan(norm.y, norm.x);
    float r = length(norm);
    float coord = (theta + PI) / (2.0*PI);

    vec2 tc = vec2(coord, 0.0);

    float center = sampleMap(tc, r);

    float blur = (1./u_resolution.x)  * smoothstep(0., 1., r);

    float sum = 0.0;

    sum += sampleMap(vec2(tc.x - 4.0*blur, tc.y), r) * 0.05;
    sum += sampleMap(vec2(tc.x - 3.0*blur, tc.y), r) * 0.09;
    sum += sampleMap(vec2(tc.x - 2.0*blur, tc.y), r) * 0.12;
    sum += sampleMap(vec2(tc.x - 1.0*blur, tc.y), r) * 0.15;

    sum += center * 0.16;

    sum += sampleMap(vec2(tc.x + 1.0*blur, tc.y), r) * 0.15;
    sum += sampleMap(vec2(tc.x + 2.0*blur, tc.y), r) * 0.12;
    sum += sampleMap(vec2(tc.x + 3.0*blur, tc.y), r) * 0.09;
    sum += sampleMap(vec2(tc.x + 4.0*blur, tc.y), r) * 0.05;

    gl_FragColor = v_color * vec4(vec3(1.0), sum * smoothstep(1.0, 0.0, r));
}
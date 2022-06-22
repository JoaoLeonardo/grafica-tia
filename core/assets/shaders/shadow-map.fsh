varying vec4 v_color;
varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

void main() {
    vec4 shadow_color = vec4(255, 255, 255, 1.0);
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);
    gl_FragColor = shadow_color;
}

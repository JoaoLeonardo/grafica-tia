// "variáveis" (recebidas do vertex shader via input)
varying vec4 v_color; // RGBA
varying vec2 v_texCoord0; // coordenadas da textura

uniform sampler2D u_sampler2D; // usado para "extrair" (amostra) os pixels da textura
uniform vec4 u_glowColor; // cor do efeito
uniform vec2 u_glowSource; // coordenadas origem da luz

bool isSource(vec4 frag_color) {
    float y = (0.349*frag_color.r) + (0.114*frag_color.g) + (0.537*frag_color.b);
    return y >= 0.1f;
}

float fragAlpha(vec2 fragCoord, vec2 sourceCoord) {
    vec2 distance = vec2(fragCoord.x - sourceCoord.x, fragCoord.y - sourceCoord.y);

    int threshhold = 1;
    while (threshhold < 3) {
        if ((distance.x <= threshhold && distance.x > 0) || (distance.y <= threshhold && distance.y > 0)) {
            return 0.8 - (threshhold / 10);
        } else if ((distance.x >= -threshhold && distance.x < 0) || (distance.y >= -threshhold && distance.y < 0)) {
            return 0.8 - (threshhold / 10);
        }
        threshhold += 1;
    }

    return 0.0;
}

void main() {
    vec4 frag_color = texture2D(u_sampler2D, v_texCoord0);

    if (isSource(frag_color)) {
        gl_FragColor = frag_color;
    } else {
        vec4 frag_color = u_glowColor;
        frag_color.a = fragAlpha(gl_FragCoord, u_glowSource);
        gl_FragColor = frag_color;
    }
}

/**

bool isBrighter(in vec4 v1, in vec4 v2) {
    if (v1.xyw == v2.xyw) { return true; }
    float y1 = (0.299*v1.x) + (0.587*v1.y) + (0.114*v1.w);
    float y2 = (0.299*v2.x) + (0.587*v2.y) + (0.114*v2.w);
    return y1 > y2;
}

vec2 findSource(in vec2 pos, in vec2 last_pos) {
    vec4 current = texture2D(u_sampler2D, pos);

    vec4 up = texture2D(u_sampler2D, vec2(pos.x, pos.y + 1));
    if (isSource(up)) {
        return vec2(pos.x, pos.y + 1);
    } else if (isBrighter(up, current)) {

    }

    vec4 right = texture2D(u_sampler2D, vec2(pos.x + 1, pos.y));
    if (isSource(right)) {
        return vec2(pos.x + 1, pos.y);
    } else if (isBrighter(right, current)) {
        return findSource(right, current);
    }

    vec4 down = texture2D(u_sampler2D, vec2(pos.x, pos.y - 1));
    if (isSource(down)) {
        return vec2(pos.x, pos.y - 1);
    } else if (isBrighter(down, current)) {
        return findSource(down, current);
    }

    vec4 left = texture2D(u_sampler2D, vec2(pos.x - 1, pos.y));
    if (isSource(left)) {
        return vec2(pos.x - 1, pos.y);
    } else if (isBrighter(left, current)) {
        return findSource(left, current);
    }

    return vec2(0,0);
}

float fragAlpha(vec2 fragCoord, vec2 sourceCoord) {
    vec2 distance = vec2(fragCoord.x - sourceCoord.x, fragCoord.y - sourceCoord.y);

    int threshhold = 2;
    while (threshhold < 10) {
        if (distance.x <= threshhold || distance.y <= threshhold) {
            return 1.0 - threshhold;
        }
        threshhold += 2;
    }

    return 0.0;
}
**/

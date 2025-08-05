#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 screenPos;

out vec4 fragColor;

// Simple rounded rectangle function
float roundedBox(vec2 pos, vec2 size, float radius) {
    vec2 d = abs(pos) - size + radius;
    return length(max(d, 0.0)) + min(max(d.x, d.y), 0.0) - radius;
}

void main() {
    vec2 uv = texCoord0;
    vec2 pos = (uv - 0.5) * 2.0;
    
    // Create rounded rectangle
    float dist = roundedBox(pos, vec2(0.9, 0.7), 0.1);
    float alpha = 1.0 - smoothstep(-0.02, 0.02, dist);
    
    // Base color from vertex
    vec3 color = vertexColor.rgb;
    
    // Add gradient effect
    color = mix(color * 0.7, color * 1.3, uv.y);
    
    // Add subtle glow
    float glow = exp(-dist * 8.0) * 0.3;
    color += glow * vec3(0.4, 0.2, 0.8);
    
    fragColor = vec4(color, alpha * vertexColor.a);
    
    // Apply fog
    float fogFactor = smoothstep(FogStart, FogEnd, length(screenPos));
    fragColor.rgb = mix(fragColor.rgb, FogColor.rgb, fogFactor * FogColor.a);
}